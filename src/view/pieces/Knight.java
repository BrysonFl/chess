package view.pieces;

import java.net.URL;

/**
 *
 * @author brysonfl
 */
public class Knight extends Piece {
    
    public Knight() {
        URL url = getClass().getResource("/images/pieces/wN.png");
        
        if (url != null) {
            setIcon(url);
        } else {
            System.out.println("No se encontró la imagen");
        }
    }
    
}
