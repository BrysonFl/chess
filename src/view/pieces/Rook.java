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
public class Rook extends Piece {
    
    public Rook(boolean isWhite) {
        super(isWhite, PieceType.ROOK);
    }

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
