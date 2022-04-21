
document.addEventListener("DOMContentLoaded", () => {

    var playersOnlineNames = [];

    var imMatchParticipant = false;

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
    
    
    
    function showToast(text){
    
        var toastHTML = document.getElementById('joinAndLeaveRoomToast');
    
        // Make it visible (remove display = "none")
        toastHTML.style.display = '';
        // Put message content on toast body
        if(text.type=='join'){
            toastHTML.querySelector('.toast-body-text').innerHTML = text.userJoiner + " joined the game!!!!!!";
        }
        else if(text.type=='leave'){
            toastHTML.querySelector('.toast-body-text').innerHTML = text.userLeaver + " left the game!!!!!!";
        }
        // Cast it to bootstrap toast and make it show
        var toast = new bootstrap.Toast(toastHTML);
        toast.show();
    
    }


    var acceptMatchInvitationButton = document.getElementById('acceptMatchInvitationButton');
    var rejectMatchInvitationButton = document.getElementById('rejectMatchInvitationButton');
    var acceptAndRejectMatchToast = document.getElementById('acceptAndRejectMatchToast');
   
    acceptMatchInvitationButton.onclick = () => {
        acceptAndRejectMatchToast.style.display = 'none';

        const messageAcceptMatch = {
            type: "acceptMatchInvitation",
            message: USERSESSIONAME
        }

        ws.stompClient.send(ROOMURL, ws.headers, JSON.stringify(messageAcceptMatch));

        var toastHTML = document.getElementById('waitingAdminToast');

        toastHTML.style.display = '';

        var toast = new bootstrap.Toast(toastHTML);
        toast.show(); 
    }
    


    rejectMatchInvitationButton.onclick = () => {
        acceptAndRejectMatchToast.style.display = 'none';
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
        if(text.type=="matchInvitation"){          
            var toastHTML = document.getElementById('acceptAndRejectMatchToast');
            toastHTML.style.display = 'block';
            var toast = new bootstrap.Toast(toastHTML);
            toast.show();                  
        }
    
        if(text.type=="playersNames"){
            if(text.message.indexOf(USERSESSIONAME)!=-1){
                imMatchParticipant = true;
            }
        }
    
        if(text.type=="startMatch"){   
            if(imMatchParticipant){
                window.location.replace(text.message);
            }         
        }
    
    }
       
    setTimeout(init, 500);
})

