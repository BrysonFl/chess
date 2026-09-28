package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import view.pieces.Bishop;
import view.pieces.King;
import view.pieces.Knight;
import view.pieces.Pawn;
import view.pieces.Queen;
import view.pieces.Rook;

/**
 *
 * @author brysonfl
 */
public class Board extends JPanel {
    
    private Square[][] squares;
    
    public Board() {
        setPreferredSize(new Dimension(1000, 1000));
        setBackground(Color.red);
        loadSquaresBoard();
        initializeWhitePieces();
        initializeBlackPieces();
    }
    
    private void loadSquaresBoard() {
        setLayout(new GridLayout(8, 8));
        squares = new Square[8][8];
        
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                squares[row][col] = new Square(row, col);
                add(squares[row][col]);
            }
        }
    }
    
    private void initializeWhitePieces() {
        for (int col = 0; col < squares.length; col++) {
            squares[6][col].setPiece(new Pawn(true));
        }
        
        squares[7][0].setPiece(new Rook(true));
        squares[7][7].setPiece(new Rook(true));
        
        squares[7][1].setPiece(new Knight(true));
        squares[7][6].setPiece(new Knight(true));
        
        squares[7][2].setPiece(new Bishop(true));
        squares[7][5].setPiece(new Bishop(true));
        
        squares[7][4].setPiece(new Queen(true));
        squares[7][3].setPiece(new King(true));
    }
    
    private void initializeBlackPieces() {
        for (int col = 0; col < squares.length; col++) {
            squares[1][col].setPiece(new Pawn(false));
        }
        
        squares[0][0].setPiece(new Rook(false));
        squares[0][7].setPiece(new Rook(false));
        
        squares[0][1].setPiece(new Knight(false));
        squares[0][6].setPiece(new Knight(false));
        
        squares[0][2].setPiece(new Bishop(false));
        squares[0][5].setPiece(new Bishop(false));
        
        squares[0][4].setPiece(new Queen(false));
        squares[0][3].setPiece(new King(false));
    }
    
}
