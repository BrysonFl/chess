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
    public int[][] validMovements(int row, int col) {
        return new int[][]{
            { row - 1, col },
            { row - 2, col },
            /*{ 1, col + 1 },
            { 1, col - 1 }*/
        };
    }
    
}
