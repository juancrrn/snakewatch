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

    this.load.image('red', '/img/red.png');
    this.load.image('pink', '/img/pink.png');
    this.load.image('white', '/img/white.png');
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
