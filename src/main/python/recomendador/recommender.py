
import os

import numpy as np
import pandas as pd

import time
from collections import defaultdict

import torch
import dgl

import networkx as nx
from torch.nn.functional import normalize

from multiprocessing import Queue, Process, Value

from log import logger
from utils import load_features, load_graph
"""
MODEL_PATH    : str = "/usr/local/recommender/model/predictor-recommender.pt"
EMBEDINGS_PATH: str = "/usr/local/recommender/model/embedding-model.pt"
GRAPH_PATH    : str = "/usr/local/recommender/data/graph.gpickle"
FEATURES_DATA : str = "/usr/local/recomemnder/data/songset_features.csv"
"""
MODEL_PATH    : str = "./model/predictor-recommender.pt"
EMBEDINGS_PATH: str = "./model/embedding-model.pt"
GRAPH_PATH    : str = "./data/graph.gpickle"
FEATURES_DATA : str = "./data/songset_features.csv"



class Recommender():
    def __init__(self):
        self.input_queue = Queue()
        self.output_queue = Queue()
        self.model_is_active = Value('b', False)

        self.process = Process(target = self.recommend_process, args = (self.input_queue, self.output_queue))
        self.process.daemon = True
        self.process.start()

    def is_active(self):
        with self.model_is_active.get_lock():
            active = self.model_is_active.value
        return active

    def set_active(self):
        with self.model_is_active.get_lock():
            self.model_is_active.value = True

    def load_model(self) -> None:
        logger.info("Model: ........................ Loading")
        self.embeddings = torch.load(EMBEDINGS_PATH)
        self.model_pred = torch.load(MODEL_PATH)
        logger.info("Model: ........................ Loaded")
        self.load_data()

        self.set_active()

    def load_data(self) -> None:
        logger.info("Data : ........................ Loading Features")
        self.features, self.uri_map = load_features(FEATURES_DATA, normalize = True)
        logger.info("Data : ........................ Loading Graph")
        self.graph, self.weight = load_graph(GRAPH_PATH, self.uri_map)
        logger.info("Data : ........................ Loaded")

    def recommend_process(self, input_queue: Queue, output_queue: Queue):
        self.load_model()
        
        self.seeds = None
        while True: 
            while not input_queue.empty() and self.seeds is not None:
                self.seeds = input_queue.get()
                
            logger.info(self.seeds)
            output_queue.put({ "code": 202 })


    def recommend(self, i, seeds):
        self.input_queue.put((i, seeds))
        return self.output_queue.get()