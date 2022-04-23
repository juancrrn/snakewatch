export default class Food extends Phaser.GameObjects.Sprite {

  constructor(scene, pos) {
    super(scene, pos.x * 20 + 10, pos.y * 20 + 10, "pink");
    this.pos = pos;
    this.scene.add.existing(this);
    this.eaten = false;
  }

  processTick() {
    if (this.eaten) {
      this.pos = this.scene.getEmptyCell();
      if (this.pos !== null) {
        this.eaten = false;
        this.setPosition(this.pos.x * 20 + 10, this.pos.y * 20 + 10);
        if (!this.active) this.setActive(true).setVisible(true);
      } else if (this.active) {
        this.setActive(false).setVisible(false);
      }
    }
  }

  canEat(pos) {
    if (!this.eaten && pos.x === this.pos.x && pos.y === this.pos.y) {
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

  importFromJson(json) {
    this.pos.x = json.x;
    this.pos.y = json.y;
    this.setPosition(this.pos.x * 20 + 10, this.pos.y * 20 + 10);
    this.eaten = json.canEat;
  }
}
