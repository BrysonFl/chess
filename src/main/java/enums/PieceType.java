package enums;

/**
 * Enumeración que define los diferentes tipos de piezas del juego de ajedrez.
 * <p>
 * Cada constante asocia un identificador alfabético en notación inglesa estándar
 * (utilizado para la notación algebraica, identificación interna de estado y carga
 * de recursos gráficos) junto con su valor material relativo en puntos.
 * </p>
 *
 * <p>Los códigos son P, R, N, B, Q y K, independientemente del idioma de la interfaz.
 * Los valores materiales son descriptivos y no determinan por sí solos el resultado
 * ni la legalidad de una posición.</p>
 *
 * @author brysonfl
 */
public enum PieceType {
    /**
     * Representa al peón, identificado con la letra {@code "P"} y un valor material de {@code 1} punto.
     */
    PAWN("P", 1),
    /**
     * Representa a la torre, identificada con la letra {@code "R"} y un valor material de {@code 5} puntos.
     */
    ROOK("R", 5),
    /**
     * Representa al caballo, identificado con la letra {@code "N"} y un valor material de {@code 3} puntos.
     */
    KNIGHT("N", 3),
    /**
     * Representa al alfil, identificado con la letra {@code "B"} y un valor material de {@code 3} puntos.
     */
    BISHOP("B", 3),
    /**
     * Representa a la dama (reina), identificada con la letra {@code "Q"} y un valor material de {@code 9} puntos.
     */
    QUEEN("Q", 9),
    /**
     * Representa al rey, identificado con la letra {@code "K"} y un valor material de {@code 0} puntos,
     * dado que no puede ser capturado ni participa en el conteo de ventaja material.
     */
    KING("K", 0);
    
    /**
     * Identificador alfabético de una sola letra que representa el tipo de pieza.
     */
    private final String type;

    /**
     * Valor material relativo de la pieza expresado en puntos.
     */
    private final int value;
    
    /**
     * Construye un nuevo tipo de pieza con su identificador en texto y su valor material.
     *
     * @param type  la letra representativa del tipo de pieza (por ejemplo, {@code "P"}, {@code "N"}, {@code "K"}).
     * @param value el valor material de la pieza en puntos; {@code 0} para el rey, que no se captura.
     */
    PieceType(String type, int value) {
        this.type = type;
        this.value = value;
    }
    
    /**
     * Obtiene el identificador en cadena de texto asociado al tipo de pieza.
     *
     * @return una cadena de un carácter con la letra representativa de la pieza.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Obtiene el valor material relativo asignado al tipo de pieza.
     *
     * @return el valor en puntos de la pieza; {@code 0} indica que el rey no participa en el conteo material.
     */
    public int getValue() {
        return value;
    }
}
