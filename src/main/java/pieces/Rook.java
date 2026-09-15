package pieces;

import javax.swing.*;
import java.net.URL;

public class Rook extends Piece {

    public Rook(String color) {
        URL imageUrl = getClass().getResource(color.equalsIgnoreCase("white") ? "/rook_white.png" : "/rook_black.png");

        if (imageUrl != null) {
            setImage(new ImageIcon(imageUrl));
        } else {
            System.out.println();
        }
    }

    @Override
    public void validateMovement() {

    }

    @Override
    public String toString() {
        return "Rook{}";
    }
}
