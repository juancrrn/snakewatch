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
    return oldPos;
  }

  /**
   * Creates a JSON object representing the current state
   * @returns JSON object containing current state
   */
  toJSON() {
    return { x: this.pos.x, y: this.pos.y, dir: this.pos.dir, frame: this.frameId };
  }

  /**
   * Updates current state from the given JSON representation
   */
  fromJSON(json) {
    this.pos.x = json.x;
    this.pos.y = json.y;
    this.pos.dir = json.dir;
    this.setAngle(90 * json.dir);
    this.setFrame(json.frame);
    this.setPosition(this.pos.x * 20 + 10, this.pos.y * 20 + 10);
  }
}
