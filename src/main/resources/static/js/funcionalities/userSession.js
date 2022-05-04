document.addEventListener("DOMContentLoaded", () => {
    // Manejar la seccion de solicitudes recibidas: aceptar o rechazar cada solicitud

    const requestContainer = document.getElementById("request-block");
    var requestRows = document.getElementsByClassName("request-row");       // Una row para cada solicitud

    if(requestRows.length <= 0){
      requestContainer.style.display = 'none';
    }

    for (let i = 0; i < requestRows.length; i++) {
      let row = requestRows[i];

      // Boton de aceptar la solicitud: aceptarla a traves de ayax y ocultar esta row
      let acceptRequestButton = row.querySelector('#request-accept-button');
      acceptRequestButton.onclick = (e) => {
        e.preventDefault();
        go(acceptRequestButton.parentNode.action, 'POST', {})
          .then(d => {
            row.remove();
            // Ocultar row. Si ya no queda ninguna row mas, ocultar la seccion de solicitudes entera
            if (requestRows.length <= 0) {
              requestContainer.style.display = 'none';
            }
          })
          .catch(e => console.log("sad", e))
      }

      // Boton de rechazar la solicitud: rechazarla a traves de ayax y ocultar esta row
      let rejectRequestButton = row.querySelector('#request-reject-button');
      rejectRequestButton.onclick = (e) => {
        e.preventDefault();
        go(rejectRequestButton.parentNode.action, 'POST', {})
          .then(d => {
            row.style.display = 'none';
            // Ocultar row. Si ya no queda ninguna row mas, ocultar la seccion de solicitudes entera
            if (requestRows.length <= 0) {
              requestContainer.style.display = 'none';
            }
          })
          .catch(e => console.log("sad", e))
      }
    }

  });