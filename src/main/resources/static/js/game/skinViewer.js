let savedSkin = PLAYERSKIN;
let skinNameH = document.getElementById("skinName");
skinNameH.innerHTML = PLAYERSKIN.split('.')[0];

const game = new Phaser.Game({
    type: Phaser.CANVAS,
    parent: document.getElementById('game-container'),
    canvas: document.getElementById('canvas'),
    scale: {
        mode: Phaser.Scale.CENTER_BOTH,
        width: 200,
        height: 80,
        fullscreenTarget: 'canvas'
    },
    backgroundColor: '#E6FFFF',
    pixelArt: true,
    scene: {
        preload: preload,
        create: create,
        update: update
    },
    physics: {
        default: 'arcade',
        arcade: {
            gravity: { y: 0 },
            debug: false
        }
    }
});

function preload() {
    for (let i = 0; i < SKINS.length; i++) {
        this.load.spritesheet(SKINS[i].split('.')[0], '/img/Skins/' + SKINS[i], { frameWidth: 20, frameHeight: 20 });
    }
}

function create() {
    this.parts = []
    let selected = PLAYERSKIN.split('.')[0];

    this.parts.push(this.add.sprite(70, 40, selected, 0));
    this.parts.push(this.add.sprite(50, 40, selected, 1));
    this.parts.push(this.add.sprite(30, 40, selected, 1));
    this.parts.push(this.add.sprite(10, 40, selected, 3));

    this.parts.forEach((part) => part.setAngle(90), this);

    let timer = this.time.addEvent({
        delay: 1500,
        callback: () => this.parts[0].setFrame(this.parts[0].frame.name === 0 ? 4 : 0),
        callbackScope: this,
        loop: true
    })

    this.registry.set('selectedSkin', PLAYERSKIN);
    this.registry.events.on('changedata-selectedSkin', () => updateSkin(game.registry.values.selectedSkin.split('.')[0], this.parts), this);
}

function update() {
    moveSnake(this.parts, 2);
}

function moveSnake(snake, speed) {
    snake.forEach((part) => {
        part.x += speed;
        if((part.x) > game.scale.width + 10) {
            part.x = -10;
        }
    }, this);
}

function changeSkin(skin = savedSkin) {
    game.registry.values.selectedSkin = skin;
    skinNameH.innerHTML = skin.split('.')[0];
}

function updateSkin(skin, parts) {
    parts.forEach((part) => {
        part.setTexture(skin, part.frame.name)
    }, this);
}

function saveSkin(skin) {
    go("/skins/change_skin/" + skin, "POST").then((d) => {
        savedSkin = d.skin;
        changeSkin();
    }).catch((e) => {
        console.log("Error: ", e);
    })
}