package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.Board;
import view.Square;

/**
 *
 * @author brysonfl
 */
public class GameController implements ActionListener {
    
    private Board board;
    
    private static int movements = 0;
    
    public GameController(Board board) {
        this.board = board;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Square currentSquare = (Square) e.getSource();
        
        if (currentSquare.getPiece() != null) {
            validatePieceMovements(currentSquare);
        } else {
            clearHighlights();
        }
    }
    
    private void validatePieceMovements(Square square) {
        clearHighlights();
        
        int[][] pieceMovements = square.getPiece().validMovements(square.getRow(), square.getCol());
        
        for (int[] moves : pieceMovements) {
            this.board.getSquares()[moves[0]][moves[1]].setBackground(Color.BLUE);
        }
        
        square.setBackground(new Color(0, 0, 0, 100));
    }
    
    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                this.board.getSquares()[row][col].resetBackground();
            }
        }
    }
   
}
