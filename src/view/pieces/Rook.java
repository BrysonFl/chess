package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
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
        return new ArrayList<>();
    }
    
}
