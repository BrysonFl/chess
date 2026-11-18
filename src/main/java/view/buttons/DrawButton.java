package view.buttons;

import controller.GameController;

public class DrawButton extends ButtonBase {

    public DrawButton(String title) {
        super(title);

        this.addActionListener(event -> {

        });
    }

    @Override
    public void configureAction(GameController controller) {
        addActionListener(event -> controller.offerDraw());
    }
}
