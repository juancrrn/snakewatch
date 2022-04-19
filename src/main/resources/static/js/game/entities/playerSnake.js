import Snake from '../entities/snake.js';

export default class PlayerSnake extends Snake {

  constructor(scene, snakesGroup, pos, texture) {
    super(scene, snakesGroup, pos, 2, texture);
  }

  chooseNextMove() {}
}
