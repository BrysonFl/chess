package utils;

/**
 * Clase utilitaria que contiene métodos de validación globales utilizados 
 * en la lógica del juego de ajedrez.
 * 
 * @author brysonfl
 */
public class UtilValidations {

    /**
     * Valida si una posición dada (fila y columna) se encuentra dentro de los 
     * límites permitidos de un tablero de ajedrez estándar (de 0 a 7).
     * 
     * @param row El índice de la fila a validar.
     * @param col El índice de la columna a validar.
     * @return {@code true} si las coordenadas están dentro del tablero, {@code false} en caso contrario.
     */
    public static boolean validateBoard(int row, int col) {
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }
    
}
