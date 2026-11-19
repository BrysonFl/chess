package view.buttons;

import controller.GameController;
import view.WindowGame;
import view.frames.ResignGameWindow;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ResignButton extends ButtonBase {

    public ResignButton(String title) {
        super(title);
    }

    @Override
    public void configureAction(GameController controller) {
        addActionListener(event -> {
            ResignGameWindow resignGameWindow = new ResignGameWindow();

            if (resignGameWindow.confirmResignation(this)) {
                controller.resign();

                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.schedule(() -> JOptionPane.showMessageDialog(this, "Se iniciará una nueva partida en 10 segundo"), 5, TimeUnit.SECONDS);
                new WindowGame("Iniciaste un juego nuevo");
            }
        });
    }
}
