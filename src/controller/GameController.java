package controller;

import view.Board;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.Square;

/**
 *
 * @author brysonfl
 */
public class GameController implements ActionListener {
    
    private Board board;
    
    public GameController(Board board) {
        this.board = board;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Square currentSquare = (Square) e.getSource();
        validatePieceMovements(currentSquare);
    }
    
    private static void validatePieceMovements(Square square) {
        
    }
   
}
