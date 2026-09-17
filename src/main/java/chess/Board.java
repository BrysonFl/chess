package chess;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Board extends JPanel {

    private final Square[][] squares = new Square[8][8];

    private static Square beforeSquare;

    public Board() {
        setLayout(new GridLayout(8, 8, 0, 0));
        setBorder(BorderFactory.createLineBorder(Color.BLUE));

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

                String rowCoordinate = (col == 0) ? String.valueOf(8 - row) : "";
                String colCoordinate = (row == 7) ? String.valueOf((char) ('a' + col)) : "";

                square.setCoordinates(rowCoordinate, colCoordinate);
                squares[row][col] = square;

                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showValidMovements((Square) e.getSource());
                        movePiece((Square) e.getSource());
                    }
                });

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

    private void showValidMovements(Square square) {
        square.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearAllSquares();

                if (square.getPiece() != null) {
                    List<Integer[]> result = square.getPiece().validateMovements(square.getRow(), square.getCol());

                    for (Integer[] move : result) {
                        int targetRow = move[0];
                        int targetCol = move[1];

                        squares[targetRow][targetCol].setPossible(true);
                    }
                }
            }
        });
    }

    public void clearAllSquares() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setPossible(false);
            }
        }
    }

    private void movePiece(Square square) {
        System.out.println("Before square " + beforeSquare);
        System.out.println("Current square " + square);

        if (beforeSquare == null) {
            beforeSquare = square;
        }

        if (square.isPossible()) {
            squares[square.getRow()][square.getCol()].setPiece(beforeSquare.getPiece());
            squares[beforeSquare.getRow()][beforeSquare.getCol()].setPiece(null);
            beforeSquare = null;
        }

        clearAllSquares();
        //showValidMovements(square);
    }

}
