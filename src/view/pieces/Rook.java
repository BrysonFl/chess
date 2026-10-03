package view.pieces;

import enums.PieceType;

/**
 *
 * @author brysonfl
 */
public class Rook extends Piece {
    
    public Rook(boolean isWhite) {
        super(isWhite, PieceType.ROOK);
    }

    @Override
    public int[][] validMovements() {
        return new int[][]{
            { 1, 0 },
            { 2, 0 },
            { 1, 1 },
            { 1, -1 }
        };
    }
    
}
