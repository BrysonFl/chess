package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Board;

/**
 * Clase representativa de la pieza Caballo. 
 * Gestiona los movimientos en forma de "L" característicos del caballo, 
 * evaluando las 8 posibles posiciones en las que puede saltar dentro del tablero.
 * 
 * @author brysonfl
 */
public class Knight extends Piece {
    
    /**
     * Construye un nuevo caballo con el color especificado.
     * 
     * @param isWhite Define si el caballo es blanco ({@code true}) o negro ({@code false}).
     */
    public Knight(boolean isWhite) {
        super(isWhite, PieceType.KNIGHT);
    }

    /**
     * Calcula y retorna una lista con todos los movimientos válidos y legales disponibles 
     * para el caballo desde su posición actual en el tablero. El caballo se mueve en "L" 
     * (dos casillas en una dirección y una perpendicular, o viceversa) y tiene la capacidad 
     * de saltar por encima de otras piezas.
     * 
     * @param row   La fila actual en la que se encuentra el caballo.
     * @param col   La columna actual en la que se encuentra el caballo.
     * @param board El tablero actual del juego, utilizado para verificar el estado de las casillas.
     * @return Una lista de arreglos de enteros donde cada elemento representa las coordenadas 
     *         {@code [fila, columna]} de una casilla válida a la que el caballo puede desplazarse.
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>();
        
        if (UtilValidations.validateBoard(row - 2, col + 1) && board.getSquares()[row - 2][col + 1].getPiece() == null) {
            movements.add(new Integer[]{ row - 2, col + 1 });
        }
        
        if (UtilValidations.validateBoard(row - 2, col - 1) && board.getSquares()[row - 2][col - 1].getPiece() == null) {
            movements.add(new Integer[]{ row - 2, col - 1 });
        }
        
        if (UtilValidations.validateBoard(row + 2, col + 1) && board.getSquares()[row + 2][col + 1].getPiece() == null) {
            movements.add(new Integer[]{ row + 2, col + 1 });
        }
        
        if (UtilValidations.validateBoard(row + 2, col - 1) && board.getSquares()[row + 2][col - 1].getPiece() == null) {
            movements.add(new Integer[]{ row + 2, col - 1 });
        }
        
        if (UtilValidations.validateBoard(row - 1, col + 2) && board.getSquares()[row - 1][col + 2].getPiece() == null) {
            movements.add(new Integer[]{ row - 1, col + 2 });
        }
        
        if (UtilValidations.validateBoard(row - 1, col - 2) && board.getSquares()[row - 1][col - 2].getPiece() == null) {
            movements.add(new Integer[]{ row - 1, col - 2 });
        }
        
        if (UtilValidations.validateBoard(row + 1, col + 2) && board.getSquares()[row + 1][col + 2].getPiece() == null) {
            movements.add(new Integer[]{ row + 1, col + 2 });
        }
        
        if (UtilValidations.validateBoard(row + 1, col - 2) && board.getSquares()[row + 1][col - 2].getPiece() == null) {
            movements.add(new Integer[]{ row + 1, col - 2 });
        }
        
        return movements;
    }
    
}
