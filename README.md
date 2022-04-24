# Snakewatch Server

Snakewatch es una aplicación web dedicada al videojuego de la serpiente o _Snake_ en versión _online_ y multijugador.

Concretamente, Snakewatch Server es es la aplicación web que sirve el _front end_ y mantiene en ejecución un _back end_ para gestionar peticiones.

El proyecto está desarrollado dentro de la asignatura de Ingeniería Web del Grado en Ingeniería Informática de la Universidad Complutense de Madrid, y utiliza tecnologías como Spring, Maven y Thymeleaf.

**Índice de contenidos**

- [Snakewatch Server](#snakewatch-server)
  - [Propuesta de proyecto](#propuesta-de-proyecto)
    - [La serpiente](#la-serpiente)
    - [_Catch_](#catch)
  - [Funcionalidad](#funcionalidad)
    - [Funcionalidad de gestión de acceso, usuarios y roles](#funcionalidad-de-gestión-de-acceso-usuarios-y-roles)
    - [Funcionalidad de amistad entre usuarios](#funcionalidad-de-amistad-entre-usuarios)
    - [Funcionalidad de clasificaciones o _rankings_](#funcionalidad-de-clasificaciones-o-rankings)
    - [Funcionalidad de base de juego](#funcionalidad-de-base-de-juego)
    - [Funcionalidad de multijugador en juego](#funcionalidad-de-multijugador-en-juego)
    - [Funcionalidad de salas o _rooms_ de juego](#funcionalidad-de-salas-o-rooms-de-juego)
    - [Funcionalidad de reporte de usuarios](#funcionalidad-de-reporte-de-usuarios)
    - [Funcionalidad de niveles de juego](#funcionalidad-de-niveles-de-juego)
    - [Funcionalidad de skins de juego](#funcionalidad-de-skins-de-juego)
  - [Vistas](#vistas)
    - [Lobby](#lobby)
    - [Perfil de usuario](#perfil-de-usuario)
    - [Niveles](#niveles)
    - [Administrador](#administrador)
    - [Juego](#juego)
    - [Rankings](#rankings)
  - [Documentación del proyecto](#documentación-del-proyecto)
    - [Clases de Java de Spring Boot](#clases-de-java-de-spring-boot)
    - [Clases de Java de modelos del dominio](#clases-de-java-de-modelos-del-dominio)
    - [Clases de Java de controladores y lógica](#clases-de-java-de-controladores-y-lógica)
    - [Lógica de JavaScript en el cliente](#lógica-de-javascript-en-el-cliente)
  - [Desarrollo](#desarrollo)
    - [Dependencias del proyecto de Java](#dependencias-del-proyecto-de-java)
    - [Dependencias de la lógica de JavaScript en el cliente](#dependencias-de-la-lógica-de-javascript-en-el-cliente)
    - [Herramientas](#herramientas)
  - [Referencias, _cheat sheets_ y _shortcuts_](#referencias-cheat-sheets-y-shortcuts)
    - [Maven y ejecución](#maven-y-ejecución)
    - [Spring y componentes](#spring-y-componentes)
    - [Thymeleaf](#thymeleaf)
    - [Guías de Spring](#guías-de-spring)
    - [Base de datos](#base-de-datos)

**Autores**

- Daniel Marín Irún
- Juan Carrión Molina
- Mohamed Ghanem
- Óscar Caro Navarro
- Óscar Molano Buitrago

## Propuesta de proyecto

### La serpiente

En resumen, la dinámica del juego consiste en que el jugador controla una criatura larga y delgada, similar a una serpiente, que se mueve sobre un plano delimitado recogiendo alimentos. El jugador debe evitar que la serpiente se golpee contra su propia cola o contra las paredes que delimitan el plano.

Se pueden obtener más detalles en la [página de Wikipedia de la serpiente](https://es.wikipedia.org/wiki/La_serpiente_(videojuego)).

### _Catch_

> ¿Viciado al Elden Ring o al God of war Ragnarök? Si eres más de clásicos, ¡estás de suerte! Con Snakewatch podrás jugar al mítico y legendario Snake con tus amigos. Gracias a su entorno multijugador, la partida se sincronizará para que luchéis por ver quién es el amo del mapa. ¡Sé el último en sobrevivir y álzate con la victoria!

## Funcionalidad

### Funcionalidad de gestión de acceso, usuarios y roles

> Estado: ✅ funcionalidad implementada
> 
> ☕ Clases Java relacionadas: `User`, `SecurityConfig`, `LoginSuccessHandler`, `IwUserDetailsService`  
> 📄 Ficheros JavaScript relacionados: (...)

A partir de la base proporcionada para la asignatura, se ha construido la funcionalidad de gestión de acceso, usuarios y roles.

**Roles de usuario**

- **Usuario invitado o _guest_**:
  - Solo podrá ver las páginas públicas.
  - Con respecto a la lógica de juego, solo podrá unirse a una sesión existente y no podrá alojar la suya propia. No tendrá acceso a ninguna de las características secundarias, como equipar skins o aparecer en los rankings. No se guardará su información.
- **Usuario registrado**:
  - Poseerá un perfil propio persistente.
  - Con respecto a la lógica de juego, podrá crear una sesión, participar en las clasificaciones o _rankings_, personalizar pieles o _skins_, consultar historiales de partidas, reportar a usuarios y configurar su cuenta.
- **Usuario espectador**:
  - Pueden observar el desarrollo de una partida, ya sean invitados o registrados.
- **Administrador**:
  - Tienen la capacidad de gestionar la plataforma, ver reportes y, especialmente, de actuar ante reportes de usuarios.

> 📝 Pendiente (TODO): crear página de registro.

> 📝 Pendiente (TODO): mostrar mensajes de error en los formularios de inicio de sesión y registro.

### Funcionalidad de amistad entre usuarios

> Estado: ✅ funcionalidad implementada
> 
> ☕ Clases Java relacionadas: (...)  
> 📄 Ficheros JavaScript relacionados: (...)

### Funcionalidad de clasificaciones o _rankings_

> Estado: ✅ funcionalidad implementada
> 
> ☕ Clases Java relacionadas: (...)  
> 📄 Ficheros JavaScript relacionados: (...)

### Funcionalidad de base de juego

> Estado: ✅ funcionalidad implementada
> 
> ☕ Clases Java relacionadas: (...)  
> 📄 Ficheros JavaScript relacionados: (...)

### Funcionalidad de multijugador en juego

> Estado: 🚧 funcionalidad en proceso de implementación
> 
> ☕ Clases Java relacionadas: (...)  
> 📄 Ficheros JavaScript relacionados: (...)

### Funcionalidad de salas o _rooms_ de juego

> Estado: 🚧 funcionalidad en proceso de implementación
> 
> ☕ Clases Java relacionadas: (...)  
> 📄 Ficheros JavaScript relacionados: (...)

> 📝 Pendiente (TODO): crear nuevas salas.

> 📝 Pendiente (TODO): permitir al administrador de la sala comenzar una nueva partida.

> 📝 Pendiente (TODO): implementar estado de "espera" para las salas, anterior al juego en sí.

> 📝 Pendiente (TODO): lógica de salas privadas.

### Funcionalidad de reporte de usuarios

> Estado: 📅 funcionalidad planificada
> 
> ☕ Clases Java relacionadas: (...)  
> 📄 Ficheros JavaScript relacionados: (...)

### Funcionalidad de niveles de juego

> Estado: 📅 funcionalidad planificada
> 
> ☕ Clases Java relacionadas: (...)  
> 📄 Ficheros JavaScript relacionados: (...)

> 📝 Pendiente (TODO): añadir muros al juego.

> 📝 Pendiente (TODO): cargar el nivel correspondiente desde la pestaña de niveles.

> 📝 Pendiente (TODO): crear niveles por defecto.

> 📝 Pendiente (TODO): permitir al admin subir nuevos niveles como ficheros JSON.

### Funcionalidad de skins de juego

> Estado: 📅 funcionalidad planificada
> 
> ☕ Clases Java relacionadas: (...)  
> 📄 Ficheros JavaScript relacionados: (...)

## Vistas

> ⚠️ Sección pendiente de actualización

### Lobby

La pantalla de lobby es la pantalla de inicio del juego, donde un usuario una vez se ha registrado en la aplicación web podrá seleccionar como desee jugar, si multijugador (play online) o un jugador solo (levels), así como observar otras partidas (spectate). También podrá acceder a su perfil, a los rankings o en caso de ser administrador a la pantalla de administrador.

### Perfil de usuario

La pantalla de perfil incluirá el perfil del usuario con su respectivo nombre y foto de perfil, así como los amigos que tiene, el número total de partidas jugadas y ganadas y un historial con las partidas recientes.

### Niveles

La pantalla de niveles es la pantalla de un solo jugador, en la que el usuario podrá seleccionar el nivel al que desee jugar, donde habrá distintas dificultades por nivel, al seleccionar el nivel tendrá que superar la dificultad que incluya dicho nivel.

### Administrador

La pantalla de administrador únicamente será accesible para aquellos usuarios con el rol de administrador, en la cual se incluirá una lista de aquellos usuarios que han sido reportados donde el administrador podrá ver el motivo de su reporte y si banea o no a dicho usuario.

### Juego

La pantalla de juego es la pantalla de multijugador donde varios jugadores podrán entrar a jugar simultáneamente, se enfrentarán entre ellos y ganará aquel que sea el último en quedar de pie.

### Rankings

La pantalla de rankings mostrará un top con los 100 mejores jugadores del juego o lo que es lo mismo, los jugadores que hayan ganado más veces en el modo de juego multijugador. Se mostrará el nombre del usuario junto a las victorias conseguidas y la posición que ocupa en el top.

## Documentación del proyecto

### Clases de Java de Spring Boot

<p align="center">
    <img alt="Diagrama pseudo-UML de clases de Spring Boot" src="https://github.com/juancrrn/snakewatch/blob/main/doc/img/spring-classes-pseudo-uml.png?raw=true">
</p>
<p align="center"><sup>Diagrama pseudo-UML de clases de Java de Spring Boot</sup></p>

Este diagrama muestra, de forma resumida y con un formato parecido a UML, la funcionalidad de las clases de configuración y arranque de Spring Boot utilizadas en el proyecto.

### Clases de Java de modelos del dominio

<p align="center">
    <img alt="Diagrama pseudo-UML de clases de Java del dominio" src="https://github.com/juancrrn/snakewatch/blob/main/doc/img/domain-classes-pseudo-uml.png?raw=true">
</p>
<p align="center"><sup>Diagrama pseudo-UML de clases de Java del dominio</sup></p>

### Clases de Java de controladores y lógica

<p align="center">
    <img alt="Diagrama pseudo-UML de clases de Java de controladores y lógica" src="https://github.com/juancrrn/snakewatch/blob/main/doc/img/controller-classes-pseudo-uml.png?raw=true">
</p>
<p align="center"><sup>Diagrama pseudo-UML de clases de Java de controladores y lógica</sup></p>

Se ha seguido la plantilla de la asignatura en cuanto a implementar la lógica de servicios dentro de los controladores.

### Lógica de JavaScript en el cliente

<p align="center">
    <img alt="Diagrama pseudo-UML de la lógica de JavaScript en el cliente" src="https://github.com/juancrrn/snakewatch/blob/main/doc/img/javascript-logic-pseudo-uml.png?raw=true">
</p>
<p align="center"><sup>Diagrama pseudo-UML de la lógica de JavaScript en el cliente</sup></p>

## Desarrollo

### Dependencias del proyecto de Java

El proyecto de Java se basa en las siguientes dependencias:

- [Spring Boot](https://spring.io/projects/spring-boot)
    - [Spring Data JPA](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
    - [Spring Security](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security)
    - [Spring Thymeleaf](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf)
    - [Spring Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)
    - [Spring Websocket](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-websocket)
    - [Spring Test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test)
- [Thymeleaf](https://www.thymeleaf.org/)
    - [Thymeleaf Spring Security 5 extra](https://mvnrepository.com/artifact/org.thymeleaf.extras/thymeleaf-extras-springsecurity5)
- [Spring Security](https://spring.io/projects/spring-security)
    - [Spring Security Messaging](https://mvnrepository.com/artifact/org.springframework.security/spring-security-messaging)
    - [Spring Security Test](https://mvnrepository.com/artifact/org.springframework.security/spring-security-test)
- [Base de datos H2](https://mvnrepository.com/artifact/com.h2database/h2)
- [Anotaciones Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)
- [Karate para pruebas](https://mvnrepository.com/artifact/com.intuit.karate/karate-junit5)
- [JSON](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple)
- [Java Bean Validation API](https://mvnrepository.com/artifact/javax.validation/validation-api)

### Dependencias de la lógica de JavaScript en el cliente

La lógica de JavaScript en el cliente se basa en las siguientes dependencias:

- [Phaser](http://phaser.io/)

### Herramientas

Para el desarrollo en equipo, se ha utilizado Visual Studio Code como editor. Como sistema de control de versiones, se ha utilizado Git o, más concretamente, GitHub. De GitHub también se ha utilizado la herramienta de proyectos para realizar la planificación y la distribución de tareas.

Además, se han utilizado las siguientes extensiones de VS Code:

- [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack):
    - [Language Support for Java(TM) by Red Hat](https://marketplace.visualstudio.com/items?itemName=redhat.java) (se ha usado también para formatear el código)
    - [Debugger for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-debug)
    - [Test Runner for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test)
    - [Maven for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-maven)
    - [Project Manager for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-dependency)
    - [IntelliCode](https://marketplace.visualstudio.com/items?itemName=VisualStudioExptTeam.vscodeintellicode)
- [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-boot-dev-pack)
    - [Spring Boot Tools](https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-spring-boot)
    - [Spring Initializr Java Support](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-initializr)
    - [Spring Boot Dashboard](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-boot-dashboard)
- [Lombok Annotations Support for VS Code](https://marketplace.visualstudio.com/items?itemName=GabrielBB.vscode-lombok)
- [Checkstyle for Java](https://marketplace.visualstudio.com/items?itemName=shengchen.vscode-checkstyle)
- [SonarLint](https://marketplace.visualstudio.com/items?itemName=SonarSource.sonarlint-vscode)
- [Live Share](https://marketplace.visualstudio.com/items?itemName=MS-vsliveshare.vsliveshare)
- [Markdown All in One](https://marketplace.visualstudio.com/items?itemName=yzhang.markdown-all-in-one)

## Referencias, _cheat sheets_ y _shortcuts_

### Maven y ejecución

Ejecutar tests del proyecto:

```shell
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

**H2 Console**

Acceso a H2 Console desde `http://localhost:8080/h2/`.

**Importación en el arranque**

A través de un `import.sql`.

- Ant tasks for schema creation and documentation: https://developer.jboss.org/docs/DOC-14011
- Using Hibernate import.sql: http://christopherlakey.com/articles/import-sql.html
