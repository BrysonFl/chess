package view.buttons;

import controller.GameController;

public class ClaimDrawButton extends ButtonBase {

    public ClaimDrawButton(String title) {
        super(title);
    }

    @Override
    public void configureAction(GameController controller) {
        addActionListener(event -> controller.claimDraw());
    }
}
