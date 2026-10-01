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
    protected void validMovements() {
        
    }
    
}
