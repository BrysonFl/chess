package view.frames;

import javax.swing.*;
import java.awt.*;

public class ResignGameWindow {

    public boolean confirmResignation(Component parentComponent) {
        JOptionPane optionPane = new JOptionPane("¿Quieres abandonar la partida?",
                JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        JDialog dialog = optionPane.createDialog(parentComponent, "Abandonar");
        dialog.setLocationRelativeTo(null);

        try {
            dialog.setVisible(true);

            return Integer.valueOf(JOptionPane.YES_OPTION).equals(optionPane.getValue());
        } finally {
            dialog.dispose();
        }
    }
}
