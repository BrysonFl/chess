package view.pieces;

import view.Board;
import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Square;

/**
 * Clase representativa de la pieza Peón. 
 * Gestiona las reglas de movimiento específicas del peón, incluyendo el avance 
 * de una o dos casillas desde su posición inicial, las capturas diagonales estándar 
 * y la regla especial de captura al paso (en passant).
 * 
 * @author brysonfl
 */
public class Pawn extends Piece {
    
    /**
     * Construye un nuevo peón con el color especificado.
     * 
     * @param isWhite Define si el peón es blanco ({@code true}) o negro ({@code false}).
     */
    public Pawn(boolean isWhite) {
        super(isWhite, PieceType.PAWN);
    }

    /**
     * Calcula y retorna una lista con todos los movimientos válidos y legales disponibles 
     * para el peón desde su posición actual en el tablero. Esto incluye avanzar hacia adelante 
     * (una o dos casillas si está en su fila inicial y el camino está libre), capturar piezas 
     * enemigas en diagonal, y la captura al paso cuando corresponde.
     * 
     * @param row   La fila actual en la que se encuentra el peón.
     * @param col   La columna actual en la que se encuentra el peón.
     * @param board El tablero actual del juego, utilizado para verificar el estado de las casillas y piezas.
     * @return Una lista de arreglos de enteros donde cada elemento representa las coordenadas 
     *         {@code [fila, columna]} de una casilla válida a la que el peón puede desplazarse.
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>();
        Square[][] squares = board.getSquares();
        
        int direction = isWhite() ? -1 : 1;
        int startRow = isWhite() ? 6 : 1;
        
        int targetRow = row + direction;
        
        if (UtilValidations.validateBoard(targetRow, col) && squares[targetRow][col].getPiece() == null) {
            movements.add(new Integer[] { targetRow, col });
            
            int doubleTargetRow = row + (2 * direction);
            
            if (row == startRow && UtilValidations.validateBoard(doubleTargetRow, col) && squares[doubleTargetRow][col].getPiece() == null) {
                movements.add(new Integer[]{ doubleTargetRow, col });
            }
        }
        
        int[] captureCols = new int[]{ col - 1, col + 1};
        
        for (int captureSingleCol : captureCols) {
            if (UtilValidations.validateBoard(targetRow, captureSingleCol)
                    && squares[targetRow][captureSingleCol].getPiece() != null
                    && squares[targetRow][captureSingleCol].getPiece().isWhite() != this.isWhite()) {
                movements.add(new Integer[]{ targetRow, captureSingleCol });
            }
        }
        
        int rowPassant = isWhite() ? 3 : 4;

        if (rowPassant == row) {
            for (int captureSingleCol : captureCols) {
                if (UtilValidations.validateBoard(targetRow, captureSingleCol) && row == rowPassant && squares[row][captureSingleCol].getPiece() != null
                        && squares[row][captureSingleCol].getPiece().isWhite() != this.isWhite()) {
                    movements.add(new Integer[]{ targetRow, captureSingleCol });
                }
            }
        }
        
        return movements;
    }
    
}
