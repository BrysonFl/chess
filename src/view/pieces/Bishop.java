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
public class Bishop extends Piece {
    
    public Bishop(boolean isWhite) {
        super(isWhite, PieceType.BISHOP);
    }

    /**
     * Esta implementación valida las casillas en donde el alfil puede moverse
     * @param row
     * @param col
     * @param board
     * @return Los movimientos válidos para el alfil
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>();
        int targetCol = col;
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            targetCol++;
            
            if (UtilValidations.validateBoard(targetRow, targetCol) && board.getSquares()[targetRow][targetCol].getPiece() == null) {
                movements.add(new Integer[]{ targetRow, targetCol });
            } else {
                break;
            }
        }
        
        return movements;
    }
    
}
