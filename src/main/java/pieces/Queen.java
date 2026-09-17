package pieces;

import chess.Square;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(PiecesColors color) {
        super(color);

        URL imageUrl = getClass().getResource(color.equals(PiecesColors.WHITE) ? "/queen_white.png" : "/queen_black.png");

        if (imageUrl != null) {
            setImage(new ImageIcon(imageUrl));
        } else {
            System.out.println();
        }
    }

    @Override
    public List<Integer[]> validateMovements(int row, int col, Square[][] board) {
        return new ArrayList<>();
    }
}
