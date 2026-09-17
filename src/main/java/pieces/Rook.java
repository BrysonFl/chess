package pieces;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    public List<Integer[]> validateMovements(int row, int col) {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Rook{}";
    }
}
