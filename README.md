# Recomendador de Spotify con Agentes

Este proyecto implementa un recomendador de Spotify usando un sistema de agentes JADE ( Jade Agent DEvelopment Framework). 

## Autores

Este trabajo fue realizado por:

| Nombre                    | Email                               |
|---------------------------|-------------------------------------|
| Rubén Cid Costa           | ruben.cid@alumnos.upm.es            |
| Rodrigo Durán Andrés      | rodrigo.duran.andres@alumnos.upm.es |
| Ignacio Guzmán García     | ignacioguzman.garcia.luengo@alumnos.upm.es|
| Nerea Rodriguez Francisco | nerea.rfrancisco@alumnos.upm.es     |
| Jorge Saenz de Miera      | jorge.saenzdemiera@alumnos.upm.es   |

## Lista de TODO
En el archivo TODO.md tienen acceso a la lista partes faltante. Marquen cuando acaben.
Pueden hacer click en el siguiente enlace para ver: [todo](./TODO.md)



## Uso 

Nota: para parar el servidor se requiere usar el administrador de tareas.

1) Descargar la información del grafo de canciones. Este grafo se puede descargar del siguiente enlace y se debe colocar en `./data`. Este es el [enlace](https://upm365-my.sharepoint.com/:u:/g/personal/ruben_cid_alumnos_upm_es/ESyOC13tmgpFuG6VwIeqriEBsiPcyI5bPeeT6Qod3dRNWw?e=xlaFId).
2) Descargar las librerias de python: 
```bash
pip install --no-cache-dir torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpup  

pip install --no-cache-dir dgl -f https://data.dgl.ai/wheels/repo.html

pip install --no-cache-dir dglgo -f https://data.dgl.ai/wheels-test/repo.html

pip install --no-cache-dir networkx=2.8.8
```

3) Ejecutar el servidor con el comando:

```python
python ./src/python/recommend/server.py
```
Se debe esperar a la carga del grafo en los logs de la consola.


4) Situarse en la carpeta del proyecto que contiene el archivo 'docker-compose.yml' y ejecutar el comando: docker compose up -d
5) Abrir el IDE Eclipse y añadir las librerias de la carpeta 'lib' al java build path del proyecto
6) Ir al archivo Main.java y ejecutarlo como una Java application
7) Introducir 5 canciones por la interfaz que se presenta. Deben ser canciones que existan en la base de datos. Aquí se presenta una lista con 20 opciones:

- Abeja Miope;
- If I Gave Myself To Someone Else;
- Mercy;
- Still Got Time (feat. PARTYNEXTDOOR);
- It's Alright Now;
- Ginger;
- I Ain't Safe;
- Yee - Original Mix;
- Jasmine Runs Away;
- Hit It From The Back;
- Middle;
- Halloween, 1987;
- Mamzad (Serjik);
- At Your Feet (Live);
- Be Alright (feat. EMEL);
- The Way I Am;
- I Can't Live Without You;
- Heart and Soul - From 'Camp Rock 2: The Final Jam';
- Early Morning Riser;
- Those Who Know
