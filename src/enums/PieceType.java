package enums;

/**
 *
 * @author brysonfl
 */
public enum PieceType {
    PAWN("P"),
    ROOK("R"),
    KNIGHT("N"),
    BISHOP("B"),
    QUEEN("Q"),
    KING("K");
    
    private final String type;
    
    PieceType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
