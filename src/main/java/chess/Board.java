package chess;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Board extends JPanel {

    public Board() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        //setBackground(Color.white);
        add(new BoardPanel());
    }

}
