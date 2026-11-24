package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Board;

/**
 * Representa la pieza de ajedrez Alfil ({@link PieceType#BISHOP}).
 * <p>
 * Esta clase extiende {@link Piece} e implementa la lógica de desplazamiento diagonal.
 * El alfil puede recorrer cualquier cantidad de casillas desocupadas en las cuatro
 * direcciones diagonales a partir de su posición actual, deteniéndose ante los límites
 * del tablero, una pieza aliada o tras capturar la primera pieza adversaria en su trayectoria.
 * </p>
 *
 * <p>Los destinos son pseudolegales: respetan desplazamientos y ocupación, pero
 * no comprueban el turno ni la seguridad del rey propio. El
 * {@link controller.GameController} realiza ese filtrado antes de permitir una jugada.</p>
 *
 * @author brysonfl
 * @see Piece
 * @see PieceType#BISHOP
 * @see Board
 */
public class Bishop extends Piece {
    
    /**
     * Construye una nueva instancia de {@code Bishop} con el color especificado.
     *
     * @param isWhite define si el alfil pertenece al bando blanco ({@code true})
     *                o negro ({@code false}).
     */
    public Bishop(boolean isWhite) {
        super(isWhite, PieceType.BISHOP);
    }

    /**
     * Calcula y retorna todos los movimientos diagonales válidos disponibles para el alfil
     * desde su posición actual en el tablero.
     * <p>
     * Explora de forma iterativa las cuatro diagonales (superior derecha, superior izquierda,
     * inferior derecha e inferior izquierda) verificando que las coordenadas se mantengan
     * dentro de los límites mediante {@link UtilValidations#validateBoard(int, int)}. Si
     * encuentra una casilla ocupada, evalúa si la pieza puede ser capturada con
     * {@link #canCapture(Piece)} e interrumpe la búsqueda en esa dirección.
     * </p>
     *
     * @param row   el índice de la fila actual (0 a 7) en la que se encuentra el alfil.
     * @param col   el índice de la columna actual (0 a 7) en la que se encuentra el alfil.
     * @param board el tablero actual del juego ({@link Board}), utilizado para consultar
     *              el estado de ocupación de cada casilla.
     * @return una lista de arreglos {@code Integer[]} donde cada elemento contiene las
     *         coordenadas {@code [fila, columna]} de una casilla válida de destino.
     * @see UtilValidations#validateBoard(int, int)
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>();
        int targetColRight = col;
        int targetColLeft = col;
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            targetColRight++;
            
            if (!UtilValidations.validateBoard(targetRow, targetColRight)) break;
            if (board.getSquares()[targetRow][targetColRight].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][targetColRight].getPiece())) {
                    movements.add(new Integer[]{ targetRow, targetColRight });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, targetColRight });
        }
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            targetColLeft--;
            
            if (!UtilValidations.validateBoard(targetRow, targetColLeft)) break;
            if (board.getSquares()[targetRow][targetColLeft].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][targetColLeft].getPiece())) {
                    movements.add(new Integer[]{ targetRow, targetColLeft });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, targetColLeft });
        }
        
        targetColRight = col;
        targetColLeft = col;
        
        for (int targetRow = row + 1; targetRow <= 8; targetRow++) {
            targetColRight++;
            
            if (!UtilValidations.validateBoard(targetRow, targetColRight)) break;
            if (board.getSquares()[targetRow][targetColRight].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][targetColRight].getPiece())) {
                    movements.add(new Integer[]{ targetRow, targetColRight });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, targetColRight });
        }
        
        for (int targetRow = row + 1; targetRow <= 8; targetRow++) {
            targetColLeft--;
            
            if (!UtilValidations.validateBoard(targetRow, targetColLeft)) break;
            if (board.getSquares()[targetRow][targetColLeft].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][targetColLeft].getPiece())) {
                    movements.add(new Integer[]{ targetRow, targetColLeft });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, targetColLeft });
        }
        
        return movements;
    }
    
}
