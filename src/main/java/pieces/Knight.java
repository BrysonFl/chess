package pieces;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(String color) {
        URL imageUrl = getClass().getResource(color.equalsIgnoreCase("white") ? "/knight_white.png" : "/knight_black.png");

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
