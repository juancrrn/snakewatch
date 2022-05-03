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

    let roomInvitationsButton = document.getElementById('roomInvitationsButton');


    roomInvitationsButton.onclick = (e) => {
        e.preventDefault();
        go("/rooms/get_room_invitations", 'GET', {})
        .then(d => {
            let roomInvitationsModalBody = document.getElementById('roomInvitationsModalBody');
            roomInvitationsModalBody.innerHTML = '';
            for(let i=0; i < d.roomInvitations.length; i++){
                let li = document.createElement('li');

                li.setAttribute("class", "text-center justify-content-around border-bottom border-2");

                let div = document.createElement('div');

                div.setAttribute("class", "row justify-content-around my-2")
                let p = document.createElement('p');

                p.setAttribute("class", "col-12 text-center fs-4");
                p.innerHTML = d.roomInvitations[i][1] + " invites you to Room " + d.roomInvitations[i][0] + "!!!! Do you want to Join?";

                let acceptButton = document.createElement('button');
                let rejectButton = document.createElement('button');
                acceptButton.setAttribute("class", "col-4 btn btn-outline-success text-center fs-5");
                rejectButton.setAttribute("class", "col-4 btn btn-outline-danger text-center fs-5");

                acceptButton.innerHTML = "Accept";
                rejectButton.innerHTML = "Reject";

                div.appendChild(p);
                div.appendChild(rejectButton);
                div.appendChild(acceptButton);
                li.appendChild(div);
                roomInvitationsModalBody.appendChild(li);
            }
        })
        .catch(e => console.log("sad", e))

    }
  });