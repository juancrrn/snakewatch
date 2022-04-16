class Food {

  constructor() {
    // Appear on a random spot in the map
    this.x = Math.floor(getRandom(0, numCols - 1));
    this.y = Math.floor(getRandom(0, numRows - 1));

    console.log(numCols);
    console.log(getRandom(0, numCols - 1));
    console.log(Math.floor(getRandom(0, numCols - 1)));
  }

  draw(gameAreaContext) {
    /*
    fill(0, 255, 0);
    noStroke();
    rect(this.x * cellSize, this.y * cellSize, cellSize, cellSize);
    */

    gameAreaContext.fillStyle = "red";
    gameAreaContext.fillRect(this.x * cellSize, this.y * cellSize, cellSize, cellSize);
  }

}