FROM python:3.9-bullseye

WORKDIR /usr/src/recommender

COPY requirements.txt ./
RUN pip install --no-cache-dir torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpup
RUN pip install --no-cache-dir dgl -f https://data.dgl.ai/wheels/repo.html
RUN pip install --no-cache-dir dglgo -f https://data.dgl.ai/wheels-test/repo.html
RUN pip install --no-cache-dir networkx=2.8.8

COPY ./model ./model
COPY ./data ./data

COPY ./src/main/python/recomendador .

EXPOSE 9090:9090

CMD [ "python", "./server.py" ]