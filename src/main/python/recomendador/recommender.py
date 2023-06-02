
import os

import numpy as np
import pandas as pd

import time
from collections import defaultdict

import torch
import dgl

import torch
import torch.nn.functional as F

import networkx as nx
from multiprocessing import Queue, Process, Value

from log import logger
from utils import load_features, load_graph
"""
MODEL_PATH    : str = "/usr/local/recommender/model/predictor-recommender.pt"
EMBEDINGS_PATH: str = "/usr/local/recommender/model/embeddings.pt"
GRAPH_PATH    : str = "/usr/local/recommender/data/graph.gpickle"
FEATURES_DATA : str = "/usr/local/recomemnder/data/songset_features.csv"
"""
MODEL_PATH    : str = "./model/predictor-recommender.pt"
EMBEDINGS_PATH: str = "./model/embeddings.pt"
GRAPH_PATH    : str = "./data/graph.gpickle"
FEATURES_DATA : str = "./data/songset_features.csv"

GAMMA: float = 0.95
NUMBER:  int = 5

import signal

class Recommender():
    def __init__(self):
        self.input_queue     = Queue()
        self.output_queue    = Queue()
        self.model_is_active = Value('b', False)

        self.process = Process(target = self.recommend_process, args = (self.input_queue, self.output_queue))
        self.process.daemon = True
        self.process.start()

    def is_active(self):
        return bool(self.model_is_active.value)

    def set_active(self):
        with self.model_is_active.get_lock():
            self.model_is_active.value = True

    def set_inactive(self):
        with self.model_is_active.get_lock():
            self.model_is_active.value = False

    def load_model(self) -> None:
        logger.info("Model:  Loading")
        self.embeddings = torch.load(EMBEDINGS_PATH)
        self.model_pred = torch.load(MODEL_PATH)
        logger.info("Model:  Loaded")
        self.load_data()
        self.set_active()

    def load_data(self) -> None:
        logger.info("Data :  Loading Features")
        start = time.perf_counter()
        self.features, self.uri_map = load_features(FEATURES_DATA, normalize = True)
        end   = time.perf_counter()
        logger.info("Data :  Loaded  Features (Time: %6.2f)", end - start)
        
        logger.info("Data :  Loading Graph")
        start = time.perf_counter()
        self.graph, self.weight = load_graph(GRAPH_PATH, self.uri_map)
        end   = time.perf_counter()
        logger.info("Data :  Loaded  Graph (Time: %6.2f)", end - start)

    def recommend_process(self, input_queue: Queue, output_queue: Queue):
        signal.signal(signal.SIGINT, signal.SIG_IGN)
        self.load_model()
        
        self.seeds = None
        keep = self.is_active()
        try:
            while keep: 
                attemps = 0
                while not input_queue.empty() and attemps < 20:
                    input_data = input_queue.get(timeout = 1.5)
                    logger.debug("%s", input_data)

                    if input_data['all']: self.search = None
                    else: self.search = input_data['filtered']
                    self.seeds = input_data['seeds']

                    attemps += 1
                    
                if self.seeds is not None: 
                    if self.search is not None:
                        logger.debug("Petition for Recomendation with Filtered Items")
                        recommendations = self.get_recomendations_from_list()
                    else:
                        logger.debug("Petition for Recomendation with All Songs")
                        recommendations = self.get_recomendations()

                    response = {
                        'status': 202,
                        'uris': recommendations
                    }
                    output_queue.put(response)

                    self.search = None
                    self.seeds  = None

                #logger.debug("%s", keep)
                keep = self.is_active()
                #logger.debug("%s", keep)

        except KeyboardInterrupt:
            logger.warning("Recommender is Closing")

        logger.warning("Recommender is  Closed")
    

    def get_recomendations( self ): 
        listed = list(self.uri_map)
        score_dict = defaultdict(dict)

        seed_songs = []
        for s in self.seeds:
            s = self.uri_map[s]
            _, candidates = self.graph.out_edges(s, form='uv')
            candidates = [c.item() for c in candidates]
            seed_songs.append(s)        
            seed_songs.extend(candidates)

        for song in self.seeds:
            song = self.uri_map[song]
            _, candidates = self.graph.out_edges(song, form = 'uv')

            song_embedding = self.embeddings[song].unsqueeze(dim=0)
            edge_embeds = [torch.cat([song_embedding, self.embeddings[c.item()].unsqueeze(dim=0)],1) for c in candidates]
            edge_embeds = torch.cat(edge_embeds, 0)

            scores = self.model_pred.W1(edge_embeds)
            scores = self.model_pred.W2(F.relu(scores)).squeeze(1)

            for candidate, score in zip(candidates.detach().numpy(), scores.detach().numpy()):
                if listed[candidate] not in self.seeds and candidate != s: 
                    score_dict[candidate] = score_dict.get(candidate, 0) + score
                    #score_dict[candidate] = (1 - GAMMA) * score_dict.get(candidate, 0) + GAMMA * score
                    #score_dict[candidate] = 2 / ( 1 / score_dict.get(candidate, 0) + 1 / score)
                    # score_dict[candidate] = max(score_dict.get(candidate, 0), score)

        # Get uris            
        ret = sorted(list(score_dict.items()), key = lambda v: v[1], reverse = True)[:NUMBER]
        return list(map(lambda v: listed[v[0]], ret))

    def get_recomendations_from_list( self ): 
        listed = list(self.uri_map)
        score_dict = { candidate: 0 for candidate in self.search}

        seeds = [ self.uri_map[s] for s in self.seeds ] 
        for candidate in score_dict.keys():
            candidate_uri = self.uri_map[candidate]
            edge_embeds = [torch.cat([self.embeddings[song].unsqueeze(dim=0), self.embeddings[candidate_uri].unsqueeze(dim=0)],1) for song in seeds]
            edge_embeds = torch.cat(edge_embeds, 0)

            scores = self.model_pred.W1(edge_embeds)
            scores = self.model_pred.W2(F.relu(scores)).squeeze(1)

            score_dict[candidate] = torch.sum(scores)
            #score_dict[candidate] = torch.max(scores)
            #score_dict[candidate] = torch.mean(scores)
            #score_dict[candidate] = torch.median(scores)

        ret = sorted(list(score_dict.items()), key = lambda v: v[1], reverse = True)[:NUMBER]
        return list(map(lambda v: v[0], ret))

    def recommend(self, seeds):
        self.input_queue.put(seeds)
        return self.output_queue.get()
    
    def shutdown(self):
        logger.warning("Setting Inactive Service")
        self.set_inactive()
        self.process.join()
        self.input_queue.close()
        self.output_queue.close()
        logger.warning("Service is Inactive")
    