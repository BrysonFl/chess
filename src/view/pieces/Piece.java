package view.pieces;

import enums.PieceType;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author brysonfl
 */
public abstract class Piece {
    
    private ImageIcon icon;
    private final PieceType pieceType;
    
    private final boolean isWhite;
    
    protected Piece(boolean isWhite, PieceType pieceType) {
        this.isWhite = isWhite;
        this.pieceType = pieceType;
        loadIcon(isWhite);
    }
    
    private void loadIcon(boolean isWhite) {
        String builderRoutePiece = new StringBuilder("/images/pieces/")
            .append(isWhite ? "w" : "b")
            .append(this.pieceType.getType())
            .append(".png")
            .toString();
        
        URL url = getClass().getResource(builderRoutePiece);
        
        if (url != null) {
            Image image = new ImageIcon(url).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            this.icon = new ImageIcon(image);
        } else {
            System.out.println("No se encontró la imagen");
        }
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public boolean isIsWhite() {
        return isWhite;
    }
    
    public abstract int[][] validMovements(int row, int col);
    
}
