package chess;

import pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Square extends JButton {

    private Piece piece;

    private int row;
    private int col;

    private boolean isLifted = false;

    private String rowNumber = "";
    private String colNumber = "";

    private boolean isPossible;

    public Square() {
        setBorderPainted(false);
        setOpaque(true);
        setContentAreaFilled(true);
        setFocusPainted(false);

        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (getIcon() != null) {
                    isLifted = true;
                    setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isLifted = false;
                setBorder(BorderFactory.createEmptyBorder());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Square currentSquare = (Square) e.getSource();

                setPiece(currentSquare.piece);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setFont(new Font("SansSerif", Font.BOLD, 12));

        if (!rowNumber.isEmpty())
            graphics2D.drawString(rowNumber, 4, 14);

        if (!colNumber.isEmpty())
            graphics2D.drawString(colNumber, getWidth() - 12, getHeight() - 5);

        if (isPossible) {
            graphics2D.setColor(new Color(0, 0, 0, 90));

            int diameter = 20;

            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;

            graphics2D.fillOval(x, y, diameter, diameter);
        }
    }

    public void updateCursor() {
        if (this.getPiece() != null || this.isPossible) {
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (piece != null) this.setIcon(piece.getImage());

        updateCursor();
    }

    public Piece getPiece() {
        return piece;
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

    public void setPossible(boolean possible) {
        isPossible = possible;
        updateCursor();
        repaint();
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setCoordinates(String rowNumber, String colNumber) {
        this.rowNumber = rowNumber;
        this.colNumber = colNumber;
    }

    @Override
    public String toString() {
        return "Square{" +
                "piece=" + piece +
                ", row=" + row +
                ", col=" + col +
                '}';
    }

}
