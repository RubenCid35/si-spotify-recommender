## Logger Configuration
import logging
logger = logging.getLogger(name = "server-main")
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

stream_handler = logging.StreamHandler()
stream_handler.setFormatter(formatter)

logger.setLevel(logging.DEBUG)
logger.addHandler(stream_handler)
