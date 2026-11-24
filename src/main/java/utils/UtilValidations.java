package utils;

import view.Square;
import view.pieces.Pawn;
import view.pieces.Piece;

/**
 * Clase utilitaria que centraliza métodos estáticos de validación de coordenadas
 * del tablero y generación de notación algebraica para las jugadas de ajedrez.
 * <p>
 * Proporciona funciones auxiliares empleadas tanto por las piezas al calcular sus
 * desplazamientos como por el controlador principal ({@link controller.GameController})
 * para registrar movimientos y verificar posiciones válidas dentro de la cuadrícula de 8x8.
 * </p>
 *
 * <p>La notación generada es simplificada: omite la desambiguación y la marca de
 * captura para piezas distintas del peón. El controlador añade enroque, promoción,
 * jaque y mate. La conversión de coordenadas presupone una casilla válida y no nula.</p>
 *
 * @author brysonfl
 * @see Square
 * @see Piece
 */
public class UtilValidations {

    /**
     * Constructor por defecto de la clase utilitaria {@code UtilValidations}.
     */
    public UtilValidations() {
    }

    /**
     * Valida si un par de coordenadas (fila y columna) se encuentra dentro de los
     * límites permitidos de un tablero de ajedrez estándar de 8x8 (índices de {@code 0} a {@code 7}).
     *
     * @param row el índice de la fila a evaluar (donde {@code 0} es la fila superior y {@code 7} la inferior).
     * @param col el índice de la columna a evaluar (donde {@code 0} es la columna izquierda y {@code 7} la derecha).
     * @return {@code true} si tanto la fila como la columna están dentro del rango {@code [0, 7]};
     *         {@code false} en caso contrario.
     */
    public static boolean validateBoard(int row, int col) {
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }

    /**
     * Genera la representación en notación algebraica simplificada de un movimiento
     * hacia una casilla de destino determinada.
     * <p>
     * Si la pieza movida es un peón ({@link Pawn}), devuelve únicamente la coordenada
     * de destino (precedida por {@code "x"} si se produjo una captura normal o al paso).
     * Para el resto de las piezas, concatena el identificador del tipo de pieza
     * ({@link Piece#getPieceType()}) con la coordenada algebraica de la casilla destino.
     * </p>
     *
     * @param piece         la pieza que realiza el movimiento.
     * @param currentSquare la casilla de destino ({@link Square}) a la que llega la pieza.
     * @param capture       {@code true} si el movimiento implica la captura directa de una pieza rival
     *                      en la casilla de destino; {@code false} en caso contrario.
     * @param passant       {@code true} si el movimiento corresponde a una captura al paso (<i>en passant</i>);
     *                      {@code false} en caso contrario.
     * @return una cadena de texto con la notación de la jugada (por ejemplo, {@code "e4"}, {@code "xd5"} o {@code "Nf3"}).
     * @see #coordinate(Square)
     */
    public static String generateCoordinate(Piece piece, Square currentSquare, boolean capture, boolean passant) {
        if (piece instanceof Pawn) {
            if (capture || passant) {
                return "x" + coordinate(currentSquare);
            }

            return coordinate(currentSquare);
        }

        return piece.getPieceType() + coordinate(currentSquare);
    }

    /**
     * Convierte la posición matricial (fila y columna de {@code 0} a {@code 7}) de una
     * casilla en su coordenada algebraica estándar de ajedrez (columnas {@code 'a'} a {@code 'h'}
     * y filas {@code 1} a {@code 8}).
     *
     * @param square la casilla del tablero ({@link Square}) cuya coordenada se desea obtener.
     * @return una cadena de dos caracteres que representa la coordenada algebraica
     *         de la casilla (por ejemplo, {@code "a8"} para fila 0, columna 0, o {@code "e1"} para fila 7, columna 4).
     */
    public static String coordinate(Square square) {
        return "" + (char) ('a' + square.getCol()) + (8 - square.getRow());
    }

}
