package chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardContainer extends JPanel {

    public BoardContainer() {
        setSize(1000, 1000);
        setBorder(BorderFactory.createLineBorder(Color.RED));
        setLayout(new GridBagLayout());

        Board board = new Board();

        add(board);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                board.clearAll();
            }
        });
    }

}
