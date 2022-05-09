document.addEventListener("DOMContentLoaded", () => {

    function findOngoingMatches(){
        go("/spectate/get_ongoing_matches" , 'GET', {})
        .then(d => {
            let matchContainer = document.getElementById('matchcontainer');

            let matchContainerDivs = matchContainer.querySelectorAll('div');

            matchContainerDivs.forEach( m => {
                if(m.id!='divemptymatches'){
                    m.remove();
                }
            })

            let emptymatchesdiv = document.getElementById("divemptymatches");
            emptymatchesdiv.style.display = "block";
            if(emptymatchesdiv.style.display != "none" && d.arrayMatches.length > 0){
                emptymatchesdiv.style.display = "none";
            }
            for(let i = 0; i < d.arrayMatches.length; i++){
                if(!document.getElementById("match"+d.arrayMatches[i][0])){
            
                    let matchDiv = document.createElement("div");
                    let rowDiv = document.createElement('div');
                    let firstColDiv = document.createElement('div');
                    let secondColDiv = document.createElement('div');

                    matchDiv.setAttribute("class", "card col-8 mb-3 shadow bg-info bg-gradient bg-opacity-10 px-0 mx-0");
                    rowDiv.setAttribute("class", "row g-0 justify-content-between");                  
                    firstColDiv.setAttribute("class", "col-md-4 bg-info bg-gradient bg-opacity-75");
                    secondColDiv.setAttribute("class", "col-md-6" );

                    let titleMatchDiv = document.createElement('h2');
                    titleMatchDiv.setAttribute("class", "text-center text-white fst-italic");
                    titleMatchDiv.innerHTML = "Match " + d.arrayMatches[i][0];
                    

                    let cardBody = document.createElement('div');
                    cardBody.setAttribute("class", "card-body w-100");

                    let cardBodyTitle = document.createElement('h5');
                    cardBodyTitle.setAttribute("class", "card-title text-center text-danger fs-4");
                    cardBodyTitle.innerHTML = "Room " + d.arrayMatches[i][1];

                    let form = document.createElement('form');
                    form.setAttribute("class", "card-text text-center fs-4");
                    form.setAttribute("action", "/rooms/get_match/"+ d.arrayMatches[i][0]);
                    form.setAttribute("method", "GET");
                    
                    let btn = document.createElement('button');
                    btn.setAttribute("class", "h-100 w-100 btn btn-outline-info fs-5");
                    btn.setAttribute("type", "submit");
                    btn.innerHTML = "Spectate";
                    matchDiv.id = "match" + d.arrayMatches[i][0];
                    
                    firstColDiv.appendChild(titleMatchDiv);
                    secondColDiv.appendChild(cardBodyTitle);
                    form.appendChild(btn);
                    cardBody.appendChild(cardBodyTitle);
                    cardBody.appendChild(form);
                    secondColDiv.appendChild(cardBody);
                    rowDiv.appendChild(firstColDiv);
                    rowDiv.appendChild(secondColDiv);          
                    matchDiv.appendChild(rowDiv);
                    matchContainer.appendChild(matchDiv);
                }
            }
        })
        .catch(e => console.log("sad", e))
    }

    setInterval(findOngoingMatches, 1000);
})