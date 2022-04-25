import Boot from './scenes/boot.js';
import Level from './scenes/level.js';
import Spectator from'./scenes/spectator.js';
/**
 * Inicio del juego en Phaser. Creamos el archivo de configuración del juego y creamos
 * la clase Game de Phaser, encargada de crear e iniciar el juego.
 */

let config = {
    type: Phaser.CANVAS,
    canvas: document.getElementById('canvas'),
    scale: {
        width: 500,
        height: 500,
        fullscreenTarget: 'canvas',
    },
    backgroundColor: '#82bb4d',
    pixelArt: true,
    scene: [Boot, Level, Spectator],
    physics: {
        default: 'arcade',
        arcade: {
            gravity: { y: 0 },
            debug: false
        }
    } 
};

new Phaser.Game(config);
