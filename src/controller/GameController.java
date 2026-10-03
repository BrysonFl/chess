package controller;

import view.Board;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.Square;
import view.pieces.Pawn;

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
        validatePieceMovements(currentSquare);
    }
    
    private static void validatePieceMovements(Square square) {
        int[][] pieceMovements = square.getPiece().validMovements();
        
        if (square.getPiece() instanceof Pawn && movements == 0) {
            movements++;
        }
    }
   
}
