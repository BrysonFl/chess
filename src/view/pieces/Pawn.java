package view.pieces;

import view.Board;
import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;

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
        
        int direction = isWhite() ? -1 : 1;
        int startRow = isWhite() ? 6 : 1;
        
        int targetRow = row + direction;
        
        if (UtilValidations.validateBoard(targetRow, col)) {
            movements.add(new Integer[] { targetRow, col });
            
            int doubleTargetRow = row + (2 * direction);
            
            if (row == startRow && UtilValidations.validateBoard(doubleTargetRow, col)) {
                movements.add(new Integer[]{ doubleTargetRow, col });
            }
        }
        
        return movements;
    }
    
}
