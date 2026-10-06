package view.pieces;

import enums.PieceType;

/**
 *
 * @author brysonfl
 */
public class Knight extends Piece {
    
    public Knight(boolean isWhite) {
        super(isWhite, PieceType.KNIGHT);
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
