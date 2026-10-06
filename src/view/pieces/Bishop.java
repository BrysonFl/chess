package view.pieces;

import enums.PieceType;

/**
 *
 * @author brysonfl
 */
public class Bishop extends Piece {
    
    public Bishop(boolean isWhite) {
        super(isWhite, PieceType.BISHOP);
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
