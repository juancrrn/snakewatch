
document.addEventListener("DOMContentLoaded", () => {

    function loadGame(){
      let script = document.createElement('script');
      script.src= SCRIPTURL;
      script.type = "module";
      document.body.appendChild(script);
    }

    setTimeout(loadGame, 1500);

  })