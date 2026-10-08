package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import view.Board;
import view.Square;
import view.pieces.Pawn;
import view.pieces.Piece;

/**
 *
 * @author brysonfl
 */
public class GameController implements ActionListener {
    
    private final Board board;
    
    private static boolean whitesMove = true;
    
    private static int generalMovements;
    private static int movementsWithoutCaptures;
    
    private static Square beforeSquare;
    
    public GameController(Board board) {
        this.board = board;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Square currentSquare = (Square) e.getSource();
        
        System.out.println("beforeSquare " + beforeSquare);
        
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
        Piece pieceMoved = beforeSquare.getPiece();
        
        if (pieceMoved instanceof Pawn) {
            if (beforeSquare.getCol() != currentSquare.getCol() && currentSquare.getPiece() == null) {
                board.getSquares()[beforeSquare.getRow()][currentSquare.getCol()].setPiece(null);
            }
        }
        
        currentSquare.setPiece(beforeSquare.getPiece());
        beforeSquare.setPiece(null);
        clearHighlights();
        generalMovements++;
        whitesMove = !whitesMove;
    }
    
    private void validatePieceMovements(Square square) {
        clearHighlights();
        
        boolean isCorrectMove = (whitesMove && square.getPiece().isWhite()) || (!whitesMove && !square.getPiece().isWhite());
        
        if (isCorrectMove) {
            List<Integer[]> pieceMovements = square.getPiece()
                .validMovements(square.getRow(), square.getCol(), this.board);
        
            for (Integer[] move : pieceMovements) {
                int row = move[0];
                int col = move[1];

                board.getSquares()[row][col].setHighlight(true);
            }
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
