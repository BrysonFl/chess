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
    protected void validMovements() {
        int[] openMovements = { 1, 2 };
        int[] captureMovements = {  };
    }
    
}
