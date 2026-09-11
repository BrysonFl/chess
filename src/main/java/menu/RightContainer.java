package menu;

import javax.swing.*;
import java.awt.*;

public class RightContainer extends JPanel {

    public RightContainer() {
        setBackground(Color.WHITE);
        setSize(500, getHeight());
        add(new RightPanel());
    }

    

}
