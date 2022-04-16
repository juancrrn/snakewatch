

var myGamePiece;
var myObstacles = [];
var myScore;


const cellSize = 20;
const mapSize = 500;
var numRows;
var numCols;

var snake;      // player snake
var botSnakes;  // array with all botSnakes
var food;


/** 
 * Called when the html has finished been loaded
 */
function startGame() {
    numRows = 500 / cellSize;
    numCols = 500 / cellSize;

    snake = new Snake();
    food = new Food();
    botSnakes = [];
    botSnakes[0] = new BotSnake();
    botSnakes[1] = new BotSnake();
    botSnakes[2] = new BotSnake();
    botSnakes[3] = new BotSnake();
    botSnakes[4] = new BotSnake();

    //myGamePiece = new component(30, 30, "red", 10, 120);
    //myGamePiece.gravity = 0.05;
    //myScore = new component("30px", "Consolas", "black", 280, 40, "text");
    myGameArea.start();
}

var myGameArea = {
    canvas: document.getElementById("canvas"),
    start: function () {
        this.canvas.width = mapSize;
        this.canvas.height = mapSize;

        this.context = this.canvas.getContext("2d");
        this.frameNo = 0;
        this.interval = setInterval(updateGameArea, 100);
    },
    clear: function () {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
}

function component(width, height, color, x, y, type) {
    this.type = type;
    this.score = 0;
    this.width = width;
    this.height = height;
    this.speedX = 0;
    this.speedY = 0;
    this.x = x;
    this.y = y;
    this.gravity = 0;
    this.gravitySpeed = 0;
    this.update = function () {
        ctx = myGameArea.context;
        if (this.type == "text") {
            ctx.font = this.width + " " + this.height;
            ctx.fillStyle = color;
            ctx.fillText(this.text, this.x, this.y);
        } else {
            ctx.fillStyle = color;
            ctx.fillRect(this.x, this.y, this.width, this.height);
        }
    }
    this.newPos = function () {
        this.gravitySpeed += this.gravity;
        this.x += this.speedX;
        this.y += this.speedY + this.gravitySpeed;
        this.hitBottom();
    }
    this.hitBottom = function () {
        var rockbottom = myGameArea.canvas.height - this.height;
        if (this.y > rockbottom) {
            this.y = rockbottom;
            this.gravitySpeed = 0;
        }
    }
    this.crashWith = function (otherobj) {
        var myleft = this.x;
        var myright = this.x + (this.width);
        var mytop = this.y;
        var mybottom = this.y + (this.height);
        var otherleft = otherobj.x;
        var otherright = otherobj.x + (otherobj.width);
        var othertop = otherobj.y;
        var otherbottom = otherobj.y + (otherobj.height);
        var crash = true;
        if ((mybottom < othertop) || (mytop > otherbottom) || (myright < otherleft) || (myleft > otherright)) {
            crash = false;
        }
        return crash;
    }
}

/**
 * Called periodically with setInterval(...)
 */
function updateGameArea() {
    // Update positions and status of all snakes
    botSnakes.forEach(botSnake => botSnake.update());
    snake.update();

    myGameArea.clear();
    myGameArea.frameNo += 1;

    // Paint new cycle on screen
    snake.draw(myGameArea.context);
    botSnakes.forEach(botSnake => botSnake.draw(myGameArea.context));
    food.draw(myGameArea.context);


    /*
    var x, height, gap, minHeight, maxHeight, minGap, maxGap;
    for (i = 0; i < myObstacles.length; i += 1) {
        if (myGamePiece.crashWith(myObstacles[i])) {
            return;
        }
    }
    myGameArea.clear();
    myGameArea.frameNo += 1;
    if (myGameArea.frameNo == 1 || everyinterval(150)) {
        x = myGameArea.canvas.width;
        minHeight = 20;
        maxHeight = 200;
        height = Math.floor(Math.random() * (maxHeight - minHeight + 1) + minHeight);
        minGap = 50;
        maxGap = 200;
        gap = Math.floor(Math.random() * (maxGap - minGap + 1) + minGap);
        myObstacles.push(new component(10, height, "green", x, 0));
        myObstacles.push(new component(10, x - height - gap, "green", x, height + gap));
    }
    for (i = 0; i < myObstacles.length; i += 1) {
        myObstacles[i].x += -1;
        myObstacles[i].update();
    }
    myScore.text = "SCORE: " + myGameArea.frameNo;
    myScore.update();
    myGamePiece.newPos();
    myGamePiece.update();
    */
}

function everyinterval(n) {
    if ((myGameArea.frameNo / n) % 1 == 0) { return true; }
    return false;
}

function accelerate(n) {
    myGamePiece.gravity = n;
}

/**
 * @returns número aleatorio entre min (incluido) y max (excluido)
 */
function getRandom(min, max) {
    return Math.random() * (max - min) + min;
}

/**
 * @returns distance between the points (x1,y1) and (x2,y2)
 */
function dist(x1, y1, x2, y2) {
    return Math.sqrt((Math.pow((x1 - x2), 2)) + (Math.pow((y1 - y2), 2)))
}