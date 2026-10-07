package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import view.Board;

/**
 *
 * @author brysonfl
 */
public class Bishop extends Piece {
    
    public Bishop(boolean isWhite) {
        super(isWhite, PieceType.BISHOP);
    }

    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        return new ArrayList<>();
    }
    
}
