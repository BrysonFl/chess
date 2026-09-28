package view.pieces;

import java.net.URL;

/**
 *
 * @author brysonfl
 */
public class King extends Piece {
    
    public King() {
        URL url = getClass().getResource("/images/pieces/wK.png");
        
        if (url != null) {
            super.setIcon(url);
        } else {
            System.out.println("No se encontró la imagen");
        }
    }
    
}
