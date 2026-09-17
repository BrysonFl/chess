package pieces;

import chess.Square;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(PiecesColors color) {
        super(color);

        URL imageUrl = getClass().getResource(color.equals(PiecesColors.WHITE) ? "/king_white.png" : "/king_black.png");

        if (imageUrl != null) {
            setImage(new ImageIcon(imageUrl));
        } else {
            System.out.println();
        }
    }

    @Override
    public List<Integer[]> validateMovements(int row, int col, Square[][] board) {
        List<Integer[]> moves = new ArrayList<>();

        return new ArrayList<>();
    }
}
