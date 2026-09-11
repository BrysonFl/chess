package chess;

import pieces.Piece;

import javax.swing.*;
import java.awt.*;

public class Square extends JButton {

    private Piece piece;

    public Square(Color color) {
        setBorderPainted(true);
        setOpaque(false);
        setContentAreaFilled(false);
        setBackground(color);
    }

}
