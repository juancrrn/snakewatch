

document.addEventListener("DOMContentLoaded", () => {

    function init(){ 
        ws.subscribe("/topic/match" + MATCH); 
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