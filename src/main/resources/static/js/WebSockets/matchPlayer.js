
document.addEventListener("DOMContentLoaded", () => {

    function init(){
        ws.subscribe("/topic/match" + MATCH);
    }

    function loadGame(){
      let script = document.createElement('script');
      script.src= SCRIPTURL;
      script.type = "module";
      document.body.appendChild(script);
    }

    setTimeout(init, 500);

    setTimeout(loadGame, 1000);

    ws.receive = (json) => {
      // TODO: importFromJson(json) para pintar el estado del juego
    }

  })