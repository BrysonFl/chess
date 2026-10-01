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
    protected void validMovements() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
