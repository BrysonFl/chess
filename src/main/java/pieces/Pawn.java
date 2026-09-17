package pieces;

import chess.Square;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(PiecesColors color) {
        super(color);

        URL imageUrl = getClass().getResource(color.equals(PiecesColors.WHITE) ? "/pawn_white.png" : "/pawn_black.png");

        if (imageUrl != null) {
            setImage(new ImageIcon(imageUrl));
        } else {
            System.out.println();
        }
    }

    @Override
    public List<Integer[]> validateMovements(int row, int col, Square[][] board) {
        List<Integer[]> moves = new ArrayList<>();

        moves.add(new Integer[]{ row - 1, col});

        if (row == 6) {
            moves.add(new Integer[]{ row - 2, col});
        }

        return moves;
    }

}
