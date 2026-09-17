package pieces;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    public List<Integer[]> validateMovements(int row, int col) {
        List<Integer[]> moves = new ArrayList<>();

        moves.add(new Integer[]{ row - 1, col});

        if (row == 6) {
            moves.add(new Integer[]{ row - 2, col});
        }

        return moves;
    }

}
