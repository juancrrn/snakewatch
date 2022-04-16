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

    myGameArea.start();
}

/** 
 * Handles initialization, refresh and restart of canvas
 */
var myGameArea = {
    canvas: document.getElementById("canvas"),
    start: function () {
        this.canvas.width = mapSize;
        this.canvas.height = mapSize;

        this.context = this.canvas.getContext("2d");
        this.interval = setInterval(updateGameArea, 100);
    },
    clear: function () {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
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
}

function accelerate(n) {
    myGamePiece.gravity = n;
}

/**
 * Arrow keys pressed -> update dir of player snake
 */
document.onkeydown = function(e) {
    switch (e.key) {
        case "ArrowLeft":
            snake.newDir(-1, 0);
            break;
        case "ArrowRight":
            snake.newDir(1, 0);
            break;
        case "ArrowUp":
            snake.newDir(0, -1);
            break;
        case "ArrowDown":
            snake.newDir(0, 1);
            break;
    }
}

/**
 * Número aleatorio entre min (incluido) y max (excluido)
 */
function getRandom(min, max) {
    return Math.random() * (max - min) + min;
}

/**
 * Distance between the points (x1,y1) and (x2,y2)
 */
function dist(x1, y1, x2, y2) {
    return Math.sqrt((Math.pow((x1 - x2), 2)) + (Math.pow((y1 - y2), 2)))
}