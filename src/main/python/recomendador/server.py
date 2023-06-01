

import json
import socket as soq
from socket import socket
from socket import AF_INET, SOCK_STREAM

from recommender import Recommender

from log import logger

## Server Configuration
HOST = "127.0.0.1"
PORT = 9090  

def wait_model_loading ( connection: socket, recommender: Recommender):
    modelos_cargados = False
    #recommender.load_model()
    while not modelos_cargados:
        try:
            _ = connection.recv(1024)

            modelos_cargados = recommender.is_active()
            
            if not modelos_cargados:
                response = { 'code': 500 }
            else: 
                response = { 'code': 200 }
            
            response = json.dumps( response, indent = 2)
            connection.send( bytes ( response , encoding = 'utf-8'))
            
        except (ConnectionResetError, ConnectionAbortedError):
            logger.warning("User Has Abandoned the Connection")    
            return False
    return True

def process_petition(connection: socket, address, recommender: Recommender):
    logger.info(f"Connection Established: ({address[0]}) - ({address[1]})")
    
    keep_conected = wait_model_loading(connection, recommender)

    i = 0
    while keep_conected:
        petition = connection.recv(1024)
        petition = petition.decode('utf-8')
        
        if len(petition) == 0: 
            keep_conected = False
            continue

        try: 
            petition = json.loads(petition)
            logger.info(json.dumps(petition, indent = 2))
            
            response = recommender.recommend(i, petition)

        except json.JSONDecodeError:
            response = { "code": 401 }
            logger.error("Fail to Create M")

        connection.send( bytes ( json.dumps( response, indent = 2), encoding = 'utf-8'))
        i += 1

def main(): 

    recomemnder = Recommender()

    soc  = socket(AF_INET, SOCK_STREAM)
    soc.bind((HOST, PORT))
    soc.listen()

    client_found = False
    while not client_found:
        connection, address = soc.accept()
        process_petition(connection, address, recomemnder)
        connection.close()

        client_found = True
    




if __name__ == '__main__': main()