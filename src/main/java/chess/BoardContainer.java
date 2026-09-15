package chess;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BoardContainer extends JPanel {

    public BoardContainer() {
        //setSize(800, 800);
        setBorder(BorderFactory.createLineBorder(Color.RED));
        add(new Board());
    }

}
