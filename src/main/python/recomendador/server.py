

import json
import socket as soq
from socket import socket
from socket import AF_INET, SOCK_STREAM

from recommender import Recommender

## Logger Configuration
import logging
logger = logging.getLogger(name = "server-main")
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

stream_handler = logging.StreamHandler()
stream_handler.setFormatter(formatter)

logger.setLevel(logging.INFO)
logger.addHandler(stream_handler)

## Server Configuration
HOST = "127.0.0.1"
PORT = 9090  

def wait_model_loaded ( connection: socket, recommender):
    modelos_cargados = False
    #recommender.load_model()

    logger.info(f"Loading Model Status: Not Loaded")
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

    logger.info(f"Loading Model Status: Loaded")
    return True

def process_petition(connection: socket, address):
    logger.info(f"Connection Established: ({address[0]}) - ({address[1]})")
    recommender = Recommender()

    keep_conected = wait_model_loaded(connection, recommender)

    while keep_conected:
        petition = connection.recv(1024)
        petition = petition.decode('utf-8')
        
        if len(petition) == 0: 
            keep_conected = False
            continue

        try: 
            petition = json.loads(petition)
            logger.info(json.dumps(petition, indent = 2))
            response = { "code": 202 }

        except json.JSONDecodeError:
            response = { "code": 401 }
            logger.error("Fail to Create M")

        connection.send( bytes ( json.dumps( response, indent = 2), encoding = 'utf-8'))


def main(): 
    
    soc  = socket(AF_INET, SOCK_STREAM)
    soc.bind((HOST, PORT))
    soc.listen()

    client_found = False
    while not client_found:
        connection, address = soc.accept()
        client_found = True
    
    process_petition(connection, address)
    connection.close()




if __name__ == '__main__': main()