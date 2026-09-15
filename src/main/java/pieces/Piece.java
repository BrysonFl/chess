package pieces;

import javax.swing.*;

public abstract class Piece {

    private ImageIcon image;

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public ImageIcon getImage() {
        return image;
    }

    public abstract void validateMovement();

}
