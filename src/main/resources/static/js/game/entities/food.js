export default class Food extends Phaser.GameObjects.Sprite {

  constructor(scene, pos) {
    super(scene, pos.x * 20 + 10, pos.y * 20 + 10, "pink");
    this.pos = pos;
    this.scene.add.existing(this);
    this.eaten = false;
  }

  processTick() {
    if (this.eaten) {
      this.eaten = true;
      // TODO: randomize position
    }
  }

  canEat(pos) {
    if (pos.x === this.pos.x && pos.y === this.pos.y) {
      this.eaten = true;
      return true;
    }
    return false;
  }

  exportJson() {
    let json = {};
    json.x = this.pos.x;
    json.y = this.pos.y;
    json.eaten = this.eaten;
    return json;
  }
}
