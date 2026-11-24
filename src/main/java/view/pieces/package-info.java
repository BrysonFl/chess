/**
 * Modela las piezas, sus iconos y sus movimientos pseudolegales.
 *
 * <p>{@link view.pieces.Piece} define el color, el tipo y la marca de movimiento.
 * Los destinos se expresan como pares [fila, columna] con índices de 0 a 7
 * en listas modificables. Se respetan ocupación y geometría, pero el controlador
 * comprueba turno y seguridad del rey. Los recursos se buscan en /pieces/.</p>
 */
package view.pieces;
