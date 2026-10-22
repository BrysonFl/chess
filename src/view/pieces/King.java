package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Board;

/**
 *
 * @author brysonfl
 */
public class King extends Piece {
    
    public King(boolean isWhite) {
        super(isWhite, PieceType.KING);
    }

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
