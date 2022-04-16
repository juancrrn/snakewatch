class BotSnake extends Snake {

  constructor() {
    super();

    // Appear on a random spot of the map
    var initX = Math.floor(getRandom(6, numCols - 1));
    var initY = Math.floor(getRandom(0, numRows - 1));

    // Start with long body
    this.body[0] = {x: initX, y:initY};
    this.body[1] = {x: this.head.x - 1, y:this.head.y};
    this.body[2] = {x: this.head.x - 2, y:this.head.y};
    this.body[3] = {x: this.head.x - 3, y:this.head.y};
    this.body[4] = {x: this.head.x - 4, y:this.head.y};

    // Direction:
    this.xDir = 0;
    this.yDir = 1;
  }

  update() {
    this.newRandomDir();
    super.update();
  }

  // Logic of bot's movement (they avoid crashes and sometimes they change direction)
  newRandomDir() {

    // Si (crash de frente) o (30% prob) -> mover al lado
    if (this.checkCrash(this.head.x + this.xDir, this.head.y + this.yDir) ||
      Math.random() > 0.7) {
      var newXDir, newYDir;

      // Si se estaba moviendo en eje Y (arriba o abajo) -> mover en eje X (izq o dcha)
      if (this.xDir == 0) {
        newYDir = 0;

        // Elegir si izq o dcha:

        // Si (crash de izquierda) o (50%prob + no crash derecha) -> mover derecha
        if (this.checkCrash(this.head.x - 1, this.head.y) ||
          (Math.random() > 0.5 && !this.checkCrash(this.head.x + 1, this.head.y))) {
          newXDir = 1;
        }
        // Sino (no crash de izq) -> mover izquierda
        else {
          newXDir = -1;
        }
      }
      // Si se estaba moviendo en eje X (dcha o izq) -> mover en eje Y (arriba o abajo)
      else {
        newXDir = 0;

        // Elegir si arriba o abajo:

        // Si (crash arriba) o (50% prob + no crash abajo) -> mover abajo
        if (this.checkCrash(this.head.x, this.head.y - 1) ||
          (Math.random() > 0.5 && !this.checkCrash(this.head.x, this.head.y + 1))) {
          newYDir = 1;
        }
        // Sino (no crash arriba) -> mover arriba
        else {
          newYDir = -1;
        }
      }

      // Apply new dir value:
      this.xDir = newXDir;
      this.yDir = newYDir;
    }
  }


  // Death function -> put new botSnake on the variable of the dead one
  restart() {
    // Check which botSnake has has died by comparing positions of head
    for (var i = 0; i < botSnakes.length; i++) {
      if (this.head === botSnakes[i].head) {
        botSnakes[i] = new BotSnake();
        return;
      }
    }
  }

  // BotSnake is red
  get color() {
    return [250, 100, 100];
  }
}