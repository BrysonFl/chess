package chess;

import pieces.*;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {

    private final Square[][] squares = new Square[8][8];

    public Board() {
        setLayout(new GridLayout(8, 8, 0, 0));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 800));

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = new Square();
                square.setRow(row);
                square.setCol(col);

                if ((row + col) % 2 == 0) {
                    square.setBackground(new Color(238, 238, 210));
                } else {
                    square.setBackground(new Color(118, 150, 86));
                }

                squares[row][col] = square;

                int currentRow = row;
                int currentCol = col;

                square.addActionListener(e -> movement(square, currentRow, currentCol));
                add(square);
            }
        }

        initializeWhitePieces();
        initializeBlackPieces();
    }

    private void initializeWhitePieces() {
        for (int i = 0; i < 8; i++) {
            squares[squares.length - 2][i].setPiece(new Pawn("white"));
        }

        squares[squares.length - 1][0].setPiece(new Rook("white"));
        squares[squares.length - 1][7].setPiece(new Rook("white"));

        squares[squares.length - 1][1].setPiece(new Knight("white"));
        squares[squares.length - 1][6].setPiece(new Knight("white"));

        squares[squares.length - 1][2].setPiece(new Bishop("white"));
        squares[squares.length - 1][5].setPiece(new Bishop("white"));

        squares[squares.length - 1][3].setPiece(new Queen("white"));
        squares[squares.length - 1][4].setPiece(new King("white"));
    }

    private void initializeBlackPieces() {
        for (int i = 0; i < 8; i++) {
            squares[1][i].setPiece(new Pawn("black"));
        }

        squares[0][0].setPiece(new Rook("black"));
        squares[0][7].setPiece(new Rook("black"));

        squares[0][1].setPiece(new Knight("black"));
        squares[0][6].setPiece(new Knight("black"));

        squares[0][2].setPiece(new Bishop("black"));
        squares[0][5].setPiece(new Bishop("black"));

        squares[0][3].setPiece(new Queen("black"));
        squares[0][4].setPiece(new King("black"));
    }

    private void movement(Square square, int currentRow, int currentCol) {
        Color beforeColor = square.getBackground();

        System.out.println("------");
        System.out.println(currentRow);
        System.out.println(currentCol);
        System.out.println("------");
        System.out.println(square.getRow());
        System.out.println(square.getCol());

        if (square.getRow() != currentRow && square.getCol() != currentCol) {
            squares[currentRow][currentCol].setBackground(Color.LIGHT_GRAY);
        } else {
            squares[square.getRow()][square.getCol()].setBackground(beforeColor);
        }
    }

}
