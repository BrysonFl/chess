package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
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
        return new ArrayList<>();
    }
    
}
