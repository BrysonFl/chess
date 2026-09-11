package chess;

import javax.swing.*;
import java.awt.*;

public class ChessWindow extends JFrame {

    public ChessWindow() {
        setTitle("Chess");
        setSize(500, 500);

        JPanel principalPanel = new JPanel(new GridLayout(1, 2));

        principalPanel.add(new BoardPanel());
        principalPanel.add(new RightPanel());
        add(principalPanel);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


}
