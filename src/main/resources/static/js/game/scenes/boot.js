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
    this.load.tilemapTiledJSON('tilemap', '/maps/demo.json');
    this.load.image('tileset', '/img/snake_tileset.png');

    this.load.spritesheet('red', '/img/red.png', { frameWidth: 20 });
    this.load.spritesheet('white', '/img/white.png', { frameWidth: 20 });
    this.load.image('pink', '/img/pink.png');
  }

  /**
   * Start the level scene
   */
  create() {
    // TODO: cargar level o spectator dependiendo de si es el propietario o espectador
    
    if(ADMIN){
      this.scene.start('level');
    }
    else{
      this.scene.start('spectator');
    }
  }
}
