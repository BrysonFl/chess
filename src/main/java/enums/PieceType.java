package enums;

/**
 * Enumeración que representa los diferentes tipos de piezas de ajedrez disponibles, 
 * asociando a cada una un identificador en forma de letra para su manejo interno 
 * y la carga de recursos gráficos.
 * 
 * @author brysonfl
 */
public enum PieceType {
    PAWN("P", 1),
    ROOK("R", 5),
    KNIGHT("N", 3),
    BISHOP("B", 3),
    QUEEN("Q", 9),
    KING("K", 0);
    
    private final String type;
    private final int value;
    
    /**
     * Construye un tipo de pieza con el identificador en cadena de texto correspondiente.
     * 
     * @param type La letra representativa del tipo de pieza.
     * @param value El valor material de la pieza; 0 para el rey, que no se captura.
     */
    PieceType(String type, int value) {
        this.type = type;
        this.value = value;
    }
    
    /**
     * Obtiene el identificador en texto (la primera letra) asociado al tipo de pieza.
     * 
     * @return Una cadena con la letra representativa de la pieza.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Obtiene el valor material del tipo de pieza.
     *
     * @return El valor en puntos; 0 indica que el rey no participa en el conteo material.
     */
    public int getValue() {
        return value;
    }
}
