package view.pieces;

import enums.PieceType;
import java.util.List;
import view.Board;

/**
 * Clase representativa de la pieza Rey. 
 * Gestiona los movimientos permitidos para el rey, evaluando las 8 casillas 
 * adyacentes a su posición actual en el tablero (hacia arriba, abajo, izquierda, 
 * derecha y en diagonal, una casilla a la vez).
 * 
 * @author brysonfl
 */
public class King extends Piece {
    
    /**
     * Construye un nuevo rey con el color especificado.
     * 
     * @param isWhite Define si el rey es blanco ({@code true}) o negro ({@code false}).
     */
    public King(boolean isWhite) {
        super(isWhite, PieceType.KING);
    }

    /**
     * Calcula y retorna una lista con todos los movimientos posibles disponibles
     * para el rey desde su posición actual en el tablero. El rey puede moverse una única 
     * casilla en cualquier dirección (horizontal, vertical o diagonal) siempre que esté 
     * dentro de los límites del tablero y la casilla de destino esté libre u ocupada por una pieza enemiga.
     * 
     * @param row   La fila actual en la que se encuentra el rey.
     * @param col   La columna actual en la que se encuentra el rey.
     * @param board El tablero actual del juego, utilizado para verificar el estado de las casillas.
     * @return Una lista de arreglos de enteros donde cada elemento representa las coordenadas 
     *         {@code [fila, columna]} de una casilla válida a la que el rey puede desplazarse.
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        return directionalMovements(row, col, board, new int[][]{ {-1,0}, {1,0}, {0,-1}, {0,1}, {-1,-1}, {-1,1}, {1,-1}, {1,1} }, false);
    }
    
}
