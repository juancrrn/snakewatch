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
    this.load.tilemapTiledJSON('tilemap', '/maps/' + LEVEL + '.json');
    this.load.image('tileset', '/img/snake_tileset.png');

    let directoryHeader = '/img/Skins/';
    for(let i=0; i<SKINS.length;i++){
      this.load.spritesheet(SKINS[i], directoryHeader + SKINS[i], { frameWidth: 20 });
    }

    this.load.image('pink', '/img/pink.png');
  }

  /**
   * Start the level scene
   */
  create() {
    // TODO: cargar level o spectator dependiendo de si es el propietario o espectador
    if(ADMIN){
      this.scene.start('host');
    }
    else{
      this.scene.start('spectator');
    }
  }
}
