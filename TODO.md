
# Lista TODO:
`NOTA: un TODO es un elemento o tarea a realizar`

Esto es una lista de componentes que faltan por completar. Si una actividad ha sido completada, se debe marcar como `[x]`

## Común: Mensajes y Utiles

* [ ] Definir el esquema de los mensajes de interfaz a catalogo para filtrar
* [ ] Definir el esquema de los mensajes de catalogo a recomendador para crear las recomendaciones
* [ ] Definir el esquema de los mensajes de recomendador a catalogo para solicitar información de las canciones que se muestran
* [ ] Definir el esquema de los mensajes de recomendador a interfaz para mostrar información de las canciones que se recomiendan

## Agente Visualización

* [ ] Crear diseño en Swing
* [ ] Crear y Diseñar los comportamientos
* [ ] Dividir los comportamientos por uso
* [ ] Implementar los comportamientos

## Agente Catalogo

* [ ] TODO


## Agente Recomendador (JAVA)

* [ ] Crear y Definir los comportamientos del recomendador de Java

    * [ ] Comportamiento 1 (Circular  ) : Recibir y deserializar el mensaje de catalogo. 
    * [ ] Comportamiento 2 (Secuencial) : Mandar el mensaje al contenedor de Python
    * [ ] Comportamiento 3 (Secuencial) : Solicitar la información de las mejores recomendaciones
    * [ ] Comportamiento 4 (Secuencial) : Mandar la Información a Interfaz

* [ ] Crear conexión con el recomendador de python
* [ ] Solicitar la información de las N recomendaciones a catalogo. 
* [ ] Devolver la información a la interfaz.

## Agente Recomendador (Python)
* [ ] Crear servidor para recibir peticiones
* [ ] Deserializar los mensajes y procesarlos
* [x] Crear modelo de recomendaciones
* [ ] Crear DockerFile
* [ ] Implementar el uso del modelo.
