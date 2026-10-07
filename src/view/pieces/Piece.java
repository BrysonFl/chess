package view.pieces;

import enums.PieceType;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
import view.Board;

/**
 *
 * @author brysonfl
 */
public abstract class Piece {
    
    private ImageIcon icon;
    private final PieceType pieceType;
    
    private final boolean white;
    
    protected Piece(boolean isWhite, PieceType pieceType) {
        this.white = isWhite;
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

    public boolean isWhite() {
        return white;
    }
    
    public abstract List<Integer[]> validMovements(int row, int col, Board board);

    @Override
    public String toString() {
        return "Piece{" + "icon=" + icon + ", pieceType=" + pieceType + ", isWhite=" + white + '}';
    }
    
}
