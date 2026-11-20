package view.frames;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ResignGameWindow {

    public boolean confirmResignation(Component parentComponent) {
        JOptionPane optionPane = new JOptionPane("¿Quieres abandonar la partida?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        JDialog dialog = optionPane.createDialog(parentComponent, "Abandonar");
        dialog.setLocationRelativeTo(null);

        try {
            dialog.setVisible(true);

            return Integer.valueOf(JOptionPane.YES_OPTION).equals(optionPane.getValue());
        } finally {
            dialog.dispose();
        }
    }

    public void showCountdown(Component parentComponent, int seconds, Runnable onFinish) {
        AtomicInteger remainingSeconds = new AtomicInteger(seconds);
        JLabel messageLabel = new JLabel(formatCountdownMessage(remainingSeconds.get()), SwingConstants.CENTER);
        JOptionPane optionPane = new JOptionPane(messageLabel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

        JDialog dialog = optionPane.createDialog(parentComponent, "Nueva partida");
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setLocationRelativeTo(null);

        Timer timer = new Timer(1000, event -> {
            int remaining = remainingSeconds.decrementAndGet();

            if (remaining > 0) {
                messageLabel.setText(formatCountdownMessage(remaining));
            } else {
                ((Timer) event.getSource()).stop();
                dialog.dispose();
            }
        });

        try {
            timer.start();
            dialog.setVisible(true);
        } finally {
            timer.stop();
            dialog.dispose();
        }

        if (onFinish != null) {
            onFinish.run();
        }
    }

    private String formatCountdownMessage(int seconds) {
        return "Se iniciará una nueva partida en " + seconds + (seconds == 1 ? " segundo..." : " segundos...");
    }
}
