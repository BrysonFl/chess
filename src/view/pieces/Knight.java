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
public class Knight extends Piece {
    
    public Knight(boolean isWhite) {
        super(isWhite, PieceType.KNIGHT);
    }

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
