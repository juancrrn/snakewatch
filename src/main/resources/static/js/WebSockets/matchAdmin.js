document.addEventListener("DOMContentLoaded", () => {

    function init(){
      
        ws.subscribe("/topic/match" + MATCH); 

        const messageStartMatch = {
            type: "startMatch",
            message: CLIENTURL
        }

        ws.stompClient.send("/topic/room" + ROOM,ws.headers, JSON.stringify(messageStartMatch));
    }

    function loadGame(){
        let script = document.createElement('script');
        script.src= SCRIPTURL;
        script.type = "module";
        document.body.appendChild(script);
    }

    setTimeout(init, 500);

    setTimeout(loadGame, 1000);

  })