package chess;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    public BoardPanel() {
        setLayout(new GridLayout(8, 8, 0, 0));

        for (int i = 0; i < 64; i++) {
            add(new Square(i % 2 == 0 ? Color.CYAN : Color.WHITE));
        }
    }
}
