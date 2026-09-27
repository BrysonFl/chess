package view;

import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author brysonfl
 */
public class WindowGame extends JFrame {
    
    public WindowGame() {
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setVisible(true);
    }
    
}
