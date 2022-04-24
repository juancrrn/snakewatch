import SnakePart from './snakePart.js';

/**
 * Class that represents a generic Snake and handles common logic of them
 */
export default class Snake {

  constructor(scene, snakesGroup, pos, initSize, skin) {
    this.scene = scene;
    this.snakesGroup = snakesGroup;
    this.skin = skin;
    this.initSize = initSize;
    this.dead = false;

    this.parts = [];
    this.head = new SnakePart(this, pos);
    this.parts.push(this.head);
    if (this.snakesGroup !== null) this.snakesGroup.addMultiple(this.parts);
    this.scene.physics.add.overlap(this.head, this.snakesGroup, this.onCollision, null, this);
    this.scene.physics.add.collider(this.head, this.scene.wallsLayer);

    this.scene.setCellState(pos, false);

    this.dir = Math.floor(Math.random() * 4);
    this.lastDir;
  }

  chooseNextMove() {}

  /**
   * Handles the movement of the snake each cycle
   */
  processTick() {
    this.chooseNextMove();
    if (!this.dead) {
      // Calculate next position
      let dest = this.nextPos();

      // Check if the snake will collide with the level borders
      if ((dest.x < 0 || dest.x >= 25) || (dest.y < 0 || dest.y >= 25)) {
        this.die();

      } else {
        // Advance part positions
        let tailPos = this.move(dest);

        // Grow if te snake can eat
        if (this.canEat(dest) || this.parts.length < this.initSize) {
          let tail = new SnakePart(this, tailPos, true);
          if (this.snakesGroup !== null) this.snakesGroup.add(tail);
          this.parts.push(tail);
        } else {
          // Mark the no longer occupied cell as empty
          this.scene.setCellState(tailPos, true);
        }

        // Mark the target cell as occupied
        this.scene.setCellState(dest, false);
      }
    }
  }

  /**
   * Handles delayed death of the snake when it collides with another
   */
  handleDeath() {
    if (!this.dead && this.collided !== undefined) {
      this.die(this.collided);
    }
  }

  /**
   * Sets the direction of the snake's next movement
   * @param dir The direction to move to
   */
  setDir(dir) {
    if (dir % 2 !== this.lastDir % 2 || this.lastDir === dir) {
      this.dir = dir;
    }
  }

  /**
   * Handles collision between snakes
   * @param {SnakePart} one
   * @param {SnakePart} other
   */
  onCollision(one, other) {
    if (this === one.snake) {
      this.collided = other.snake;
    } else {
      this.collided = one.snake;
    }
  }

  /**
   * Handles death of the snake
   */
  die(why) {
    let headPos = this.head.pos;
    this.parts.forEach((part) => part.die(), this);
    this.parts = [];
    this.dead = true;

    if (why instanceof Snake) {
      // If the other snake is not goind to die, restore the state of the cell
      if (why.collided === undefined) this.scene.setCellState(headPos, false);
    } else if (why !== undefined) { // If collided with a wall, restore the state of the cell
      this.scene.setCellState(headPos, false);
    }
  }

  /**
   * Checks if the snake can eat on its new position
   * @param pos Position the snake will move to
   * @returns If there is food to be eaten at that location
   */
  canEat(pos) {
    return this.scene.food.canEat(pos);
  }

  /**
   * Gets the new position for the snake when moving to a
   * given direction
   * @param dir Optional. The direction the snake would move.
   * Defaults to current direction
   * @returns The new position for the snake
   */
  nextPos(dir = this.dir) {
    let pos = { x: this.head.pos.x, y: this.head.pos.y };
    switch (dir) {
      case 0:
        pos.y--;
        break;
      case 1:
        pos.x++;
        break;
      case 2:
        pos.y++;
        break;
      case 3:
        pos.x--;
        break;
  
      default:
        break;
    }

    return pos;
  }

  /**
   * Handles movement of the parts of the snake's body
   * @param pos The new position for the snake
   * @returns The previous position of the snake's tail
   */
  move(pos) {
    this.lastDir = this.dir;
    for (let idx = 0; idx < this.parts.length; idx++) {
      const part = this.parts[idx];
      pos = part.moveTo(pos);
    }

    return pos;
  }

  /**
   * Creates a JSON object representing the current state
   * @returns JSON object containing current state
   */
  toJSON() {
    return {
      parts: this.parts.map(p => p.toJSON()),
      dead: this.dead,
      skin: this.skin
    };
  }

  /**
   * Updates current state from the given JSON representation
   */
  fromJSON(json) {
    if (!json.dead) {
      while (this.parts < json.parts) this.parts.push(new SnakePart(this, { x: 0, y: 0 }));
      for (let idx = 0; idx < this.parts.length; idx++) {
        this.parts[idx].fromJSON(json.parts[idx]);
      }
    } else if (!this.dead) {
      this.die();
    }
  }
}
