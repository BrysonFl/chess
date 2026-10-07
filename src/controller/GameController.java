package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import view.Board;
import view.Square;

/**
 *
 * @author brysonfl
 */
public class GameController implements ActionListener {
    
    private final Board board;
    
    private static int movements = 0;
    
    private static Square beforeSquare;
    
    public GameController(Board board) {
        this.board = board;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Square currentSquare = (Square) e.getSource();
        
        if (beforeSquare != null && currentSquare.isHighlight()) {
            movePiece(currentSquare);
            beforeSquare = null;
            return;
        }
        
        if (currentSquare.getPiece() != null) {
            validatePieceMovements(currentSquare);
            beforeSquare = currentSquare;
        }
        
        if (currentSquare.getPiece() == null && !currentSquare.isHighlight()) {
            clearHighlights();
            beforeSquare = null;
        }
    }
    
    private void movePiece(Square currentSquare) {
        currentSquare.setPiece(beforeSquare.getPiece());
        beforeSquare.setPiece(null);
        clearHighlights();
        movements++;
    }
    
    private void validatePieceMovements(Square square) {
        clearHighlights();
        
        List<Integer[]> pieceMovements = square.getPiece()
                .validMovements(square.getRow(), square.getCol(), this.board);
        
        for (Integer[] move : pieceMovements) {
            int row = move[0];
            int col = move[1];
            
            board.getSquares()[row][col].setHighlight(true);
        }
    }
    
    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board.getSquares()[row][col].setHighlight(false);
            }
        }
    }
   
}
