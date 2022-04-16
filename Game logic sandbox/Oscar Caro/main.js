const cellSize = 20;
const mapSize = 500;
var numRows;
var numCols;

var snake;      // player snake
var botSnakes;  // array with all botSnakes
var food;

// P5js function: called just once at beginning of game
function setup() {
  createCanvas(mapSize, mapSize);

  // Compute number of rows/columns
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

  // speed of new cycles (slow for arcade games)
  frameRate(5);
}

// P5js function: called on every cycle
function draw() {
  background(100);

  // Enviar estado local al servidor
  // Obtener el estado global del servidor

  // Update positions and status of all snakes
  botSnakes.forEach(botSnake => botSnake.update());
  snake.update();

  // Paint new cycle on screen
  snake.draw();
  botSnakes.forEach(botSnake => botSnake.draw());
  food.draw();
}

// P5js function: arrow keys pressed -> update dir of player snake
function keyPressed() {
  if (keyCode === LEFT_ARROW) {
    snake.newDir(-1, 0);
  }
  else if (keyCode === RIGHT_ARROW) {
    snake.newDir(1, 0);
  }
  else if (keyCode === UP_ARROW) {
    snake.newDir(0, -1);
  }
  else if (keyCode === DOWN_ARROW) {
    snake.newDir(0, 1);
  }
}