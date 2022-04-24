import Food from '../entities/food.js';
import Snake from '../entities/snake.js';

/**
 * Scene that represents the current match and level
 */
export default class Spectator extends Phaser.Scene {
  /**
   * Constructor de la escena
   */
  constructor() {
    super({ key: 'spectator' });
  }

  preload() {
    // TODO: load the tilemap of the level selected for this match
  }

  create() {
    // Loads the map
    this.map = this.make.tilemap({
      key: 'tilemap'
    })

    let tileset = this.map.addTilesetImage('tileset','tileset');

    this.groundLayer = this.map.createLayer('ground', tileset);
    this.wallsLayer = this.map.createLayer('walls', tileset);

    // Create food
    this.food = new Food(this, { x: 0, y: 0 });

    // Snakes
    this.snakes = new Map();

    ws.receive = (text) => {
      if (text.type=="GameState") this.fromJSON(text.message);
    }
  }

  setCellState() {}

  /**
   * Updates current state from the given JSON representation
   */
  fromJSON(json) {
    if (this.time !== json.time) {
      this.time = json.time;
      this.snakes.forEach((s) => s.die());
      this.snakes = new Map();
    }
    for (const key in json.snakes) {
      let snake = json.snakes[key];
      if (!this.snakes.has(key)) {
        this.snakes.set(key, new Snake(this, null, { x: 0, y: 0}, 0, snake.skin));
      }
      this.snakes.get(key).fromJSON(snake);
    }
    this.food.fromJSON(json.food);
  }
}
