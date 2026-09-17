package pieces;

import chess.Square;

import javax.swing.*;
import java.util.List;

public abstract class Piece {

    private ImageIcon image;
    private PiecesColors color;

    public Piece(PiecesColors color) {
        this.color = color;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public ImageIcon getImage() {
        return image;
    }

    public PiecesColors getColor() {
        return color;
    }

    public void setColor(PiecesColors color) {
        this.color = color;
    }

    public abstract List<Integer[]> validateMovements(int row, int col, Square[][] squares);

}
