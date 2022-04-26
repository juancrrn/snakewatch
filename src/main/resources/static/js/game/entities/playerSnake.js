import Snake from '../entities/snake.js';

export default class PlayerSnake extends Snake {

  constructor(scene, snakesGroup, pos, texture, username) {
    super(scene, snakesGroup, pos, 2, texture, username);
  }

  chooseNextMove() {}
}
