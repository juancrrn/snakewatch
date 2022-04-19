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
    if (this.isCrash(dest) || Math.random() > 0.7) { // Also turn with a 30% probability
      // Turn right or left with a 50% probability
      newDir = (newDir + (Math.random() >= 0.5 ? 1 : 3)) % 4;
      dest = this.nextPos(newDir);

      // If the snake would crash, turn to the other side
      if (this.isCrash(dest)) {
        newDir = newDir + 2 % 4;
        dest = this.nextPos(newDir);

        // If the snake would crash, return to current direction
        if (this.isCrash(dest)) {
          newDir = this.dir;
        }
      }

      // Set new direction
      this.dir = newDir;
    }
  }

  /**
   * Checks if the snake would crash when moving to the new position
   * @param dest The position to check
   * @returns True it would collide. False otherwise
   */
  isCrash(dest) {
    return (dest.x < 0 || dest.x >= 25) || (dest.y < 0 || dest.y >= 25) || this.isOccupied(dest) || this.isWall(dest);
  }

  /**
   * Checks if the snake would crash with a snake when moving to the new position
   * @param dest The position to check
   * @returns True it would collide. False otherwise
   */
  isOccupied(pos) {
    return this.scene.physics.overlapRect(pos.x * 20 + 5, pos.y * 20 + 5, 10, 10, true, false).length > 0;
  }

  /**
   * Checks if the snake would crash with a wall when moving to the new position
   * @param dest The position to check
   * @returns True it would collide. False otherwise
   */
  isWall(pos) {
    return this.scene.map.getTileAt(pos.x, pos.y) !== null;
  }
}
