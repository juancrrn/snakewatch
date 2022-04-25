/**
 * Scene that preloads the game assets
 */
 export default class Boot extends Phaser.Scene {
  
    constructor() {
      super({ key: 'boot' });
    }
  
    /**
     * Preload game assets
     */
    preload() {
      this.load.tilemapTiledJSON('tilemap', '/levelMaps/' + LEVEL + '.json');
      this.load.image('tileset', '/img/snake_tileset.png');
  
      this.load.spritesheet('red', '/img/red.png', { frameWidth: 20 });
      this.load.spritesheet('white', '/img/white.png', { frameWidth: 20 });
      this.load.image('pink', '/img/pink.png');
    }
  
    /**
     * Start the level scene
     */
    create() {
        this.scene.start('level');
    }
  }
  