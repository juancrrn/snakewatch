import Snake from '../entities/snake.js';

/**
 * Class that represents a Snake controlled by no player
 */
export default class BotSnake extends Snake {

  constructor(scene, snakesGroup, pos, texture) {
    super(scene, snakesGroup, pos, 5, texture);
  }

  chooseNextMove() {
    // Calculate next position
    let dest = this.nextPos();
    let newDir = this.dir;

    // Turn if the snake will collide with the level borders, walls or a snake
    if (this.scene.isOccupied(dest) || Math.random() > 0.7) { // Also turn with a 30% probability
      // Turn right or left with a 50% probability
      newDir = (newDir + (Math.random() >= 0.5 ? 1 : 3)) % 4;
      dest = this.nextPos(newDir);

      // If the snake would crash, turn to the other side
      if (this.scene.isOccupied(dest)) {
        newDir = (newDir + 2) % 4;
        dest = this.nextPos(newDir);

        // If the snake would crash, return to current direction
        if (this.scene.isOccupied(dest)) {
          newDir = this.dir;
        }
      }

      // Set new direction
      this.dir = newDir;
    }
  }
}
