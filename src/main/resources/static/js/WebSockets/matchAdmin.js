

document.addEventListener("DOMContentLoaded", () => {

    let playersNames = [USERSESSIONAME];
    

    function init(){
      
        ws.subscribe("/topic/match" + MATCH); 

        const messageStartMatch = {
            type: "startMatch",
            message: CLIENTURL
        }

        ws.stompClient.send("/topic/room" + ROOM,ws.headers, JSON.stringify(messageStartMatch));
    }


    function loadGame(){
        let scriptGame = document.createElement('script');
        scriptGame.src= SCRIPTURL;
        scriptGame.type = "module";
        document.body.appendChild(scriptGame);
    }


    setTimeout(init, 500);

    setTimeout(loadGame, 1500);


  })