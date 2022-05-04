document.addEventListener("DOMContentLoaded", () => {

    let gamePlayersNames = [USERSESSIONAME];


    let playersOnlineNames = [];

    function init() {
        ws.subscribe(ROOMURL, callback);
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

    if(ROOMVISIBILITY == 'PRIVATE'){
        let inviteFriendsButton = document.getElementById('inviteFriendsButton');

        inviteFriendsButton.onclick = () => {
            
            function roomGo(){
                go("/rooms/get_user_friends", 'GET', {})
            .then(d => {

                let userFriendsModalBody = document.getElementById('userFriendsModalBody');
                
                userFriendsModalBody.innerHTML = '';
                let friendsP = document.createElement('p');
                friendsP.innerHTML = "Friends";
                friendsP.setAttribute("class", "col-12 text-center fs-3 fw-bold");
                userFriendsModalBody.appendChild(friendsP);
                for(let i=0; i< d.userFriends.length;i++){
                    let li = document.createElement('li');

                    li.setAttribute("class", "border-bottom border-top border-3 my-3 py-2")
                    let div = document.createElement('div');

                    div.setAttribute("class", "row justify-content-around");

                    let p = document.createElement('p');

                    p.innerHTML = d.userFriends[i];
                    p.setAttribute("class", "col-8 fs-4 text-center");

                    let b = document.createElement('button');

                    b.setAttribute("class", "col-4 btn btn-outline-success fs-4");
                    b.innerHTML = "Invite ";
                    b.style.borderRadius = "25px";
    
                   
                    let svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
                    let svgPath1 = document.createElementNS('http://www.w3.org/2000/svg', 'path');
                    let svgPath2 = document.createElementNS('http://www.w3.org/2000/svg', 'path');

                    svg.setAttribute("width", "30");
                    svg.setAttribute("height", "30");
                    svg.setAttribute("fill", "currentColor");
                    svg.setAttribute("class", "bi bi-send-plus-fill");
                    svg.setAttribute("viewBox", "0 0 16 16");

                    svgPath1.setAttribute("d", "M15.964.686a.5.5 0 0 0-.65-.65L.767 5.855H.766l-.452.18a.5.5 0 0 0-.082.887l.41.26.001.002 4.995 3.178 1.59 2.498C8 14 8 13 8 12.5a4.5 4.5 0 0 1 5.026-4.47L15.964.686Zm-1.833 1.89L6.637 10.07l-.215-.338a.5.5 0 0 0-.154-.154l-.338-.215 7.494-7.494 1.178-.471-.47 1.178Z");
                    svgPath2.setAttribute("d", "M16 12.5a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Zm-3.5-2a.5.5 0 0 0-.5.5v1h-1a.5.5 0 0 0 0 1h1v1a.5.5 0 0 0 1 0v-1h1a.5.5 0 0 0 0-1h-1v-1a.5.5 0 0 0-.5-.5Z");

                    svg.appendChild(svgPath1);
                    svg.appendChild(svgPath2);
                    b.appendChild(svg);

                    b.onclick = () => {
                        const messageToJoinPrivateRoom = {
                            type: "PrivateRoomInvitation",
                            text: {
                                from: USERSESSIONAME,
                                message: ROOM
                            }
                        }

                        ws.stompClient.send("/user/" + d.userFriends[i] + "/queue/updates", ws.headers, JSON.stringify(messageToJoinPrivateRoom));

                    }
                    
                    div.appendChild(p);
                    div.appendChild(b);
                    li.appendChild(div);
                    userFriendsModalBody.appendChild(li);
                }
            })
            .catch(e => console.log("Error", e))
            }     

            setInterval(roomGo, 3000);
        }
    }   

    let inviteGameButton = document.getElementById('inviteGameButton');


    inviteGameButton.onclick = () => {
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
                .then(d  => console.log("happy", d))
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

    function callback(text) {
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
                if(gamePlayersNames.length>1){
                    startMatchButton.style.display = 'block';
                }
            }
        }

        if (text.type == "goToMatch") {
            window.location.replace("/rooms/get_match/" + text.message);
        }
    }


    setTimeout(init, 500);
})

