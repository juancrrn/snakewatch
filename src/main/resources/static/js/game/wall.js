class Wall {

    constructor() {
      // Appear on a random spot in the map
      this.x = Math.floor(getRandom(0, numCols - 1));
      this.y = Math.floor(getRandom(0, numRows - 1));
    }

    /*
    constructor(xPos, yPos) {
        // Appear on a random spot in the map
        this.x = xPos;
        this.y = yPos;
    }
    */
  
    draw(gameAreaContext) {
      /*
      fill(0, 255, 0);
      noStroke();
      rect(this.x * cellSize, this.y * cellSize, cellSize, cellSize);
      */
      gameAreaContext.fillStyle = "yellow";
      gameAreaContext.fillRect(this.x * cellSize, this.y * cellSize, cellSize, cellSize);
    }
  
  }