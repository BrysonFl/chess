package chess;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Board extends JPanel {

    private final Square[][] squares = new Square[8][8];

    private int initialRowPawns;
    private int initialRow;

    private static Square beforeSquare;

    private int numberPlayerTurn = 1;

    public Board() {
        setLayout(new GridLayout(8, 8, 0, 0));
        setPreferredSize(new Dimension(1000, 1000));

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = initializeSquare(row, col);
                add(square);
            }
        }

        initializeWhitePieces();
        initializeBlackPieces();
    }

    private Square initializeSquare(int row, int col) {
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
                movePiece((Square) e.getSource());
            }
        });

        return square;
    }

    private void initializeWhitePieces() {
        initialRowPawns = squares.length - 2;
        initialRow = squares.length - 1;

        for (int i = 0; i < 8; i++) {
            squares[initialRowPawns][i].setPiece(new Pawn(PiecesColors.WHITE));
        }

        squares[initialRow][0].setPiece(new Rook(PiecesColors.WHITE));
        squares[initialRow][7].setPiece(new Rook(PiecesColors.WHITE));

        squares[initialRow][1].setPiece(new Knight(PiecesColors.WHITE));
        squares[initialRow][6].setPiece(new Knight(PiecesColors.WHITE));

        squares[initialRow][2].setPiece(new Bishop(PiecesColors.WHITE));
        squares[initialRow][5].setPiece(new Bishop(PiecesColors.WHITE));

        squares[initialRow][3].setPiece(new Queen(PiecesColors.WHITE));
        squares[initialRow][4].setPiece(new King(PiecesColors.WHITE));
    }

    private void initializeBlackPieces() {
        initialRowPawns = 1;
        initialRow = 0;

        for (int i = 0; i < 8; i++) {
            squares[initialRowPawns][i].setPiece(new Pawn(PiecesColors.BLACK));
        }

        squares[initialRow][0].setPiece(new Rook(PiecesColors.BLACK));
        squares[initialRow][7].setPiece(new Rook(PiecesColors.BLACK));

        squares[initialRow][1].setPiece(new Knight(PiecesColors.BLACK));
        squares[initialRow][6].setPiece(new Knight(PiecesColors.BLACK));

        squares[initialRow][2].setPiece(new Bishop(PiecesColors.BLACK));
        squares[initialRow][5].setPiece(new Bishop(PiecesColors.BLACK));

        squares[initialRow][3].setPiece(new Queen(PiecesColors.BLACK));
        squares[initialRow][4].setPiece(new King(PiecesColors.BLACK));
    }

    private void showValidMovements(Square square) {
        clearAllSquares();

        if (square.getPiece() != null) {
            List<Integer[]> result = square.getPiece().validateMovements(square.getRow(), square.getCol(), squares);

            for (Integer[] move : result) {
                squares[move[0]][move[1]].setPossible(true);
            }
        }
    }

    public void clearAllSquares() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setPossible(false);
            }
        }
    }

    private void movePiece(Square square) {
        if (beforeSquare == null) {
            if (square.getPiece() != null) {
                beforeSquare = square;
                showValidMovements(square);
            }

            return;
        }

        if (square.isPossible()) {
            squares[square.getRow()][square.getCol()].setPiece(beforeSquare.getPiece());
            squares[beforeSquare.getRow()][beforeSquare.getCol()].setPiece(null);
            beforeSquare = null;
        }

        clearAllSquares();
        beforeSquare = null;
    }

}
