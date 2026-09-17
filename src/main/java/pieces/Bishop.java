package pieces;

import chess.Square;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(PiecesColors color) {
        super(color);

        URL imageUrl = getClass().getResource(color.equals(PiecesColors.WHITE) ? "/bishop_white.png" : "/bishop_black.png");

        if (imageUrl != null) {
            setImage(new ImageIcon(imageUrl));
        } else {
            System.out.println();
        }
    }

    @Override
    public List<Integer[]> validateMovements(int row, int col, Square[][] board) {
        List<Integer[]> moves = new ArrayList<>();

        int[][] directions = new int[][]{
            { -1, 1 }, { -1, -1},
            { 1, 1 }, { 1, -1 }
        };

        for (Square[] x : board) {
            for (Square square : x) {
                System.out.println(square.getPiece().getColor());
            }
        }

        for (int[] direction : directions) {
            int r = row + direction[0];
            int c = col + direction[1];

            while (r >= 0 && r <= 7 && c >= 0 && c <= 7) {
                moves.add(new Integer[]{ r, c });
                r += direction[0];
                c += direction[1];
            }
        }

        return moves;
    }
}
