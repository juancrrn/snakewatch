document.addEventListener("DOMContentLoaded", () => {

    // envio de mensajes con AJAX
    let b = document.getElementById("sendmsg");
    b.onclick = (e) => {
      e.preventDefault();
      go(b.parentNode.action, 'POST', {
        message: document.getElementById("message").value
      })
        .then(d => console.log("happy", d))
        .catch(e => console.log("sad", e))
        document.getElementById("message").value = ""
    }

    let frLoadingMessage = document.getElementById("friendship-loading");
    let frSendButton = document.getElementById("friendship-send-button");
    let frCancelButton = document.getElementById("friendship-request-cancel-button");
    let frAcceptButton = document.getElementById("friendship-request-accept-button");
    let frFinishButton = document.getElementById("friendship-finish-button");

    function hideAll() {
      frLoadingMessage.style.display = 'none';
      frSendButton.style.display = 'none';
      frCancelButton.style.display = 'none';
      frAcceptButton.style.display = 'none';
      frFinishButton.style.display = 'none';
    }

    // Get friendship status and show corresponding button
    go(G_FRIENDSHIP_STATE_URL, 'POST', {})
      .then(d => {
        hideAll();

        if (d.result === "friends") {
          frFinishButton.style.display = 'inline-block';
        }
        else if (d.result === "request sent") {
          frCancelButton.style.display = 'inline-block';
        }
        else if (d.result === "request received") {
          frAcceptButton.style.display = 'inline-block';
        }
        else {
          frSendButton.style.display = 'inline-block';
        }
      })
      .catch(e => {
        hideAll();
        console.log("Error loading friendship status", e)
      })


    // Accion de frSendButton: envio de solicitudes de amistad
    frSendButton.onclick = (e) => {
      e.preventDefault();
      go(frSendButton.parentNode.action, 'POST', {})
        .then(d => {
          hideAll();
          frCancelButton.style.display = 'inline-block';
        })
        .catch(e => console.log("sad", e))
    }

    // Accion de frCancelButton: cancelar solicitud de amistad enviada
    frCancelButton.onclick = (e) => {
      e.preventDefault();
      go(frCancelButton.parentNode.action, 'POST', {})
        .then(d => {
          hideAll();
          frSendButton.style.display = 'inline-block';
        })
        .catch(e => console.log("sad", e))
    }

    // Accion de frAcceptButton: aceptar solicitud de amistad recibida
    frAcceptButton.onclick = (e) => {
      e.preventDefault();
      go(frAcceptButton.parentNode.action, 'POST', {})
        .then(d => {
          hideAll();
          frFinishButton.style.display = 'inline-block';
        })
        .catch(e => console.log("sad", e))
    }

    // Accion de frFinishButton: eliminar amigo
    frFinishButton.onclick = (e) => {
      e.preventDefault();
      go(frFinishButton.parentNode.action, 'POST', {})
        .then(d => {
          hideAll();
          frSendButton.style.display = 'inline-block';
        })
        .catch(e => console.log("sad", e))
    }

  });
