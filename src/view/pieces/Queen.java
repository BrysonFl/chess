package view.pieces;

import enums.PieceType;
import java.util.List;
import view.Board;

/**
 * Clase representativa de la pieza Reina. 
 * Gestiona y define el comportamiento, las características y el alcance de movimiento 
 * de la reina (la pieza más poderosa del tablero) dentro de la lógica del juego de ajedrez.
 * 
 * @author brysonfl
 */
public class Queen extends Piece {
    
    public Queen(boolean isWhite) {
        super(isWhite, PieceType.QUEEN);
    }

    /**
     * Calcula y retorna una lista con todos los movimientos posibles disponibles
     * para la reina desde su posición actual en el tablero. La reina puede moverse 
     * cualquier número de casillas libres, incluyendo la primera captura enemiga, en línea recta en cualquier dirección
     * (horizontal, vertical y diagonal).
     * 
     * @param row   La fila actual en la que se encuentra la reina.
     * @param col   La columna actual en la que se encuentra la reina.
     * @param board El tablero actual del juego, utilizado para verificar el estado de las casillas.
     * @return Una lista de arreglos de enteros donde cada elemento representa las coordenadas 
     *         {@code [fila, columna]} de una casilla válida a la que la reina puede desplazarse.
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        return directionalMovements(row, col, board, new int[][]{ {-1,0}, {1,0}, {0,-1}, {0,1}, {-1,-1}, {-1,1}, {1,-1}, {1,1} }, true);
    }
    
}
