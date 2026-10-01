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
    protected void validMovements() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
