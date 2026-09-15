package chess;

import pieces.Piece;

import javax.swing.*;
import java.awt.*;

public class Square extends JButton {

    private Piece piece;

    private static int row;
    private static int col;

    public Square() {
        setBorderPainted(false);
        setOpaque(true);
        setContentAreaFilled(true);
        setFocusPainted(false);

        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
    }

    public void setPiece(Piece piece) {
        this.piece = piece;

        if (piece != null) {
            this.setIcon(piece.getImage());
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    public Piece getPiece() {
        return piece;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        Square.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        Square.col = col;
    }
}
