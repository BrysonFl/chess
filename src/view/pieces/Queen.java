package view.pieces;

import enums.PieceType;

/**
 *
 * @author brysonfl
 */
public class Queen extends Piece {
    
    public Queen(boolean isWhite) {
        super(isWhite, PieceType.QUEEN);
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
