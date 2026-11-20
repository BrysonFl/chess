package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Board;

/**
 * Representa la pieza de ajedrez Torre ({@link PieceType#ROOK}).
 * <p>
 * Esta clase extiende {@link Piece} e implementa el desplazamiento ortogonal sobre el
 * tablero. La torre puede moverse cualquier cantidad de casillas libres en línea recta
 * horizontal o vertical (arriba, abajo, izquierda o derecha), y participa junto con el
 * rey en la jugada especial de enroque si aún no se ha movido ({@link #hasMoved()}).
 * </p>
 *
 * @author brysonfl
 * @see Piece
 * @see PieceType#ROOK
 * @see Board
 */
public class Rook extends Piece {
    
    /**
     * Construye una nueva instancia de {@code Rook} con el color especificado.
     *
     * @param isWhite define si la torre pertenece al bando blanco ({@code true})
     *                o negro ({@code false}).
     */
    public Rook(boolean isWhite) {
        super(isWhite, PieceType.ROOK);
    }

    /**
     * Calcula y retorna todos los movimientos ortogonales válidos disponibles para la torre
     * desde su posición actual en el tablero.
     * <p>
     * Recorre las cuatro direcciones rectilíneas (hacia arriba, hacia abajo, hacia la izquierda
     * y hacia la derecha) validando que cada coordenada permanezca dentro del tablero con
     * {@link UtilValidations#validateBoard(int, int)}. Si halla una pieza en el camino,
     * comprueba si es capturable mediante {@link #canCapture(Piece)} y detiene la exploración
     * en ese sentido.
     * </p>
     *
     * @param row   el índice de la fila actual (0 a 7) en la que se encuentra la torre.
     * @param col   el índice de la columna actual (0 a 7) en la que se encuentra la torre.
     * @param board el tablero actual del juego ({@link Board}), utilizado para verificar
     *              el estado de ocupación de las casillas.
     * @return una lista de arreglos {@code Integer[]} donde cada elemento contiene las
     *         coordenadas {@code [fila, columna]} de una casilla válida de destino.
     * @see UtilValidations#validateBoard(int, int)
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>();
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            if (!UtilValidations.validateBoard(targetRow, col)) break;
            if (board.getSquares()[targetRow][col].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][col].getPiece())) {
                    movements.add(new Integer[]{ targetRow, col });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, col });
        }
        
        for (int targetRow = row + 1; targetRow < 8; targetRow++) {
            if (!UtilValidations.validateBoard(targetRow, col)) break;
            if (board.getSquares()[targetRow][col].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][col].getPiece())) {
                    movements.add(new Integer[]{ targetRow, col });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, col });
        }
        
        for (int targetCol = col - 1; targetCol >= 0; targetCol--) {
            if (!UtilValidations.validateBoard(row, targetCol)) break;
            if (board.getSquares()[row][targetCol].getPiece() != null) {
                if (canCapture(board.getSquares()[row][targetCol].getPiece())) {
                    movements.add(new Integer[]{ row, targetCol });
                }
                break;
            }
            
            movements.add(new Integer[]{ row, targetCol });
        }
        
        for (int targetCol = col + 1; targetCol < 8; targetCol++) {
            if (!UtilValidations.validateBoard(row, targetCol)) break;
            if (board.getSquares()[row][targetCol].getPiece() != null) {
                if (canCapture(board.getSquares()[row][targetCol].getPiece())) {
                    movements.add(new Integer[]{ row, targetCol });
                }
                break;
            }
            
            movements.add(new Integer[]{ row, targetCol });
        }
        
        return movements;
    }
    
}
