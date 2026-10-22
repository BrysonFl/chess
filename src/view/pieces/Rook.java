package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Board;

/**
 * Clase representativa de la pieza Torre. 
 * Gestiona y define el comportamiento y las características propias de la torre 
 * dentro de la lógica del juego de ajedrez.
 * 
 * @author brysonfl
 */
public class Rook extends Piece {
    
    public Rook(boolean isWhite) {
        super(isWhite, PieceType.ROOK);
    }

    /**
     * Calcula y retorna una lista con todos los movimientos válidos y legales disponibles 
     * para la torre desde su posición actual en el tablero. La torre puede moverse 
     * cualquier número de casillas desocupadas en línea recta vertical u horizontal 
     * (hacia arriba, abajo, izquierda o derecha).
     * 
     * @param row   La fila actual en la que se encuentra la torre.
     * @param col   La columna actual en la que se encuentra la torre.
     * @param board El tablero actual del juego, utilizado para verificar el estado de las casillas.
     * @return Una lista de arreglos de enteros donde cada elemento representa las coordenadas 
     *         {@code [fila, columna]} de una casilla válida a la que la torre puede desplazarse.
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>();
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            if (!UtilValidations.validateBoard(targetRow, col)) break;
            if (board.getSquares()[targetRow][col].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, col });
        }
        
        for (int targetRow = row + 1; targetRow < 8; targetRow++) {
            if (!UtilValidations.validateBoard(targetRow, col)) break;
            if (board.getSquares()[targetRow][col].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, col });
        }
        
        for (int targetCol = col - 1; targetCol >= 0; targetCol--) {
            if (!UtilValidations.validateBoard(row, targetCol)) break;
            if (board.getSquares()[row][targetCol].getPiece() != null) break;
            
            movements.add(new Integer[]{ row, targetCol });
        }
        
        for (int targetCol = col + 1; targetCol < 8; targetCol++) {
            if (!UtilValidations.validateBoard(row, targetCol)) break;
            if (board.getSquares()[row][targetCol].getPiece() != null) break;
            
            movements.add(new Integer[]{ row, targetCol });
        }
        
        return movements;
    }
    
}
