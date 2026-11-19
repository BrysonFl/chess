package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import utils.UtilValidations;
import view.Board;
import view.Square;

/**
 * Clase representativa de la pieza Rey. 
 * Gestiona los movimientos permitidos para el rey, evaluando las 8 casillas 
 * adyacentes a su posición actual en el tablero (hacia arriba, abajo, izquierda, 
 * derecha y en diagonal, una casilla a la vez).
 * 
 * @author brysonfl
 */
public class King extends Piece {

    private static final int[][] OFFSETS = {
        {1, 0}, {1, -1}, {1, 1},
        {-1, 0}, {-1, 1}, {-1, -1},
        {0, 1}, {0, -1}
    };
    
    /**
     * Construye un nuevo rey con el color especificado.
     * 
     * @param isWhite Define si el rey es blanco ({@code true}) o negro ({@code false}).
     */
    public King(boolean isWhite) {
        super(isWhite, PieceType.KING);
    }

    /**
     * Calcula y retorna una lista con los destinos permitidos por las reglas de movimiento 
     * para el rey desde su posición actual en el tablero. El rey puede moverse una única 
     * casilla en cualquier dirección (horizontal, vertical o diagonal) siempre que esté 
     * dentro de los límites del tablero y el destino esté vacío o contenga una pieza capturable.
     * El controlador filtra los movimientos que dejan al rey en jaque y añade los enroques.
     * 
     * @param row   La fila actual en la que se encuentra el rey.
     * @param col   La columna actual en la que se encuentra el rey.
     * @param board El tablero actual del juego, utilizado para verificar el estado de las casillas.
     * @return Una lista de arreglos de enteros donde cada elemento representa las coordenadas 
     *         {@code [fila, columna]} de una casilla válida a la que el rey puede desplazarse.
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

    /** Datos del enroque; el controlador se encarga de mover las piezas. */
    public record CastleMovement(Square rookOrigin, Square rookTarget, String notation) {}

    /** Identifica un enroque entre destinos que ya fueron validados. */
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
     * Valida el historial, el camino libre y la seguridad del rey durante el enroque.
     * Las consultas de jaque y simulación se reciben del controlador.
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

        return safeDestination.test(board.getSquares()[origin.getRow()][4 + step])
                && safeDestination.test(board.getSquares()[origin.getRow()][4 + 2 * step]);
    }
}
