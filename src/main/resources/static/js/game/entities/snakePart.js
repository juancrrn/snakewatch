/**
 * Class that represents a part of the body of a Snake
 */
export default class SnakePart extends Phaser.GameObjects.Sprite {

  constructor(snake, pos, frame = 1) {
    super(snake.scene, pos.x * 20 + 10, pos.y * 20 + 10, snake.skin, frame);
    this.snake = snake;
    this.scene.add.existing(this);
    this.scene.physics.add.existing(this);
    this.body.setCollideWorldBounds(true);
    this.pos = pos;

    this.frameId = frame;
    this.setAngle(90 * pos.dir);
  }

  update() {}

  die() {
    // Mark the no longer occupied cell as empty
    this.scene.setCellState(this.pos, true);
    this.destroy();
  }

  moveTo(pos) {
    let oldPos = this.pos;
    this.pos = pos;
    this.setPosition(pos.x * 20 + 10, pos.y * 20 + 10);

    this.setAngle(90 * pos.dir);

    if (this === this.snake.parts.at(0)) {
      if (pos.turn) {
        pos.turn = false;
        oldPos.turn = true;
        oldPos.dir = pos.dir;
      }
    } else if (this !== this.snake.parts.at(-1) || this.snake.parts.length !== this.snake.size) {
      this.frameId = pos.turn ? 2 : 1;
      this.setFrame(this.frameId);
      if ((pos.dir + 1) % 4 === oldPos.dir) this.setAngle(this.angle - 90);
    }

    return oldPos;
  }

  /**
   * Creates a JSON object representing the current state
   * @returns JSON object containing current state
   */
  toJSON() {
    return { x: this.pos.x, y: this.pos.y, angle: this.angle, frame: this.frameId };
  }

  /**
   * Updates current state from the given JSON representation
   */
  fromJSON(json) {
    this.pos.x = json.x;
    this.pos.y = json.y;
    this.setAngle(json.angle);
    this.setFrame(json.frame);
    this.setPosition(this.pos.x * 20 + 10, this.pos.y * 20 + 10);
  }
}
