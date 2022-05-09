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

    this.player = new PlayerSnake(this, this.snakesGroup, this.getEmptyCell(), PLAYERSKIN, USERSESSIONAME);
    this.snakes.set(USERSESSIONAME, this.player);
    PLAYERS.forEach((p) => {
      if (p[0] !== USERSESSIONAME) {
        this.snakes.set(p[0], new PlayerSnake(this, this.snakesGroup, this.getEmptyCell(), p[1], p[0]));
      }
    });

    this.results = [];

    this.counter = NPLAYERS;
    this.texts = [];
    //Create Texts
   

    // Create food
    this.food = new Food(this, this.getEmptyCell());


    let topScoreText = this.add.text(390, 0, "Top    Scores", {
      color: '#FFFFFF',
      fontStyle: 'italic',
      fontSize: 14
    });

    topScoreText.setDepth(1);

    let cont = 0;
    this.snakes.forEach(snake => {
      let texto =  this.add.text(370,(cont+1) * 15, snake.gamePosition + " " + snake.username + "    " + snake.score, {
        color: '#FFFFFF',
        fontStyle: 'italic',
        fontSize: 14
      });
      texto.setDepth(1);
      this.texts.push(texto);
      cont++;
    });

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

    this.timeToFinish = 90;

    this.finishGameTimer = this.time.addEvent({
      delay: 1000,
      callback: this.processSecond,
      callbackScope: this,
      loop: true
    })

    let remainingTimeText = this.add.text(190, 0, "Remaining Time", {
      color: '#FFFFFF',
      fontStyle: 'italic',
      fontSize: 14
    });

    remainingTimeText.setDepth(1);

    this.timeText = this.add.text(230, 15, this.timeInMinutesSeconds(this.timeToFinish), {
      color: '#FFFFFF',
      fontStyle: 'italic',
      fontSize: 14
    });

    this.timeText.setDepth(1);

    // Let some logic be delayed
    this.ticked = false;
    this.events.on('update', () => this.postTick(), this);

    this.startTime = Date.now();

    ws.subscribe("/topic/match" + MATCH, (text) => {
      if (text.type == "Move") this.onMoveRequest(text.message);
      if (text.type == "finishMatch"){
        let toastHTML = document.getElementById('finishGameToast');
        let finishGameToastBody = document.getElementById('finishGameToastBody');
        let topScoresDiv = document.getElementById('topScoresDiv');
        finishGameToastBody.innerHTML = '';
        finishGameToastBody.appendChild(topScoresDiv);
        text.message.forEach(r => {
          let divR = document.createElement('div');
          divR.setAttribute("class", "row justify-content-around");
          let p1 = document.createElement('p');
          let p2 = document.createElement('p');
          switch(text.message.indexOf(r)){
            case 0:
            p1.style.color = 'gold';
            break;
            case 1:
            p1.style.color = 'silver';
            break;
            case 2:
            p1.style.color = 'brown';
            break;
            default:
            p1.style.color = 'grey';
          }
          p1.setAttribute("class", "col-4 text-center fs-5");
          p2.setAttribute("class", "col-4 text-center fs-5");
          p1.innerHTML = (text.message.indexOf(r) + 1) + ". " + r.playerName;
          p2.innerHTML = r.score;
          divR.appendChild(p1);
          divR.appendChild(p2);   
          finishGameToastBody.appendChild(divR);
        });


        let backButton = document.createElement("button");
        backButton.setAttribute("class", "w-50 btn btn-outline-danger text-center fs-5");
        backButton.innerHTML = 'Go Back To Room';       
        backButton.onclick = () => {
          window.location.replace("/rooms/" + ROOM);
        }
        
        finishGameToastBody.appendChild(backButton);
        toastHTML.style.display = '';
        let toast = new bootstrap.Toast(toastHTML);
        toast.show();
      }
    });

    // Broadcast initial game state
    this.broadcastState();
    go("/rooms/start_match/" + MATCH, 'POST', {})
    .then(d => console.log("Success", d))
    .catch(e => console.log("Error", e))
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
      this.actualizeGameState();

      this.broadcastState();
    }
  }


  actualizeGameState(){
    let alivePlayers = 0;
    let snakeScores = [];
    this.snakes.forEach(snake => {
      snakeScores.unshift({
        playerName: snake.username,
        score: snake.score, 
        lastSecondLive: snake.lastSecondLive
      })
      if (!snake.dead) {
        alivePlayers++;
        snake.lastSecondLive = this.timeToFinish;
      } 
      else { 
        if(!snake.putInResult){
          this.results.unshift({
            playerName: snake.username,
            score: snake.score,
            lastSecondLive: this.timeToFinish
          });      
          snake.putInResult = true;
          snake.lastSecondLive = this.timeToFinish;
        }           
      }
    });

    this.sortPlayersData(snakeScores);

    snakeScores.forEach(snakeScore => {
      let index =  snakeScores.indexOf(snakeScore);
      this.texts[index].setText((index + 1) + " " + snakeScore.playerName + "    " + snakeScore.score);
    });
   
    if (alivePlayers <= 1) {
      if(alivePlayers==1){
        this.snakes.forEach(snake => {
          if(!snake.dead && !snake.putInResult){
            if(this.timeToFinish > 0){
              this.results.unshift({
                playerName:  snake.username,
                score: snake.score,
                lastSecondLive: this.timeToFinish - 1
              });
              snake.putInResult = true;
              snake.lastSecondLive = this.timeToFinish;
            }        
          }
        })     
      }   
      this.sortPlayersData(this.results);
      this.finishGame();
    }

  }



  sortPlayersData(list){
    list.sort(function(a,b){
      if(a.lastSecondLive < b.lastSecondLive){
        return -1;
      }
      if(a.lastSecondLive > b.lastSecondLive){
        return 1;
      }

      if(a.score < b.score){
        return 1;
      }
      if(a.score > b.score){
        return -1;
      }

      if(a.playerName < b.playerName){
        return -1;
      }
      if(a.playerName > b.playerName){
        return 1;
      }
      return 0;
    });
  }


  completeResults(){

    this.snakes.forEach(snake => {
      if(!snake.dead && !snake.putInResult){
        this.results.unshift({
          playerName:  snake.username,
          score: snake.score,
          lastSecondLive: this.timeToFinish
        });
        snake.putInResult = true;
        snake.lastSecondLive = this.timeToFinish;
      }
    })
  
    this.sortPlayersData(this.results);
    this.finishGame();

  }


  timeInMinutesSeconds(seconds){
    //Minutes
    var minutes = Math.floor(seconds/60);
    // Seconds
    var partInSeconds = seconds%60;
    // Adds left zeros to seconds
    partInSeconds = partInSeconds.toString().padStart(2,'0');

    return `${minutes}:${partInSeconds}`;
  }

  processSecond(){
    this.timeToFinish-=1;

    this.timeText.setText(this.timeInMinutesSeconds(this.timeToFinish));
    if(this.timeToFinish==0){
      this.completeResults();
    }
  }

  finishGame(){
    go("/rooms/finish_match/" + MATCH, 'POST', {
      message: this.results
    })
    .then(d => console.log("Success", d))
    .catch(e => console.log("Error", e))
    this.timer.destroy();
    this.finishGameTimer.destroy();
    this.timer = undefined;
    this.finishGameTimer = undefined;
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
    let texts =[];
    this.snakes.forEach((v, k) => snakes[k] = v.toJSON());
    this.texts.forEach(t => {
      texts.push(t.text);
    })
    return { food: this.food.toJSON(), snakes: snakes, time: this.startTime, texts: texts, timeText: this.timeText.text};
  }

}
