package view.pieces;

import enums.PieceType;

/**
 *
 * @author brysonfl
 */
public class Pawn extends Piece {
    
    public Pawn(boolean isWhite) {
        super(isWhite, PieceType.PAWN);
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
