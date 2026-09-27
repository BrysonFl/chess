package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author brysonfl
 */
public class WindowGame extends JFrame {
    
    public WindowGame() {
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel boardContainer = new JPanel(new BorderLayout());
        boardContainer.setPreferredSize(new Dimension(500, 500));
        boardContainer.add(new Board());
        
        add(boardContainer);
        setVisible(true);
    }
    
}
