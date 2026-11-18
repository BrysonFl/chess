package view.buttons;

import javax.swing.*;

public abstract class ButtonBase extends JButton {

    protected String name;

    protected ButtonBase(String name) {
        super(name);
    }

}
