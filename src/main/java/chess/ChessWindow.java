package chess;

import menu.RightContainer;

import javax.swing.*;
import java.awt.*;

public class ChessWindow extends JFrame {

    public ChessWindow() {
        setTitle("Chess");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new Board(), new RightContainer());

        pane.setResizeWeight(0.8);
        pane.setDividerSize(0);
        pane.setEnabled(false);

        add(pane);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }


}
