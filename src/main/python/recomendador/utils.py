import re
import torch
import numpy as np
import networkx as nx

import torch.nn.functional as functional
import dgl

def load_features(feat_dir, normalize=True):

    data = np.genfromtxt(feat_dir, delimiter=',', skip_header=True, dtype=str)
    data = data[np.argsort(data[:, 13])]
    features = np.array(np.delete(data[:,1:], [11, 12, 13, 14, 15], 1), dtype=float)
    
    if normalize:
        features = functional.normalize(torch.Tensor(features), dim=0)

    uris = data[:, 14]
    uris = [re.sub('spotify:track:', '', uri) for uri in uris]
    uri_map = {n: i for i,n in enumerate(uris)}

    return features, uri_map

def load_graph(gpickle_dir, uri_map):
    G = nx.read_gpickle(gpickle_dir)

    src, dest = [], []
    weights = []
    for e in G.edges.data('weight'):
        uri_u, uri_v, w = e
        u, v = uri_map[uri_u], uri_map[uri_v]
        src.append(u)
        dest.append(v)
        w = G[uri_u][uri_v]['weight']
        weights.append(w)
  
    #make double edges
    src, dest = torch.tensor(src), torch.tensor(dest)
    src, dest = torch.cat([src, dest]), torch.cat([dest, src])
    dgl_G = dgl.graph((src, dest), num_nodes=len(G.nodes))
    
    #store edge weights in graph
    weights = torch.FloatTensor(weights+weights)
    dgl_G.edata['weights'] = weights
    
    return dgl_G, weights
