package view.pieces.king;

import view.Square;

/**
 * Representa los datos estructurados de una jugada de enroque (corto o largo).
 * <p>
 * Este registro inmutable encapsula las casillas involucradas en el desplazamiento
 * de la torre y la notación algebraica oficial del movimiento ({@code "O-O"} o {@code "O-O-O"}),
 * permitiendo que el controlador ejecute la reubicación de las piezas en el tablero.
 * </p>
 *
 * @param rookOrigin la casilla de origen ({@link Square}) donde se encuentra inicialmente la torre.
 * @param rookTarget la casilla de destino ({@link Square}) a la que debe trasladarse la torre tras el enroque.
 * @param notation   la representación en notación algebraica del enroque ({@code "O-O"} para enroque corto,
 *                   {@code "O-O-O"} para enroque largo).
 * @author brysonfl
 * @see King
 * @see Square
 */
public record CastleMovement(Square rookOrigin, Square rookTarget, String notation) {}
