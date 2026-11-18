package view.buttons;

import controller.GameController;
import view.frames.ResignGameWindow;

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
            }
        });
    }
}
