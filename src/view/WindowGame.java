package view;

import java.awt.GridBagLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author brysonfl
 */
public class WindowGame extends JFrame {
    
    public WindowGame() {
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Chess Royale");
        
        add(new Board());
        setVisible(true);
    }
    
}
