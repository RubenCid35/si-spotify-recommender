
import os

import numpy as np
import pandas as pd

import time
from collections import defaultdict

import torch
import dgl

import networkx as nx

import re
from torch.nn.functional import normalize

from multiprocessing import Queue, Process, Value

MODEL_PATH: str = ""
EMBDINGS_PATH: str = ""

class Recommender():
    def __init__(self):
        self.model_is_active = Value('b', False)

    def is_active(self):
        active = False
        with self.model_is_active.get_lock():
            active = self.model_is_active.value
        return active


    def load_model(self) -> None:
        with self.model_is_active.get_lock():
            self.model_is_active.value = True        
