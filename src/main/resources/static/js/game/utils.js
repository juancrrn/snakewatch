/**
 * Número aleatorio entre min (incluido) y max (excluido)
 */
 function getRandom(min, max) {
    return Math.random() * (max - min) + min;
}

/**
 * Distancia entre los puntos (x1,y1) and (x2,y2)
 */
function dist(x1, y1, x2, y2) {
    return Math.sqrt((Math.pow((x1 - x2), 2)) + (Math.pow((y1 - y2), 2)))
}

/**
 * Parse the array of colors to a string format
 * 
 * @param {Array} color Array of colors in rgb mode. Example [255, 255, 0]
 * @returns canvas string expression to be used by canvas.context.fillStyle
 */
function parseColor(color){
    return "rgba(" + color[0] + ", " + color[1] + ", " + color[2] + ")";
}