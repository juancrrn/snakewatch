class Snake {

  /*  The snake is represented as an array of positions (called this.body) 
      Each position is a vector (x,y)
      
      On each cycle the last elem of the body is deleted 
      and a new one is inserted in the front
      
      If the snake eats a food, then the last element is not removed,
      so the snake gains +1 in size
      
      The snake has a direction (xDir, yDir) that represents the
      direction where it will move on next cycle
  */

  constructor() {
    // Appear on a random spot of the map
    var initX = Math.floor(getRandom(0, numCols - 1));
    var initY = Math.floor(getRandom(0, numRows - 1));

    // Initial body has just 1 position (snake of size 1)
    this.body = [];
    this.body[0] = { x: initX, y: initY };

    // Initial direction (right):
    this.xDir = 1;
    this.yDir = 0;
  }

  update() {
    // Compute next position of the head of the snake
    var newX = this.head.x + this.xDir;
    var newY = this.head.y + this.yDir;

    // If that positions crashes with something -> kill snake
    if (this.checkCrash(newX, newY)) {
      this.restart();
    }

    // Put "next position" at [0] of array and shift the rest (snake grows)
    this.body.unshift({ x: newX, y: newY });

    // Check if "next position" has food. If not, body restores previous size
    this.checkEat();
  }

  checkEat() {
    // Compute distance from head of snake to food
    var distance = dist(this.head.x, this.head.y, food.x, food.y);

    if (distance < 1) {
      // Snake eats food -> grows by previous shifting in update()
      food = new Food();
    }
    else {
      // Snake doesn't eat -> delete last position to compensate the shifting in update()
      this.body.pop();
    }
  }

  // Check if given position causes a crash
  checkCrash(xPos, yPos) {

    // Check crash against outer walls
    if (xPos < 0 || xPos >= numCols ||
      yPos < 0 || yPos >= numRows) {
      return true;
    }

    // Check crash against map walls
    for (let k = 0; k < walls.length; k++){
      if (xPos == walls[k].x && yPos == walls[k].y) {
        return true;
      }
    }
    
    // Check crash with user snake
    for (var i = 0; i < snake.body.length; i++) {
      if (xPos == snake.body[i].x &&
        yPos == snake.body[i].y) {
        return true;
      }
    }

    // Check crash with all botSnakes
    for (var j = 0; j < botSnakes.length; j++) {
      var botSnake = botSnakes[j];

      // Check crash with specific botSnake
      for (var k = 0; k < botSnake.body.length; k++) {
        if (xPos == botSnake.body[k].x && yPos == botSnake.body[k].y) {
          return true;
        }
      }
    }

    return false;
  }

  // Death function (overriden by botSnake to create new BotSnake)
  restart() {
    snake = new Snake();
  }

  draw(gameAreaContext) {
    var color = this.color;

    for (var i = 0; i < this.body.length; i++) {
      gameAreaContext.fillStyle = parseColor(color);

      // Locate exact pixel where the cell has to be placed
      var xPixel = this.body[i].x * cellSize;
      var yPixel = this.body[i].y * cellSize;

      // Paint the cell
      gameAreaContext.fillRect(xPixel, yPixel, cellSize, cellSize);

      // Decrease color to have cool grading efect
      color[0] -= 16; color[1] -= 16; color[2] -= 16;
    }
  }

  // Change direction of movement of snake
  newDir(newXDir, newYDir) {
    if (newXDir != this.xDir && newYDir != this.yDir) {
      this.xDir = newXDir;
      this.yDir = newYDir;
    }
  }

  get head() {
    return this.body[0];
  }

  // userSnake is white (overriden by botSnake to make them red)
  get color() {
    return [255, 255, 255];
  }

}

