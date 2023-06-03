

import json
import socket as soq
from socket import socket
from socket import AF_INET, SOCK_STREAM

from recommender import Recommender

from log import logger
import signal


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
            
            # if not modelos_cargados:
            #     response = { 'status': 500 }
            # else: 
            #     response = { 'status': 200 }
            # 
            # response = json.dumps( response )
            # connection.send( bytes ( response , encoding = 'utf-8'))
            
        except (ConnectionResetError, ConnectionAbortedError):
            logger.warning("User Has Abandoned the Connection")    
            return False
    return True

"""
Search Between All Songs
{
    "all": true,
    "seeds": [" dsadsdadsa", "dssdsasdadsa"],
    "urls": []
}

Search Between a Collection of Songs

{
    "all": false,
    "seeds": [" dsadsdadsa", "dssdsasdadsa"]
}

"""


def process_petition(connection: socket, address, recommender: Recommender):
    logger.info(f"Connection Established: ({address[0]}) - ({address[1]})")
    
    keep_conected = wait_model_loading(connection, recommender)

    i = 0
    try:
        while keep_conected:
            petition = connection.recv(5000)
            petition = petition.decode('utf-8')
            
            if len(petition) == 0: 
                keep_conected = False
                continue

            try: 
                petition = json.loads(petition)
                # logger.info(json.dumps(petition, indent = 2))
                
                response = recommender.recommend(petition)
                response = json.dumps( response ) + "\n"
                logger.info(response)
                connection.send( bytes ( response, encoding = 'utf-8'))

            except json.JSONDecodeError:
                response = { "status": 401 }
                logger.error("Fail to Create M")

            i += 1

    except KeyboardInterrupt:
        recomender.shutdown()

    logger.info(f"Connection Finalized  : ({address[0]}) - ({address[1]})")

# def handler(signum, frame):
#     logger.info("Closing Service")
#     logger.info("%s", type(recomender))
#     recomender.shutdown()
# 
# signal.signal(signal.SIGINT, handler)


def main(): 
    global recomender

    logger.info("Initiating the Server")

    recomender = Recommender()


    soc  = socket(AF_INET, SOCK_STREAM)
    soc.bind((HOST, PORT))
    soc.listen()

    logger.info("Start")
    while True:
        try:
            connection, address = soc.accept()
            process_petition(connection, address, recomender)

        except KeyboardInterrupt:
            recomender.shutdown()
            break

        except soq.timeout:
            logger.debug("Timeout: Fail to Find New User")
            recomender.shutdown()
            break

if __name__ == '__main__': main()