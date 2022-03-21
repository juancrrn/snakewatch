# Developement sandbox and cheat sheet

## SSH y máquina virtual

Los comandos que se lanzan sobre SSH mueren cuando termina la conexión. Tmux permite solucionar esto.

```console
$ tmux new -s iw
$ git clone https://github.com/juancrrn/snakewatch
$ cd snakewatch
$ mvn spring-boot:run

Ctrl + b + d # Desconectarse de la sesión
```

## Tests con Maven y Karate

```console
$ mvn test -Dtest=ExternalRunner
```