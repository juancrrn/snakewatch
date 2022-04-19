import Snake from '../entities/snake.js';

export default class PlayerSnake extends Snake {

  constructor(scene, snakesGroup, pos, texture) {
    super(scene, snakesGroup, pos, texture);
  }

  chooseNextMove() {}
}
