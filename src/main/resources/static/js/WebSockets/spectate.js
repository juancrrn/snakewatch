document.addEventListener("DOMContentLoaded", () => {

    function findOngoingMatches(){
        go("/spectate/get_ongoing_matches" , 'GET', {})
        .then(d => {
            let emptymatchesdiv = document.getElementById("divemptymatches");
            emptymatchesdiv.style.display = "block";
            if(emptymatchesdiv.style.display != "none" && d.arrayMatches.length > 0){
                emptymatchesdiv.style.display = "none";
            }
            for(let i = 0; i < d.arrayMatches.length; i++){
                if(!document.getElementById("match"+d.arrayMatches[i][0])){
                    let divmatch = document.getElementById("matchdiv");
                    let div = document.createElement("div");

                    let divmatchtitle = document.getElementById("matchdivtitle");
                    let divmatchroom = document.getElementById("matchdivroom");
                    divmatchtitle.innerHTML = "Match " + d.arrayMatches[i][0];
                    divmatchroom.innerHTML = "Room " + d.arrayMatches[i][1];

                    let matchcontainer = document.getElementById("matchcontainer");
                    let spectatebtn = document.getElementById("spectatematch");
                    spectatebtn.setAttribute("action", "/rooms/get_match/"+ d.arrayMatches[i][0]);
                    spectatebtn.setAttribute("method", "GET");

                    div.setAttribute("class", "card col-8 mb-3 shadow bg-info bg-gradient bg-opacity-10 px-0 mx-0");

                    div.innerHTML = divmatch.innerHTML;
                    div.style.display="block";
                    div.id = "match" + d.arrayMatches[i][0];
                    
                    
                    // "/rooms/get_match/"+ d.arrayMatches[i][0] )
                    matchcontainer.appendChild(div);
                }
            }
        })
        .catch(e => console.log("sad", e))
    }

    setInterval(findOngoingMatches, 1000);
})