package pieces;

import chess.Square;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(PiecesColors color) {
        super(color);

        URL imageUrl = getClass().getResource(color.equals(PiecesColors.WHITE) ? "/knight_white.png" : "/knight_black.png");

        if (imageUrl != null) {
            setImage(new ImageIcon(imageUrl));
        } else {
            System.out.println();
        }
    }

    @Override
    public List<Integer[]> validateMovements(int row, int col, Square[][] board) {
        List<Integer[]> moves = new ArrayList<>();

        moves.add(new Integer[]{ row - 2, col + 1 });
        moves.add(new Integer[]{ row - 2, col - 1 });
        moves.add(new Integer[]{ row - 1, col + 2 });
        moves.add(new Integer[]{ row - 1, col - 2 });

        moves.add(new Integer[]{ row + 2, col + 1 });
        moves.add(new Integer[]{ row + 2, col - 1 });
        moves.add(new Integer[]{ row + 1, col + 2 });
        moves.add(new Integer[]{ row + 1, col - 2 });

        return moves;
    }
}
