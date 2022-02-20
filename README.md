# Snakewatch

Snakewatch es una aplicación web dedicada al videojuego de la serpiente o _Snake_ en versión _online_ y multijugador.

El proyecto está desarrollado dentro de la asignatura de Ingeniería Web del Grado en Ingeniería Informática de la Universidad Complutense de Madrid, y utiliza tecnologías como Spring, Maven y Thymeleaf.

## Autores

- Daniel Marín Irún
- Juan Carrión Molina
- Mohamed Ghanem
- Óscar Caro Navarro
- Óscar Molano Buitrago

## La serpiente

En resumen, la dinámica del juego consiste en que el jugador controla una criatura larga y delgada, similar a una serpiente, que se mueve sobre un plano delimitado recogiendo alimentos. El jugador debe evitar que la serpiente se golpee contra su propia cola o contra las paredes que delimitan el plano.

Se pueden obtener más detalles en la [página de Wikipedia de la serpiente](https://es.wikipedia.org/wiki/La_serpiente_(videojuego)).

## Propuesta de proyecto

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