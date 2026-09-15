package chess;

import javax.swing.*;
import java.awt.*;

public class ChessWindow extends JFrame {

    public ChessWindow() {
        setTitle("Chess");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Box box = new Box(BoxLayout.Y_AXIS);
        box.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(Box.createVerticalGlue());
        box.add(new BoardContainer());
        box.add(Box.createVerticalGlue());

        add(box);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }


}
