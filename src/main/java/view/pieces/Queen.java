package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Board;

/**
 * Clase representativa de la pieza Reina. 
 * Gestiona y define el comportamiento, las características y el alcance de movimiento 
 * de la reina (la pieza más poderosa del tablero) dentro de la lógica del juego de ajedrez.
 * 
 * @author brysonfl
 */
public class Queen extends Piece {
    
    public Queen(boolean isWhite) {
        super(isWhite, PieceType.QUEEN);
    }

    /**
     * Calcula y retorna una lista con todos los movimientos válidos y legales disponibles 
     * para la reina desde su posición actual en el tablero. La reina puede moverse 
     * cualquier número de casillas desocupadas en línea recta en cualquier dirección 
     * (horizontal, vertical y diagonal).
     * 
     * @param row   La fila actual en la que se encuentra la reina.
     * @param col   La columna actual en la que se encuentra la reina.
     * @param board El tablero actual del juego, utilizado para verificar el estado de las casillas.
     * @return Una lista de arreglos de enteros donde cada elemento representa las coordenadas 
     *         {@code [fila, columna]} de una casilla válida a la que la reina puede desplazarse.
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>();
        
        int targetCol;
        targetCol = col;
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            targetCol++;
            
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        targetCol = col;
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        targetCol = col;
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            targetCol--;
            
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        targetCol = col;
        
        for (int targetRow = row + 1; targetRow < 8; targetRow++) {
            targetCol--;
            
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        targetCol = col;
        
        for (int targetRow = row + 1; targetRow < 8; targetRow++) {
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        targetCol = col;
        
        for (int targetRow = row + 1; targetRow < 8; targetRow++) {
            targetCol++;
            
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        for (int targetColFor = col + 1; targetColFor < 8; targetColFor++) {
            if (!UtilValidations.validateBoard(row, targetCol)) break;
            if (board.getSquares()[row][targetCol].getPiece() != null) break;
            
            movements.add(new Integer[]{ row, targetColFor });
        }
        
        for (int targetColFor = col - 1; targetColFor >= 0; targetColFor--) {
            if (!UtilValidations.validateBoard(row, targetCol)) break;
            if (board.getSquares()[row][targetCol].getPiece() != null) break;
            
            movements.add(new Integer[]{ row, targetColFor });
        }
        
        return movements;
    }
    
}
