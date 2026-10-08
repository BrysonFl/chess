package view.pieces;

import view.Board;
import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Square;

/**
 *
 * @author brysonfl
 */
public class Pawn extends Piece {
    
    public Pawn(boolean isWhite) {
        super(isWhite, PieceType.PAWN);
    }

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
            if (squares[targetRow][captureSingleCol].getPiece() != null && squares[targetRow][captureSingleCol].getPiece().isWhite() != this.isWhite()) {
                movements.add(new Integer[]{ targetRow, captureSingleCol });
            }
        }
        
        int rowPassant = isWhite() ? 3 : 4;
        
        if (rowPassant == row) {
            for (int captureSingleCol : captureCols) {
                if (row == rowPassant && squares[row][captureSingleCol].getPiece() != null && squares[row][captureSingleCol].getPiece().isWhite() != this.isWhite()) {
                    movements.add(new Integer[]{ targetRow, captureSingleCol });
                }
            }
        }
        
        return movements;
    }
    
}
