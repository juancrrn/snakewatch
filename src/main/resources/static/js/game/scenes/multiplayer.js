import Food from '../entities/food.js';
import PlayerSnake from '../entities/playerSnake.js';


export default class Multiplayer extends Phaser.Scene {

    constructor() {
        super({ key: 'multiplayer'});
    }

    preload() {
        this.load.image('pink', '/img/pink.png');
        this.load.image('white', '/img/white.png');
    }

    create(){

        this.food = new Food(this, {x:Phaser.Math.Between(0,24), y:Phaser.Math.Between(0,24)});

        this.snakesGroup = this.physics.add.group();

        this.player = new PlayerSnake(this, this.snakesGroup, { x: 4, y: 2 }, 'white');

        this.cursors = this.input.keyboard.createCursorKeys();

        this.cursors.up.on('down', () => this.player.setDir(0), this);
        this.cursors.right.on('down', () => this.player.setDir(1), this);
        this.cursors.down.on('down', () => this.player.setDir(2), this);
        this.cursors.left.on('down', () => this.player.setDir(3), this);


        this.timer = this.time.addEvent({
            delay: 1000,
            callback: this.processTick,
            callbackScope: this,
            loop: true
          })
    }

    processTick() {
        if(!this.player.dead) {
            this.player.processTick();
        }
        else{
            this.player.dead = false;
            this.player = new PlayerSnake(this, this.snakesGroup, { x: 4, y: 2 }, 'white');
        }

        if(this.food.eaten){
            this.food.destroy();
            this.food = new Food(this, {x: Phaser.Math.Between(0,24), y: Phaser.Math.Between(0,24)});
        }
    }
}


