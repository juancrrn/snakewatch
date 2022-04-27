# Developement sandbox and cheat sheet

## SSH y máquina virtual

Conectarse al servidor con ssh:
```
$ ssh contenedor
```
Los comandos que se lanzan sobre SSH mueren cuando termina la conexión. Tmux permite solucionar esto.

```console
$ tmux new -s iw
$ rm -rf snakewatch
$ git clone https://github.com/juancrrn/snakewatch
```
Cambiar el modo de compilacion a debug=false y lanzarlo
```
$ nano snakewatch/src/main/resources/application.properties
$ cd snakewatch
$ mvn spring-boot:run
```
Desconectarse de la sesion tmux y del servidor
```
Ctrl + b + d     # Desconectarse de la sesión
$ exit           # Desconectarse del servidor
```

## Tests con Maven y Karate

```console
$ mvn test -Dtest=ExternalRunner
```

## Configuracion del laboratorio para examen
En escritorio: 
1. abrir carpeta "Publicacion Docente de Ficheros en LABs" > "ALUMNO recogida docente"
  - Izq: local
  - Dcha: servidores lab
2. Ir a /publico/todos/iw (en parte dcha)
3. Copiar los 4 ficheros de dentro a la parte izq, en la ruta local que eligas ( preferiblemente U:/hlocal/iw)
4. Seguir los pasos indicados en fichero "como_montar_entorno_iw_en_windows"
5. Si te da fallos de paths, editar el .vat y renombrar los paths. Ejemplo:
``` 
set JAVA_HOME=c:\hlocal\OSCAR\jdk
set PATH=c:\hlocal\OSCAR\jdk\bin;c:\hlocal\OSCAR\maven\bin;%PATH%
start code snakewatch-main
