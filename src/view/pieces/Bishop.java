package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Board;

/**
 *  Representación en clase de la pieza alfil
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
        int targetColRight = col;
        int targetColLeft = col;
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            targetColRight++;
            
            if (!UtilValidations.validateBoard(targetRow, targetColRight)) break;
            if (board.getSquares()[targetRow][targetColRight].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, targetColRight });
        }
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            targetColLeft--;
            
            if (!UtilValidations.validateBoard(targetRow, targetColLeft)) break;
            if (board.getSquares()[targetRow][targetColLeft].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, targetColLeft });
        }
        
        targetColRight = col;
        targetColLeft = col;
        
        for (int targetRow = row + 1; targetRow <= 8; targetRow++) {
            targetColRight++;
            
            if (!UtilValidations.validateBoard(targetRow, targetColRight)) break;
            if (board.getSquares()[targetRow][targetColRight].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, targetColRight });
        }
        
        for (int targetRow = row + 1; targetRow <= 8; targetRow++) {
            targetColLeft--;
            
            if (!UtilValidations.validateBoard(targetRow, targetColLeft)) break;
            if (board.getSquares()[targetRow][targetColLeft].getPiece() != null) break;
            
            movements.add(new Integer[]{ targetRow, targetColLeft });
        }
        
        return movements;
    }
    
}
