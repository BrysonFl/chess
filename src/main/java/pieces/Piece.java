package pieces;

import javax.swing.*;
import java.util.List;

public abstract class Piece {

    private ImageIcon image;

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public ImageIcon getImage() {
        return image;
    }

    public abstract List<Integer[]> validateMovements(int row, int col);

}
