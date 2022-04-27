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


    this.texts = [];
    //Create Texts
    this.add.text(390, 0, "Top    Scores", {
      color: '#FFFFFF',
      fontStyle: 'italic',
      fontSize: 14
    });

    let cont = 0;
    PLAYERS.forEach(p => {
      let texto =  this.add.text(370,(cont+1) * 15, 1 + " " + p + "    " + 0, {
        color: '#FFFFFF',
        fontStyle: 'italic',
        fontSize: 14
      });
      this.texts.push(texto);
      cont++;
    });

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

    ws.subscribe("/topic/match" + MATCH, (text) => {
      if (text.type == "GameState") this.fromJSON(text.message);
    });
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
    if (this.startTime !== json.time) {
      this.startTime = json.time;
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
    this.texts.forEach(t =>{
      t.setText(json.texts[this.texts.indexOf(t)]); 
    });
  }
}
