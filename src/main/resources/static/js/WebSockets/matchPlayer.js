
document.addEventListener("DOMContentLoaded", () => {

    function init(){
        ws.subscribe("/topic/match" + MATCH);

        const messageIsPlayer = {
          type: "imPlayer",
          message: USERSESSIONAME
        }

        ws.stompClient.send("/topic/match" + MATCH, ws.headers, JSON.stringify(messageIsPlayer));
    }

    function loadGame(){
      let script = document.createElement('script');
      script.src= SCRIPTURL;
      script.type = "module";
      document.body.appendChild(script);
    }

    setTimeout(init, 500);

    setTimeout(loadGame, 1500);

    ws.receive = (text) => {
      if(text.type=="GameState"){
        alert("HELLOOO");
      }
    }

  })