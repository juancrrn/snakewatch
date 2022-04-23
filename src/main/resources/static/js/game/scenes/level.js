import BotSnake from '../entities/botSnake.js';
import Food from '../entities/food.js';
import PlayerSnake from '../entities/playerSnake.js';
import SnakePart from '../entities/snakePart.js';

/**
 * Scene that represents the current match and level
 */
export default class Level extends Phaser.Scene {
  /**
   * Constructor de la escena
   */
  constructor() {
    super({ key: 'level' });
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

    this.snakesGroup = this.physics.add.group();
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

    this.cursors.up.on('down', () => this.player.setDir(0), this);
    this.cursors.right.on('down', () => this.player.setDir(1), this);
    this.cursors.down.on('down', () => this.player.setDir(2), this);
    this.cursors.left.on('down', () => this.player.setDir(3), this);

    // Set level collisions
    this.wallsLayer.setCollisionFromCollisionGroup();
    this.wallsLayer.setTileIndexCallback(2, this.onCollision, this);

    // Set timer for cycles execution
    this.timer = this.time.addEvent({
      delay: 500,
      callback: this.processTick,
      callbackScope: this,
      loop: true
    })
  }

  /**
   * Handles collision for two given GameObjects
   * If the objects are Snakes they are killed
   * @param {Phaser.GameObjects.GameObject} o1 
   * @param {Phaser.GameObjects.GameObject} o2 
   */
  onCollision(o1, o2) {
    if (o1 instanceof SnakePart) {
      o1.snake.die();
    } else if (o2 instanceof SnakePart) {
      o2.snake.die();
    }
  }

  /**
   * Calls the tick processing method of all the snakes
   */
  processTick() {
    this.bots.forEach(bot => bot.processTick());
    this.player.processTick();

    ws.stompClient.send("/topic/match" + MATCH, ws.headers, JSON.stringify(this.exportToJson()));
  }

  /**
   * Checks if the snake would crash when moving to the new position
   * @param pos The position to check
   * @returns True if the position is occupied. False otherwise
   */
  isOccupied(pos) {
    return (pos.x < 0 || pos.x >= 25) || (pos.y < 0 || pos.y >= 25) || this.isSnake(pos) || this.isWall(pos);
  }

  /**
   * Checks if there is a snake at the given position
   * @param pos The position to check
   * @returns True if there is a snake at the given position. False otherwise
   */
  isSnake(pos) {
    return this.physics.overlapRect(pos.x * 20 + 10, pos.y * 20 + 10, 0, 0, true, false).length > 0;
  }

  /**
   * Checks if there is a wall at the given position
   * @param pos The position to check
   * @returns True if there is a wall at the given position. False otherwise
   */
  isWall(pos) {
    return this.map.getTileAt(pos.x, pos.y) !== null;
  }

  /**
   * Gets a cell that is not occupied and is not a wall
   * @returns The position of the empty cell
   */
  getEmptyCell() {
    let pos;
    do {
      pos = { x: Math.floor(Math.random() * 25), y: Math.floor(Math.random() * 25) }
    } while (this.isOccupied(pos));

    return pos;
  }

  exportToJson(){
    return {
      food: this.food.exportJson(),
      snakes: this.bots.map(b => b.exportJson()),
      player: this.player.exportJson()
    };
  }
}
