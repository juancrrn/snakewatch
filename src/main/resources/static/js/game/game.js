import Boot from './scenes/boot.js';
import Level from './scenes/level.js'

/**
 * Inicio del juego en Phaser. Creamos el archivo de configuración del juego y creamos
 * la clase Game de Phaser, encargada de crear e iniciar el juego.
 */
let config = {
    type: Phaser.CANVAS,
    canvas: document.getElementById('canvas'),
    scale: {
        //autoCenter: Phaser.Scale.CENTER_BOTH,
        width: 500,
        height: 500,
        //mode: Phaser.Scale.FIT
    },
    dom: {
        createContainer: true
    },
    pixelArt: true,
    scene: [Boot, Level],
    physics: {
        default: 'arcade',
        arcade: {
            gravity: { y: 0 },
            debug: false
        }
    }
};

new Phaser.Game(config);
