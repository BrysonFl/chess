package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Board;

/**
 * Clase representativa de la pieza Rey. 
 * Gestiona los movimientos permitidos para el rey, evaluando las 8 casillas 
 * adyacentes a su posición actual en el tablero (hacia arriba, abajo, izquierda, 
 * derecha y en diagonal, una casilla a la vez).
 * 
 * @author brysonfl
 */
public class King extends Piece {
    
    /**
     * Construye un nuevo rey con el color especificado.
     * 
     * @param isWhite Define si el rey es blanco ({@code true}) o negro ({@code false}).
     */
    public King(boolean isWhite) {
        super(isWhite, PieceType.KING);
    }

    /**
     * Calcula y retorna una lista con todos los movimientos válidos y legales disponibles 
     * para el rey desde su posición actual en el tablero. El rey puede moverse una única 
     * casilla en cualquier dirección (horizontal, vertical o diagonal) siempre que esté 
     * dentro de los límites del tablero y la casilla de destino esté desocupada.
     * 
     * @param row   La fila actual en la que se encuentra el rey.
     * @param col   La columna actual en la que se encuentra el rey.
     * @param board El tablero actual del juego, utilizado para verificar el estado de las casillas.
     * @return Una lista de arreglos de enteros donde cada elemento representa las coordenadas 
     *         {@code [fila, columna]} de una casilla válida a la que el rey puede desplazarse.
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>();
        
        int upRow = row + 1;
        
        if (UtilValidations.validateBoard(row + 1, col) && board.getSquares()[row + 1][col].getPiece() == null) {
            movements.add(new Integer[]{ row + 1, col });
        }
        
        if (UtilValidations.validateBoard(row + 1, col - 1) && board.getSquares()[row + 1][col - 1].getPiece() == null) {
            movements.add(new Integer[]{ row + 1, col - 1 });
        }
        
        if (UtilValidations.validateBoard(row + 1, col + 1) && board.getSquares()[row + 1][col + 1].getPiece() == null) {
            movements.add(new Integer[]{ row + 1, col + 1 });
        }
        
        if (UtilValidations.validateBoard(row - 1, col) && board.getSquares()[row - 1][col].getPiece() == null) {
            movements.add(new Integer[]{ row - 1, col });
        }
        
        if (UtilValidations.validateBoard(row - 1, col + 1) && board.getSquares()[row - 1][col + 1].getPiece() == null) {
            movements.add(new Integer[]{ row - 1, col + 1 });
        }
        
        if (UtilValidations.validateBoard(row - 1, col - 1) && board.getSquares()[row - 1][col - 1].getPiece() == null) {
            movements.add(new Integer[]{ row - 1, col - 1 });
        }
        
        if (UtilValidations.validateBoard(row, col + 1) && board.getSquares()[row][col + 1].getPiece() == null) {
            movements.add(new Integer[]{ row, col + 1 });
        }
        
        if (UtilValidations.validateBoard(row, col - 1) && board.getSquares()[row][col - 1].getPiece() == null) {
            movements.add(new Integer[]{ row, col - 1 });
        }
        
        return movements;
    }
    
}
