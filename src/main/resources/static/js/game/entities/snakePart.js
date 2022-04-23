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

  exportJson() {
    return {x: this.pos.x, y: this.pos.y};
  }

  importFromJson(json){
    this.pos.x = json.x;
    this.pos.y = json.y;
    this.setPosition(this.pos.x * 20 + 10, this.pos.y * 20 + 10);
  }

}
