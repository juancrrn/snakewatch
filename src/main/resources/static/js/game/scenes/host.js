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
    super({ key: 'host' });
  }

  create() {
    // Loads the map
    this.map = this.make.tilemap({
      key: 'tilemap'
    })

    let tileset = this.map.addTilesetImage('tileset','tileset');

    this.groundLayer = this.map.createLayer('ground', tileset);
    this.wallsLayer = this.map.createLayer('walls', tileset);

    // Store empty cells
    this.cells = new Set();
    for (let i = 0; i < 625; i++) this.cells.add(i);

    // Mark walls as occupied cells
    this.map.forEachTile((wall) => this.cells.delete(wall.x + wall.y * 25), this, 0, 0, 25, 25, { isNotEmpty: true })

    // Create players
    this.snakesGroup = this.physics.add.group();
    this.snakes = new Map();

    this.player = new PlayerSnake(this, this.snakesGroup, this.getEmptyCell(), 'white');
    this.snakes.set(USERSESSIONAME, this.player);
    PLAYERS.forEach((p) => {
      if (p !== USERSESSIONAME) {
        this.snakes.set(p, new PlayerSnake(this, this.snakesGroup, this.getEmptyCell(), 'red'));
      }
    });

    this.results = [];
    this.contador = NPLAYERS;

    // Create food
    this.food = new Food(this, this.getEmptyCell());

    // Cursors
    this.cursors = this.input.keyboard.createCursorKeys();

    this.cursors.up.on('down', () => this.player.setDir(0), this);
    this.cursors.right.on('down', () => this.player.setDir(1), this);
    this.cursors.down.on('down', () => this.player.setDir(2), this);
    this.cursors.left.on('down', () => this.player.setDir(3), this);

    // Add key to toggle game fullscreen
    this.input.keyboard.addKey('F').on('down', () => this.scale.toggleFullscreen(), this);

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

    // Let some logic be delayed
    this.ticked = false;
    this.events.on('update', () => this.postTick(), this);

    this.time = Date.now();

    ws.subscribe("/topic/match" + MATCH, (text) => {
      if (text.type == "Move") this.onMoveRequest(text.message);
      if(text.type == "playerDeath"){

        let p = {
          playerName: text.message,
          position: this.contador
        }

        this.results.push(p);
        this.contador-=1; 
      }
    });

    // Broadcast initial game state
    this.broadcastState();
    go("/rooms/start_match/" + MATCH, 'POST', {})
    .then(d => e => console.log("happy", e))
    .catch(e => console.log("sad", e))
  }

  /**
   * Handles a movement request from a remote player
   * @param request The received request
   */
  onMoveRequest(request) {
    let snake = this.snakes.get(request.user);
    if (snake !== undefined) snake.setDir(request.dir);
  }

  /**
   * Handles collision for two given GameObjects
   * If the objects are Snakes they are killed
   * @param {Phaser.GameObjects.GameObject} o1 
   * @param {Phaser.GameObjects.GameObject} o2 
   */
  onCollision(o1, o2) {
    if (o1 instanceof SnakePart) {
      o1.snake.die(o2);
    } else if (o2 instanceof SnakePart) {
      o2.snake.die(o1);
    }
  }

  /**
   * Calls the tick processing method of all the snakes
   */
  processTick() {
    this.snakes.forEach(snake => snake.processTick());

    this.food.processTick();
    this.ticked = true;
  }

  postTick() {
    if (this.ticked) {
      this.ticked = false;
      this.snakes.forEach(snake => snake.handleDeath());

      let alivePlayers = 0;

      this.snakes.forEach(snake => {
        if(!snake.dead){
          alivePlayers+=1;
        }
      });

      if(alivePlayers<=1){

          go("/rooms/finish_match/" + MATCH, 'POST', {
            message: ["admin", "user1"]
          })
          .then(d => {console.log("happy", d)})
          .catch(e => console.log("sad", e))

          this.timer.destroy();
        
      }

      this.broadcastState();
    }
  }

  /**
   * Shares current gamestate over websocket
   */
  broadcastState() {
    const body = JSON.stringify({type: "GameState", message: this.toJSON()});
    ws.stompClient.send("/topic/match" + MATCH, ws.headers, body);
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
    let cell = [...this.cells][Math.floor(Math.random() * this.cells.size)];

    return cell === undefined ? null : { x: cell % 25, y: ~~(cell / 25) };
  }

  /**
   * Changes the state of the given position to be empty or not
   * @param pos The position of which to change the state of
   * @param {boolean} empty If is empty or not
   */
  setCellState(pos, empty) {
    if (empty) {
      this.cells.add(pos.x + pos.y * 25);
    } else {
      this.cells.delete(pos.x + pos.y * 25);
    }
  }

  /**
   * Creates a JSON object representing the current state
   * @returns JSON object containing current state
   */
  toJSON() {
    let snakes = {};
    this.snakes.forEach((v, k) => snakes[k] = v.toJSON());
    return { food: this.food.toJSON(), snakes: snakes, time: this.time };
  }
}