package enums;

/**
 * Enumeración que representa los diferentes tipos de piezas de ajedrez disponibles, 
 * asociando a cada una un identificador en forma de letra para su manejo interno 
 * y la carga de recursos gráficos.
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
    
    /**
     * Construye un tipo de pieza con el identificador en cadena de texto correspondiente.
     * 
     * @param type La letra representativa del tipo de pieza.
     */
    PieceType(String type) {
        this.type = type;
    }
    
    /**
     * Obtiene el identificador en texto (la primera letra) asociado al tipo de pieza.
     * 
     * @return Una cadena con la letra representativa de la pieza.
     */
    public String getType() {
        return this.type;
    }
}
