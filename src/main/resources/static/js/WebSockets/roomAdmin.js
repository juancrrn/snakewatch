document.addEventListener("DOMContentLoaded", () => {

    let gamePlayersNames = [USERSESSIONAME];

    let playersOnlineNames = [];

    function init() {
        ws.subscribe(ROOMURL);
        setInterval(sendMessage, 1000);
        setInterval(actualizeList, 2000);
    }

    function sendMessage() {
        const messageUser = {
            type: "playerOnline",
            message: USERSESSIONAME
        }

        ws.stompClient.send(ROOMURL, ws.headers, JSON.stringify(messageUser));

    }

    function actualizeList() {
        let accordionBody = document.getElementById('playersList');
        accordionBody.innerHTML = '';
        for (let i = 0; i < playersOnlineNames.length; i++) {
            accordionBody.innerHTML += "<li class='list-group-item text-center bg-info bg-opacity-25'><p>" + playersOnlineNames[i] + "</p></li>";
        }
        playersOnlineNames.splice(0, playersOnlineNames.length);
    }

    let editButton = document.getElementById('editButton');
    let editForm = document.getElementById('editForm');


    editButton.onclick = () => {
        editForm.style.display = 'block';
    }

    let closeEditFormButton = document.getElementById('closeEditFormButton');


    closeEditFormButton.onclick = () => {
        editForm.style.display = 'none';
    }

    let sendInvitationsButton = document.getElementById('sendInvitationsButton');


    sendInvitationsButton.onclick = () => {
        const messageAdmin = {
            type: "matchInvitation"
        }

        ws.stompClient.send(ROOMURL, ws.headers, JSON.stringify(messageAdmin));


        let toastHTML = document.getElementById('waitingPlayersToast');
        let numberOfPlayersReady = document.getElementById('numberOfPlayersReady');
        let playersReady = document.getElementById('playersReady');
        toastHTML.style.display = '';

        gamePlayersNames = [USERSESSIONAME];

        playersReady.innerHTML = "<li class='list-group-item text-primary border-0 p-0 fs-5 bg-transparent'>" + gamePlayersNames[0] + "</li>";

        numberOfPlayersReady.innerHTML = gamePlayersNames.length + "/" + MAXROOMPLAYERS + " Players Ready";

        let toast = new bootstrap.Toast(toastHTML);
        toast.show();

    }

    let startMatchButton = document.getElementById('startMatchButton');

    startMatchButton.onclick = (e) => {

        e.preventDefault();
        go(startMatchButton.parentNode.action, 'POST', {
            message: gamePlayersNames
        })
            .then(d => e => console.log("sad", e))
            .catch(e => console.log("sad", e))

    }

    function showToast(text) {

        let toastHTML = document.getElementById('joinAndLeaveRoomToast');

        // Make it visible (remove display = "none")
        toastHTML.style.display = '';
        // Put message content on toast body
        if (text.type == 'joinRoom') {
            toastHTML.querySelector('.toast-body-text').innerHTML = text.message + " joined the game!!!!!!";
        }
        else if (text.type == 'leaveRoom') {
            toastHTML.querySelector('.toast-body-text').innerHTML = text.message + " left the game!!!!!!";
        }
        // Cast it to bootstrap toast and make it show
        let toast = new bootstrap.Toast(toastHTML);
        toast.show();

    }

    // Guardar la version previa de ws.receive para no sobreescribirla
    const oldReceive = ws.receive;

    ws.receive = (text) => {
        if (text.type == "joinRoom" || text.type == "leaveRoom") {
            showToast(text);
        }

        if (text.type == "playerOnline") {
            if (playersOnlineNames.indexOf(text.message) == -1) {
                playersOnlineNames.push(text.message);
            }
        }

        if (text.type == "acceptMatchInvitation") {

            let numberOfPlayersReady = document.getElementById('numberOfPlayersReady');
            let playersReady = document.getElementById('playersReady');

            if (gamePlayersNames.indexOf(text.message) == -1) {
                gamePlayersNames.push(text.message);
                playersReady.innerHTML += "<li class='list-group-item text-primary p-0 border-0 fs-5 bg-transparent'>" + text.message + "</li>";
                numberOfPlayersReady.innerHTML = gamePlayersNames.length + "/" + MAXROOMPLAYERS + " Players Ready";
            }
        }

        if (text.type == "goToMatch") {
            window.location.replace("/rooms/get_match/" + text.message);
        }

        // Llamar a la antigua version de receive() para no sobreescribirla
        if (oldReceive != null) oldReceive(text);
    }


    setTimeout(init, 500);
})

