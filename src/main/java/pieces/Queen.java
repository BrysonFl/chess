package pieces;

import javax.swing.*;
import java.net.URL;

public class Queen extends Piece {

    public Queen(String color) {
        URL imageUrl = getClass().getResource(color.equalsIgnoreCase("white") ? "/queen_white.png" : "/queen_black.png");

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
