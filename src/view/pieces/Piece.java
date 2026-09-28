package view.pieces;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author brysonfl
 */
public abstract class Piece {
    
    private ImageIcon icon;

    public ImageIcon getIcon() {
        return icon;
    }

    protected void setIcon(URL icon) {
        if (icon != null) {
            Image image = new ImageIcon(icon).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            this.icon = new ImageIcon(image);
        }
    }
    
}
