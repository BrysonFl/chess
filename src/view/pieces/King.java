package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
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
        return new ArrayList<>();
    }
    
}
