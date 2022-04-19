/**
 * Class that represents a part of the body of a Snake
 */
export default class SnakePart extends Phaser.GameObjects.Sprite {

  constructor(snake, pos) {
    super(snake.scene, pos.x * 20 + 10, pos.y * 20 + 10, snake.skin);
    this.snake = snake;
    this.scene.add.existing(this);
    this.scene.physics.add.existing(this);
    this.body.setCollideWorldBounds(true);
    this.pos = pos;
  }

  update() {}

  moveTo(pos) {
    let oldPos = this.pos;
    this.pos = pos;
    this.setPosition(pos.x * 20 + 10, pos.y * 20 + 10);
    return oldPos;
  }
}
