package view;

import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JButton;

import view.pieces.Piece;

/**
 *
 * @author brysonfl
 */
public class Square extends JButton {
    
    private Piece piece;
    
    private int row;
    private int col;
    
    public Square(int row, int col) {
        setOpaque(true);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setSelected(false);
        setFocusPainted(false);
        setBackground((row + col) %2 == 0 ? Color.BLACK : Color.WHITE);
        
        this.row = row;
        this.col = col;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        
        if (piece != null && piece.getIcon() != null) {
            this.setIcon(piece.getIcon());
        } else {
            this.setIcon(null);
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
    
}
