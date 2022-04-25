

document.addEventListener("DOMContentLoaded", () => {

    function loadGame(){
        let scriptGame = document.createElement('script');
        scriptGame.src= SCRIPTURL;
        scriptGame.type = "module";
        document.body.appendChild(scriptGame);
    }

    setTimeout(loadGame, 1500);


  })