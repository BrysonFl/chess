package view.pieces;

import enums.PieceType;
import java.util.List;
import view.Board;

/**
 * Clase representativa de la pieza Alfil. 
 * Gestiona los movimientos en diagonal del alfil, evaluando las cuatro direcciones 
 * diagonales posibles desde su posición actual en el tablero.
 * 
 * @author brysonfl
 */
public class Bishop extends Piece {
    
    /**
     * Construye un nuevo alfil con el color especificado.
     * 
     * @param isWhite Define si el alfil es blanco ({@code true}) o negro ({@code false}).
     */
    public Bishop(boolean isWhite) {
        super(isWhite, PieceType.BISHOP);
    }

    /**
     * Calcula y retorna una lista con todos los movimientos posibles disponibles
     * para el alfil desde su posición actual en el tablero. El alfil puede moverse cualquier 
     * número de casillas desocupadas en línea recta diagonal en cualquiera de las cuatro 
     * direcciones posibles.
     * 
     * @param row   La fila actual en la que se encuentra el alfil.
     * @param col   La columna actual en la que se encuentra el alfil.
     * @param board El tablero actual del juego, utilizado para verificar el estado de las casillas.
     * @return Una lista de arreglos de enteros donde cada elemento representa las coordenadas 
     *         {@code [fila, columna]} de una casilla válida a la que el alfil puede desplazarse.
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        return directionalMovements(row, col, board, new int[][]{ {-1,-1}, {-1,1}, {1,-1}, {1,1} }, true);
    }
    
}
