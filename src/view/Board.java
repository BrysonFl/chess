package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import view.pieces.Knight;
import view.pieces.Pawn;
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
            squares[6][col].setPiece(new Pawn());
        }
        
        squares[7][0].setPiece(new Rook());
        squares[7][7].setPiece(new Rook());
        
        squares[7][1].setPiece(new Knight());
        squares[7][6].setPiece(new Knight());
    }
    
}
