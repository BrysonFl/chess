package view.buttons;

import controller.GameController;
import view.WindowGame;
import view.frames.ResignGameWindow;

import javax.swing.*;
import java.awt.*;

public class ResignButton extends ButtonBase {

    private static final int NEW_GAME_COUNTDOWN_SECONDS = 5;

    public ResignButton(String title) {
        super(title);
    }

    @Override
    public void configureAction(GameController controller) {
        addActionListener(event -> {
            ResignGameWindow resignGameWindow = new ResignGameWindow();

            if (resignGameWindow.confirmResignation(this)) {
                controller.resign();

                resignGameWindow.showCountdown(this, NEW_GAME_COUNTDOWN_SECONDS, () -> {
                    Window currentWindow = SwingUtilities.getWindowAncestor(this);
                    if (currentWindow != null) {
                        currentWindow.dispose();
                    }

                    new WindowGame("Chess Royale - Iniciaste un juego nuevo");
                });
            }
        });
    }
}
