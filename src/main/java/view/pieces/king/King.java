package view.pieces.king;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import utils.UtilValidations;
import view.Board;
import view.Square;
import view.pieces.Piece;
import view.pieces.Rook;

/**
 * Representa la pieza Rey dentro del juego de ajedrez.
 * <p>
 * Esta clase extiende {@link Piece} y gestiona tanto los desplazamientos estándar
 * del rey hacia cualquiera de las 8 casillas adyacentes (horizontal, vertical y diagonal)
 * como la validación y planificación del movimiento especial de enroque junto con una
 * {@link Rook} del mismo bando.
 * </p>
 *
 * <p>La generación de destinos adyacentes y la comprobación del enroque son operaciones
 * separadas. La seguridad del rey se evalúa mediante el controlador y los predicados
 * proporcionados a {@link #canCastle}.</p>
 *
 * @author brysonfl
 * @see Piece
 * @see CastleMovement
 * @see Rook
 */
public class King extends Piece {

    /**
     * Desplazamientos relativos de fila y columna hacia las 8 casillas adyacentes al rey.
     */
    private static final int[][] OFFSETS = {
        {1, 0}, {1, -1}, {1, 1},
        {-1, 0}, {-1, 1}, {-1, -1},
        {0, 1}, {0, -1}
    };
    
    /**
     * Construye una nueva instancia de un rey con el color especificado.
     *
     * @param isWhite indica si el rey pertenece al bando de las blancas ({@code true})
     *                o al de las negras ({@code false}).
     */
    public King(boolean isWhite) {
        super(isWhite, PieceType.KING);
    }

    /**
     * Calcula y retorna una lista con los destinos básicos permitidos por las reglas de
     * movimiento del rey desde su posición actual en el tablero.
     * <p>
     * El rey puede desplazarse una única casilla en cualquier dirección (horizontal,
     * vertical o diagonal) siempre que la casilla objetivo se encuentre dentro de los
     * límites del tablero y esté vacía o contenga una pieza enemiga capturable.
     * El controlador de juego filtra posteriormente los movimientos que dejarían al rey
     * en jaque y agrega las opciones de enroque disponibles.
     * </p>
     *
     * @param row   la fila actual en la que se encuentra el rey (de {@code 0} a {@code 7}).
     * @param col   la columna actual en la que se encuentra el rey (de {@code 0} a {@code 7}).
     * @param board el tablero actual del juego ({@link Board}), utilizado para consultar
     *              el estado de las casillas adyacentes.
     * @return una lista de arreglos {@code Integer[]} donde cada elemento contiene las coordenadas
     *         {@code [fila, columna]} de una casilla destino válida para el rey.
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>(8);

        for (int[] offset : OFFSETS) {
            int targetRow = row + offset[0];
            int targetCol = col + offset[1];

            if (!UtilValidations.validateBoard(targetRow, targetCol)) {
                continue;
            }

            Piece targetPiece = board.getSquares()[targetRow][targetCol].getPiece();

            if (targetPiece == null || canCapture(targetPiece)) {
                movements.add(new Integer[]{targetRow, targetCol});
            }
        }

        return movements;
    }

    /**
     * Identifica si el desplazamiento entre dos casillas corresponde a una jugada de enroque
     * y construye el plan de movimiento asociado para la torre.
     * <p>
     * Verifica que el movimiento ocurra en la misma fila, partiendo desde la columna central
     * del rey (columna {@code 4}) hacia la columna de destino de enroque corto ({@code 6})
     * o enroque largo ({@code 2}).
     * </p>
     *
     * @param origin la casilla de origen ({@link Square}) desde la cual se mueve el rey.
     * @param target la casilla de destino ({@link Square}) hacia la cual se desplaza el rey.
     * @param board  el tablero actual ({@link Board}) del que se obtienen las casillas de la torre.
     * @return una instancia de {@link CastleMovement} con las casillas de origen y destino de la torre
     *         y su notación correspondiente, o {@code null} si el movimiento no es un enroque.
     */
    public CastleMovement castleMovement(Square origin, Square target, Board board) {
        if (origin.getRow() != target.getRow() || origin.getCol() != 4
                || (target.getCol() != 2 && target.getCol() != 6)) return null;

        boolean kingSide = target.getCol() == 6;

        return new CastleMovement(
                board.getSquares()[origin.getRow()][kingSide ? 7 : 0],
                board.getSquares()[origin.getRow()][kingSide ? 5 : 3],
                kingSide ? "O-O" : "O-O-O");
    }

    /**
     * Evalúa si el rey cumple todas las condiciones reglamentarias para ejecutar un enroque
     * con la torre ubicada en la columna indicada.
     * <p>
     * Comprueba los siguientes requisitos:
     * </p>
     * <ul>
     *   <li>La columna de la torre debe ser {@code 0} (largo) o {@code 7} (corto).</li>
     *   <li>El rey debe encontrarse en su casilla inicial, no haberse movido antes y no estar en jaque.</li>
     *   <li>La pieza en la esquina correspondiente debe ser una {@link Rook} del mismo color que tampoco se haya movido.</li>
     *   <li>Todas las casillas intermedias entre el rey y la torre deben estar vacías.</li>
     *   <li>Tanto la casilla de paso como la casilla final del rey deben ser seguras (no atacadas por el oponente).</li>
     * </ul>
     *
     * @param origin          la casilla actual ({@link Square}) donde se ubica el rey.
     * @param rookCol         la columna donde se encuentra la torre con la que se desea enrocar ({@code 0} o {@code 7}).
     * @param board           el tablero de juego ({@link Board}) para verificar las piezas y casillas intermedias.
     * @param inCheck         predicado provisto por el controlador que evalúa si el rey se encuentra actualmente en jaque.
     * @param safeDestination predicado provisto por el controlador que simula y verifica si una casilla de paso o destino
     *                        es segura para el rey.
     * @return {@code true} si el enroque hacia el flanco indicado es completamente legal;
     *         {@code false} en caso contrario.
     */
    public boolean canCastle(Square origin, int rookCol, Board board, Predicate<Square> inCheck, Predicate<Square> safeDestination) {
        if (rookCol != 0 && rookCol != 7) return false;

        if (origin.getPiece() != this || hasMoved() || origin.getCol() != 4
                || origin.getRow() != (isWhite() ? 7 : 0) || inCheck.test(origin)) return false;

        Piece rook = board.getSquares()[origin.getRow()][rookCol].getPiece();

        if (!(rook instanceof Rook) || rook.hasMoved() || rook.isWhite() != isWhite()) return false;

        int step = rookCol == 7 ? 1 : -1;

        for (int col = 4 + step; col != rookCol; col += step) {
            if (board.getSquares()[origin.getRow()][col].getPiece() != null) return false;
        }

        return safeDestination.test(board.getSquares()[origin.getRow()][4 + step]) && safeDestination.test(board.getSquares()[origin.getRow()][4 + 2 * step]);
    }
}
