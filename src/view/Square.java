package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
    
    private boolean highlight = false;
    
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

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
        repaint();
    }

    public boolean isHighlight() {
        return highlight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (highlight) {
            Graphics2D g2D = (Graphics2D) g.create();
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setColor(new Color(0, 0, 0, 85));
            
            int diameter = Math.min(getWidth(), getHeight() / 4);
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;
            
            g2D.fillOval(x, y, diameter, diameter);
            g2D.dispose();
        }
    }

    @Override
    public String toString() {
        return "Square{" + "piece=" + piece + ", row=" + row + ", col=" + col + '}';
    }
    
}
