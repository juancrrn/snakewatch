# Snakewatch

Snakewatch es una aplicación web dedicada al videojuego de la serpiente o _Snake_ en versión _online_ y multijugador.

El proyecto está desarrollado dentro de la asignatura de Ingeniería Web del Grado en Ingeniería Informática de la Universidad Complutense de Madrid, y utiliza tecnologías como Spring, Maven y Thymeleaf.

##### Table of Contents

- [Propuesta de proyecto y funcionalidad](#propuesta-de-proyecto-y-funcionalidad)
    - [La serpiente](#la-serpiente)
    - [Catch](#catch)
    - [Roles de usuario](#roles-de-usuario)
- [Funcionalidad desarrollada](#funcionalidad-desarrollada)
- [Vistas](#vistas)
- [Documentación del proyecto](#documentación-del-proyecto)  
    - [Clases de Spring Boot](#clases-de-spring-boot)
    - [Clases del dominio](#clases-del-dominio)
- [Desarrollo](#desarrollo)
    - [Herramientas](#herramientas)
- [Referencias, _cheat sheets_ y _shortcuts_](#referencias-cheat-sheets-y-shortcuts)
    - [Maven y ejecución](#maven-y-ejecución)
    - [Spring y componentes](#spring-y-componentes)
    - [Thymeleaf](#thymeleaf)
    - [Guías de Spring](#guías-de-spring)
    - [Base de datos](#base-de-datos)

##### Autores

- Daniel Marín Irún
- Juan Carrión Molina
- Mohamed Ghanem
- Óscar Caro Navarro
- Óscar Molano Buitrago

## Propuesta de proyecto

### La serpiente

En resumen, la dinámica del juego consiste en que el jugador controla una criatura larga y delgada, similar a una serpiente, que se mueve sobre un plano delimitado recogiendo alimentos. El jugador debe evitar que la serpiente se golpee contra su propia cola o contra las paredes que delimitan el plano.

Se pueden obtener más detalles en la [página de Wikipedia de la serpiente](https://es.wikipedia.org/wiki/La_serpiente_(videojuego)).

### Catch

> ¿Viciado al Elden Ring o al God of war Ragnarök? Si eres más de clásicos, ¡estás de suerte! Con Snakewatch podrás jugar al mítico y legendario Snake con tus amigos. Gracias a su entorno multijugador, la partida se sincronizará para que luchéis por ver quién es el amo del mapa. ¡Sé el último en sobrevivir y álzate con la victoria!

### Roles de usuario

- **Usuario guest**: Solo podrá unirse a una sesión existente y no podrá alojar la suya propia. No tendrán acceso a ninguna de las características secundarias, como equipar skins o aparecer en los rankings. De este usuario no se guardará información.
- **Usuarios registrados**: Estos usuarios poseen un perfil propio que les permite:
    - Crear sesión
    - Participar en los rankings
    - Obtener y/o cambiar skins
    - Consultar historial de partidas
    - Reportar usuarios, para que los administradores comprueben la respectiva cuenta y tomen las medidas oportunas.
    - Una vez que un usuario haya iniciado sesión, podrá acceder a la vista de usuario desde la que puede administrar la configuración de su cuenta (cambiar nombre de usuario, contraseña) o en la configuración del juego, como skins, títulos o consultar los rankings, etc.
- **Usuario espectador**: Estos usuarios solo podrán ver los juegos en curso. Por lo general, estos son usuarios a los que les gusta transmitir partidos y hacer comentarios en vivo.
Estos usuarios son muy similares a los "Guests" en el sentido de que no necesitan registrarse y no se guardará ninguna información para ellos, sin embargo, lo que los define es que requieren la clave del juego para poder verlo.
- **Administrador**: Los altos mandos de la plataforma. Solo actúa cuando se notifican muchos reportes a una persona. Son los que pueden eliminar a usuarios de forma temporal o permanente de la aplicación.

## Funcionalidad desarrollada

### A

### B

### C

## Vistas

- **Lobby**: La pantalla de lobby es la pantalla de inicio del juego, donde un usuario una vez se ha registrado en la aplicación web podrá seleccionar como desee jugar, si multijugador (play online) o un jugador solo (levels), así como observar otras partidas (spectate). También podrá acceder a su perfil, a los rankings o en caso de ser administrador a la pantalla de administrador.
- **Perfil de usuario**: La pantalla de perfil incluirá el perfil del usuario con su respectivo nombre y foto de perfil, así como los amigos que tiene, el número total de partidas jugadas y ganadas y un historial con las partidas recientes.
- **Niveles**: La pantalla de niveles es la pantalla de un solo jugador, en la que el usuario podrá seleccionar el nivel al que desee jugar, donde habrá distintas dificultades por nivel, al seleccionar el nivel tendrá que superar la dificultad que incluya dicho nivel.
- **Administrador**: La pantalla de administrador únicamente será accesible para aquellos usuarios con el rol de administrador, en la cual se incluirá una lista de aquellos usuarios que han sido reportados donde el administrador podrá ver el motivo de su reporte y si banea o no a dicho usuario.
- **Juego**: La pantalla de juego es la pantalla de multijugador donde varios jugadores podrán entrar a jugar simultáneamente, se enfrentarán entre ellos y ganará aquel que sea el último en quedar de pie.
- **Rankings**: La pantalla de rankings mostrará un top con los 100 mejores jugadores del juego o lo que es lo mismo, los jugadores que hayan ganado más veces en el modo de juego multijugador. Se mostrará el nombre del usuario junto a las victorias conseguidas y la posición que ocupa en el top.

## Documentación del proyecto

### Clases de Spring Boot

### Clases del dominio

## Desarrollo

### Herramientas

Para el desarrollo en equipo, se ha utilizado Visual Studio Code como editor. Como sistema de control de versiones, se ha utilizado Git o, más concretamente, GitHub. De GitHub también se ha utilizado la herramienta de proyectos para realizar la planificación y la distribución de tareas.

Además, se han utilizado las siguientes extensiones de VS Code:

- [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack):
    - [Language Support for Java(TM) by Red Hat](https://marketplace.visualstudio.com/items?itemName=redhat.java)
    - [Debugger for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-debug)
    - [Test Runner for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test)
    - [Maven for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-maven)
    - [Project Manager for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-dependency)
    - [IntelliCode](https://marketplace.visualstudio.com/items?itemName=VisualStudioExptTeam.vscodeintellicode)
- [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-boot-dev-pack)
    - [Spring Boot Tools](https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-spring-boot)
    - [Spring Initializr Java Support](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-initializr)
    - [Spring Boot Dashboard](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-boot-dashboard)
- [Live Share](https://marketplace.visualstudio.com/items?itemName=MS-vsliveshare.vsliveshare)
- [Markdown All in One](https://marketplace.visualstudio.com/items?itemName=yzhang.markdown-all-in-one)
- [Checkstyle for Java](https://marketplace.visualstudio.com/items?itemName=shengchen.vscode-checkstyle)
- [SonarLint](https://marketplace.visualstudio.com/items?itemName=SonarSource.sonarlint-vscode)

## Referencias, _cheat sheets_ y _shortcuts_

### Maven y ejecución

Ejecutar tests del proyecto:

```console
$ mvn test
```

- Official Apache Maven documentation: https://maven.apache.org/guides/index.html
- Spring Boot Maven Plugin Reference Guide: https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/

### Spring y componentes

- Create an OCI image: https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/#build-image
- Spring Web: https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-developing-web-applications
- WebSocket: https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-websockets
- Spring Data JPA: https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-jpa-and-spring-data
- Spring Security: https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-security

### Thymeleaf

- Tymeleaf: https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-spring-mvc-template-engines

### Guías de Spring

- Building a RESTful Web Service: https://spring.io/guides/gs/rest-service/
- Serving Web Content with Spring MVC: https://spring.io/guides/gs/serving-web-content/
- Building REST services with Spring: https://spring.io/guides/tutorials/bookmarks/
- Using WebSocket to build an interactive web application: https://spring.io/guides/gs/messaging-stomp-websocket/
- Accessing Data with JPA: https://spring.io/guides/gs/accessing-data-jpa/
- Securing a Web Application: https://spring.io/guides/gs/securing-web/
- Spring Boot and OAuth2: https://spring.io/guides/tutorials/spring-boot-oauth2/
- Authenticating a User with LDAP: https://spring.io/guides/gs/authenticating-ldap/
- Handling Form Submission: https://spring.io/guides/gs/handling-form-submission/

### Base de datos

#### H2 Console

Acceso a H2 Console desde `http://localhost:8080/h2/`.

#### Importación en el arranque

A través de un `import.sql`.

- Ant tasks for schema creation and documentation: https://developer.jboss.org/docs/DOC-14011
- Using Hibernate import.sql: http://christopherlakey.com/articles/import-sql.html
