let rankingTitle = document.getElementById('rankingTitle');

let globalTable = document.getElementById("globalTable");

let mensualTable = document.getElementById("mensualTable");

let semanalTable = document.getElementById("semanalTable");

let globalRanking = document.getElementById('globalRanking');

let monthlyRanking = document.getElementById('monthlyRanking');

let weeklyRanking = document.getElementById('weeklyRanking');

rankingTitle.innerHTML = "Global Ranking";
globalTable.style.display = "table";
mensualTable.style.display = "none";
semanalTable.style.display = "none";


globalRanking.onclick = () => {
    rankingTitle.innerHTML = "Global Ranking";
    globalTable.style.display = "table";
    mensualTable.style.display = "none";
    semanalTable.style.display = "none";
}

monthlyRanking.onclick = () => {
    rankingTitle.innerHTML = "Monthly Ranking";
    globalTable.style.display = "none";
    mensualTable.style.display = "table";
    semanalTable.style.display = "none";
}

weeklyRanking.onclick = () => {
    rankingTitle.innerHTML = "Weekly Ranking";
    globalTable.style.display = "none";
    mensualTable.style.display = "none";
    semanalTable.style.display = "table";
}
