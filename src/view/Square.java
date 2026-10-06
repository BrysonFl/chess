package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
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
        setSelected(false);
        setFocusPainted(false);
        setBackground((row + col) %2 == 0 ? new Color(0xF0D9B5) : new Color(0xB58863));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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

    @Override
    protected void paintComponent(Graphics g) {
        
        
        super.paintComponent(g);
    }
    
    public void resetBackground() {
        setBackground((row + col) %2 == 0 ? new Color(0xF0D9B5) : new Color(0xB58863));
    }

    @Override
    public String toString() {
        return "Square{" + "piece=" + piece + ", row=" + row + ", col=" + col + '}';
    }
    
}
