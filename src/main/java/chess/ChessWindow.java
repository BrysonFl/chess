package chess;

import javax.swing.*;
import java.awt.*;

public class ChessWindow extends JFrame {

    public ChessWindow() {
        setTitle("Chess");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);

        add(new Board());
    }


}
