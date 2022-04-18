const cycleTime = 200;      // Duration of a cycle (in milliseconds)
const cellSize = 20;        // Size of a square cell unit
const mapSize = 500;        // Size of the whole map

var numRows;        // numRows = mapSize / cellSize
var numCols;        // numCols = mapSize / cellSize

var snake;          // player snake
var botSnakes;      // array with all botSnakes
var food;
var walls;          // array of wall positions

/** 
 * Called when the html has finished been loaded
 */
function startGame(levelId) {
    numRows = mapSize / cellSize;
    numCols = mapSize / cellSize;

    snake = new Snake();
    food = new Food();
    botSnakes = [];
    botSnakes[0] = new BotSnake();
    botSnakes[1] = new BotSnake();
    botSnakes[2] = new BotSnake();
    botSnakes[3] = new BotSnake();
    botSnakes[4] = new BotSnake();
    
    walls = [];
    walls[0] = new Wall();
    walls[1] = new Wall();
    walls[2] = new Wall();
    walls[3] = new Wall();
    walls[4] = new Wall();

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
        this.interval = setInterval(this.update, cycleTime);
    },
    update: function () {
        //Called periodically by setInterval() on myGameArea.start()
        botSnakes.forEach(botSnake => botSnake.update());
        snake.update();

        myGameArea.clear();

        // Paint new cycle on screen
        snake.draw(myGameArea.context);
        botSnakes.forEach(botSnake => botSnake.draw(myGameArea.context));
        food.draw(myGameArea.context);
        walls.forEach(wall => wall.draw(myGameArea.context));
    },
    clear: function () {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
}

/**
 * Arrow keys pressed -> update dir of player snake
 */
document.onkeydown = function (e) {
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