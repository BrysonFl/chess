package pieces;

import javax.swing.*;
import java.net.URL;

public class Pawn extends Piece {

    public Pawn(String color) {
        URL imageUrl = getClass().getResource(color.equalsIgnoreCase("white") ? "/pawn_white.png" : "/pawn_black.png");

        if (imageUrl != null) {
            setImage(new ImageIcon(imageUrl));
        } else {
            System.out.println();
        }
    }

    @Override
    public void validateMovement() {

    }
}
