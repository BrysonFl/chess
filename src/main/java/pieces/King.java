package pieces;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(String color) {
        URL imageUrl = getClass().getResource(color.equalsIgnoreCase("white") ? "/king_white.png" : "/king_black.png");

        if (imageUrl != null) {
            setImage(new ImageIcon(imageUrl));
        } else {
            System.out.println();
        }
    }

    @Override
    public List<Integer[]> validateMovements(int row, int col) {
        return new ArrayList<>();
    }
}
