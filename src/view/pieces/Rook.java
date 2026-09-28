package view.pieces;

import java.net.URL;

/**
 *
 * @author brysonfl
 */
public class Rook extends Piece {
    
    public Rook() {
        URL url = getClass().getResource("/images/pieces/wR.png");
        
        if (url != null) {
            setIcon(url);
        } else {
            System.out.println("No se encontró la imagen");
        }
    }
    
}
