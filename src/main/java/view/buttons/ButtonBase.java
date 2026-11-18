package view.buttons;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

public abstract class ButtonBase extends JButton {

    protected ButtonBase(String title) {
        super(title);

        setPreferredSize(new Dimension(200, 55));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }



    protected abstract void configureAction(GameController controller);

}
