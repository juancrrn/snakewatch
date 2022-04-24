
document.addEventListener("DOMContentLoaded", () => {

    let playersOnlineNames = [];

    let imMatchPlayer = false;

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
        let accordionBody = document.getElementById('playersList');
        accordionBody.innerHTML = '';
        for(let i=0;i<playersOnlineNames.length;i++){
            accordionBody.innerHTML+= "<li class='list-group-item text-center bg-info bg-opacity-25'><p>" + playersOnlineNames[i] + "</p></li>";
        }
        playersOnlineNames.splice(0, playersOnlineNames.length);
    }
    
    
    
    function showToast(text){
    
        let toastHTML = document.getElementById('joinAndLeaveRoomToast');
    
        // Make it visible (remove display = "none")
        toastHTML.style.display = '';
        // Put message content on toast body
        if(text.type=='joinRoom'){
            toastHTML.querySelector('.toast-body-text').innerHTML = text.message + " joined the game!!!!!!";
        }
        else if(text.type=='leaveRoom'){
            toastHTML.querySelector('.toast-body-text').innerHTML = text.message + " left the game!!!!!!";
        }
        // Cast it to bootstrap toast and make it show
        let toast = new bootstrap.Toast(toastHTML);
        toast.show();
    
    }


    let acceptMatchInvitationButton = document.getElementById('acceptMatchInvitationButton');
    let rejectMatchInvitationButton = document.getElementById('rejectMatchInvitationButton');
    let acceptAndRejectMatchToast = document.getElementById('acceptAndRejectMatchToast');
   
    acceptMatchInvitationButton.onclick = () => {
        acceptAndRejectMatchToast.style.display = 'none';

        const messageAcceptMatch = {
            type: "acceptMatchInvitation",
            message: USERSESSIONAME
        }

        ws.stompClient.send(ROOMURL, ws.headers, JSON.stringify(messageAcceptMatch));

        let toastHTML = document.getElementById('waitingAdminToast');

        toastHTML.style.display = '';

        let toast = new bootstrap.Toast(toastHTML);
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
            let toastHTML = document.getElementById('acceptAndRejectMatchToast');
            toastHTML.style.display = 'block';
            let toast = new bootstrap.Toast(toastHTML);
            toast.show();                  
        }
    
        if(text.type=="playersNames"){
            if(text.message.indexOf(USERSESSIONAME)!=-1){
                imMatchPlayer = true;
            }
        }
        if(text.type=="goToMatch"){   
            if(imMatchPlayer){
                window.location.replace("/rooms/get_match/" + text.message + "/" + ROOM);
            }         
        }
    
    }
       
    setTimeout(init, 500);
})

