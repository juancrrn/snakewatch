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

  /**
   * Creates a JSON object representing the current state
   * @returns JSON object containing current state
   */
  toJSON() {
    return { pos: this.pos, eaten: this.eaten };
  }

  /**
   * Updates current state from the given JSON representation
   */
  fromJSON(json) {
    if (json.eaten === this.active) {
      if (this.active) {
        this.setActive(false).setVisible(false);
      } else {
        this.setActive(true).setVisible(true);
        this.setPosition(json.pos.x * 20 + 10, json.pos.y * 20 + 10);
      }
    } else {
      this.setPosition(json.pos.x * 20 + 10, json.pos.y * 20 + 10);
    }
  }
}
