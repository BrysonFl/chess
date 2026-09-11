package chess;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {

    public RightPanel() {
        setBackground(Color.WHITE);
        setBounds(new Rectangle(0, 0, getWidth() - 10, getHeight() - 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("Panel derecho", getWidth() / 2, getHeight() / 2);
    }
}
