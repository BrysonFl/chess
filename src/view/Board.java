package view;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 *
 * @author brysonfl
 */
public class Board extends JPanel {
    
    private Square[][] squares;
    
    public Board() {
        setBackground(Color.red);
        setLayout(new GridBagLayout());
        
        setVisible(true);
    }
    
    private void loadSquaresBoard() {
        squares = new Square[8][8];
        
        setLayout(new GridLayout(8, 8));
    }
    
}
