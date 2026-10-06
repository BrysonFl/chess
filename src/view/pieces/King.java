package view.pieces;

import enums.PieceType;

/**
 *
 * @author brysonfl
 */
public class King extends Piece {
    
    public King(boolean isWhite) {
        super(isWhite, PieceType.KING);
    }

    @Override
    public int[][] validMovements(int row, int col) {
        return new int[][]{
            { 1, 0 },
            { 2, 0 },
            { 1, 1 },
            { 1, -1 }
        };
    }
    
}
