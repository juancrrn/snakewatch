class Food {

  constructor() {
    // Appear on a random spot in the map
    this.x = floor(random(0, numCols - 1));
    this.y = floor(random(0, numRows - 1));
  }

  draw() {
    fill(0, 255, 0);
    noStroke();
    rect(this.x * cellSize, this.y * cellSize, cellSize, cellSize);
  }

}