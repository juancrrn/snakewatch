document.addEventListener("DOMContentLoaded", () => {
    
    var gamePlayersNames = [ADMINAME];


    var playersOnlineNames = [];

    function init(){
        ws.subscribe(ROOMURL);
        setInterval(sendMessage, 1000);
        setInterval(actualizeList, 2000);
    }
    
    function sendMessage(){
        const messageUser = {
            type: "playerOnline",
            message: USERSESSIONAME
        }
    
        ws.stompClient.send(ROOMURL, ws.headers, JSON.stringify(messageUser));        
        
    }
    
    function actualizeList(){
        var accordionBody = document.getElementById('playersList');
        accordionBody.innerHTML = '';
        for(var i=0;i<playersOnlineNames.length;i++){
            accordionBody.innerHTML+= "<li class='list-group-item text-center bg-info bg-opacity-25'><p>" + playersOnlineNames[i] + "</p></li>";
        }
        playersOnlineNames.splice(0, playersOnlineNames.length);
    }
    
    var editButton = document.getElementById('editButton');
    var editForm = document.getElementById('editForm');

    
    editButton.onclick = () => {          
        editForm.style.display = 'block';        
    }

    var closeEditFormButton = document.getElementById('closeEditFormButton');

   
    closeEditFormButton.onclick = () => {
        editForm.style.display = 'none';
    }

    var sendInvitationsButton = document.getElementById('sendInvitationsButton');

  
    sendInvitationsButton.onclick = () => {
        const messageAdmin = {
            type: "matchInvitation"
        }

        ws.stompClient.send(ROOMURL, ws.headers, JSON.stringify(messageAdmin));


        var toastHTML = document.getElementById('waitingPlayersToast');
        var numberOfPlayersReady = document.getElementById('numberOfPlayersReady');
        var playersReady = document.getElementById('playersReady');
        toastHTML.style.display = '';

        gamePlayersNames = [ADMINAME];
        
        playersReady.innerHTML = "<li class='list-group-item text-primary border-0 p-0 fs-5 bg-transparent'>" + gamePlayersNames[0] + "</li>";
        
        numberOfPlayersReady.innerHTML = gamePlayersNames.length + "/" +  MAXROOMPLAYERS   + " Players Ready";

        var toast = new bootstrap.Toast(toastHTML);
        toast.show(); 

    }
    
    var startMatchButton = document.getElementById('startMatchButton');

    startMatchButton.onclick = () => {
        const messageHaveYouAcceptInvitation = {
            type: "playersNames",
            message: gamePlayersNames
        }

        ws.stompClient.send(ROOMURL, ws.headers, JSON.stringify(messageHaveYouAcceptInvitation));
    }
    
    ws.receive = (text) => {
        if(text.type=="joinRoom" || text.type=="leaveRoom"){
            showToast(text);
        }
    
        if(text.type=="playerOnline"){
            if(playersOnlineNames.indexOf(text.message)==-1){
                playersOnlineNames.push(text.message);
            }
        }
        
        if(text.type=="acceptMatchInvitation"){
            
            var numberOfPlayersReady = document.getElementById('numberOfPlayersReady');
            var playersReady = document.getElementById('playersReady');

            if(gamePlayersNames.indexOf(text.message)==-1){
                gamePlayersNames.push(text.message);
                playersReady.innerHTML += "<li class='list-group-item text-primary p-0 border-0 fs-5 bg-transparent'>" + text.message + "</li>";
                numberOfPlayersReady.innerHTML = gamePlayersNames.length + "/" +  MAXROOMPLAYERS   + " Players Ready";
            }

        }
      
    }


    setTimeout(init, 500);
})

