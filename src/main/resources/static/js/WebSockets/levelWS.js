
document.addEventListener("DOMContentLoaded", () => {
    function showToast() {

        let toastHTML = document.getElementById('levelFinishedToast');
        // Put message content on toast body
        toastHTML.style.display = '';
        // Cast it to bootstrap toast and make it show
        let toast = new bootstrap.Toast(toastHTML);
        toast.show();
    
    }
    
    function callback(text) {
        if(text.type == "finishLevelGame"){
            showToast();
        }
    }
    function init() {
        ws.subscribe("/topic/level/" + USERSESSIONAME, callback);
    }
    setTimeout(init, 500);

})

