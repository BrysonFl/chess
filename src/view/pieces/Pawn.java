package view.pieces;

import java.net.URL;

/**
 *
 * @author brysonfl
 */
public class Pawn extends Piece {
    
    public Pawn() {
        URL url = getClass().getResource("/images/pieces/wP.png");
        
        if (url != null) {
            setIcon(url);
        } else {
            System.out.println("No se encontró la imagen");
        }
    }
    
}
