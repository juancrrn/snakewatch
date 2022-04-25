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

    let tileset = this.map.addTilesetImage('tileset', 'tileset');

    this.groundLayer = this.map.createLayer('ground', tileset);
    this.wallsLayer = this.map.createLayer('walls', tileset);

    // Create food
    this.food = new Food(this, { x: 0, y: 0 });

    // Snakes
    this.snakes = new Map();

    // Cursors
    if(PLAYERS.indexOf(USERSESSIONAME) != -1){
    this.cursors = this.input.keyboard.createCursorKeys();

    this.cursors.up.on('down', () => this.sendMove(0), this);
    this.cursors.right.on('down', () => this.sendMove(1), this);
    this.cursors.down.on('down', () => this.sendMove(2), this);
    this.cursors.left.on('down', () => this.sendMove(3), this);
    }
    // Add key to toggle game fullscreen
    this.input.keyboard.addKey('F').on('down', () => this.scale.toggleFullscreen(), this);

    // Guardar la version previa de ws.receive para no sobreescribirla
    const oldReceive = ws.receive;
    ws.receive = (text) => {
      if (text.type == "GameState") this.fromJSON(text.message);
      // Llamar a la antigua version de receive() para no sobreescribirla
      if (oldReceive != null) oldReceive(text);
    }
  }

  /**
   * Sends the desired move to the host over websocket
   */
  sendMove(dir) {
    const body = JSON.stringify({type: "Move", message: { user: USERSESSIONAME, dir: dir }});
    ws.stompClient.send("/topic/match" + MATCH, ws.headers, body);
  }

  setCellState() { }

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
        this.snakes.set(key, new Snake(this, null, { x: 0, y: 0 }, 0, snake.skin));
      }
      this.snakes.get(key).fromJSON(snake);
    }
    this.food.fromJSON(json.food);
  }
}
