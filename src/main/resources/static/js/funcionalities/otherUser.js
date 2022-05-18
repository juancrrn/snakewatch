const chatBox = document.getElementById("chat-box");

const frLoadingMessage = document.getElementById("friendship-loading");
const frSendButton = document.getElementById("fr-send");
const frCancelButton = document.getElementById("fr-cancel");
const frAcceptButton = document.getElementById("fr-accept");
const frFinishButton = document.getElementById("fr-finish");

function sendMsg() {
  go("/user/" + USER_ID + "/msg", 'POST', { message: chatBox.value }).then((d) => {
    // Do nothing
  }).catch((e) => {
    console.log("Error: ", e);
  })
  chatBox.value = '';
}

function hideElement(element) {
  element.style.display = 'none';
}

function unhideElement(element) {
  element.style.display = '';
}

function updateFriendship(id, action) {
  go("/friendship/" + action + "/" + id + "/", "POST").then((d) => {

    switch (action) {
      case "send_req":
        hideElement(frSendButton);
        unhideElement(frCancelButton);
        break;
      case "cancel_req":
        hideElement(frCancelButton);
        unhideElement(frSendButton);
        break;
      case "accept_req":
        hideElement(frAcceptButton);
        unhideElement(frFinishButton);
        break;
      case "finish":
        hideElement(frFinishButton);
        unhideElement(frSendButton);
        break;

      default:
        break;
    }
  }).catch((e) => {
    console.log("Error: ", e);
  })
}

function report() {
  go("/user/" + USER_ID + "/report", 'POST', { message: reportmessage.value }).then((d) => {
    document.getElementById('closeModal').click();
  }).catch((e) => {
    console.log("Error: ", e);
  })
  chatBox.value = '';
}

document.addEventListener("DOMContentLoaded", () => {
    // Get the modal
    var modal = document.getElementById("myModal");

    // Get the button that opens the modal
    var btn = document.getElementById("report-btn");
  
    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    // When the user clicks on the button, open the modal
    btn.onclick = function() {
      modal.style.display = "block";
    }
  
    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
      modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
      if (event.target == modal) {
        modal.style.display = "none";
      }
    }
})

// Get friendship status and show corresponding button
document.addEventListener("DOMContentLoaded", () => {
  go("/friendship/state/" + USER_ID, 'GET').then((d) => {
      hideElement(frLoadingMessage);

      switch (d.result) {
        case "friends":
          unhideElement(frFinishButton);
          break;
        case "request sent":
          unhideElement(frCancelButton);
          break;
        case "request received":
          unhideElement(frAcceptButton);
          break;
        case "not friends":
          unhideElement(frSendButton);
          break;

        default:
          break;
      }
    }).catch((e) => {
      hideElement(frLoadingMessage);
      console.log("Error loading friendship status. Error: ", e)
  })
})
