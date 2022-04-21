import BotSnake from '../entities/botSnake.js';
import Food from '../entities/food.js';
import PlayerSnake from '../entities/playerSnake.js';
import SnakePart from '../entities/snakePart.js';

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
    this.food = new Food(this, { x: 6, y: 6 });

    this.snakesGroup = null;
    // Create player
    this.player = new PlayerSnake(this, this.snakesGroup, { x: 4, y: 2 }, 'white');
    
    // Create bots
    this.bots = [];
    this.bots.push(new BotSnake(this, this.snakesGroup, { x: 7, y: 6 }, 'red'));
    this.bots.push(new BotSnake(this, this.snakesGroup, { x: 12, y: 14 }, 'red'));
    this.bots.push(new BotSnake(this, this.snakesGroup, { x: 14, y: 2 }, 'red'));
    this.bots.push(new BotSnake(this, this.snakesGroup, { x: 3, y: 19 }, 'red'));

    // Cursors
    this.cursors = this.input.keyboard.createCursorKeys();

    // TODO: en vez de cambiar nueva direccion, pasarla por websocket al propietario
    this.cursors.up.on('down', () => this.player.setDir(0), this);
    this.cursors.right.on('down', () => this.player.setDir(1), this);
    this.cursors.down.on('down', () => this.player.setDir(2), this);
    this.cursors.left.on('down', () => this.player.setDir(3), this);
  }

  exportToJson() {
    return {
      food: this.food.exportJson(),
      snakes: this.bots.map(b => b.exportJson()),
      player: this.player.exportJson()
    };
  }

  importFromJson(json) {
    //this.json.snakes.map(snakeJson => b.exportJson());
  }
}
