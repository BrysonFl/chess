package view.pieces;

import enums.PieceType;
import java.util.List;
import view.Board;

/**
 * Clase representativa de la pieza Torre. 
 * Gestiona y define el comportamiento y las características propias de la torre 
 * dentro de la lógica del juego de ajedrez.
 * 
 * @author brysonfl
 */
public class Rook extends Piece {
    
    public Rook(boolean isWhite) {
        super(isWhite, PieceType.ROOK);
    }

    /**
     * Calcula y retorna una lista con todos los movimientos posibles disponibles
     * para la torre desde su posición actual en el tablero. La torre puede moverse 
     * cualquier número de casillas libres, incluyendo la primera captura enemiga, en línea recta vertical u horizontal
     * (hacia arriba, abajo, izquierda o derecha).
     * 
     * @param row   La fila actual en la que se encuentra la torre.
     * @param col   La columna actual en la que se encuentra la torre.
     * @param board El tablero actual del juego, utilizado para verificar el estado de las casillas.
     * @return Una lista de arreglos de enteros donde cada elemento representa las coordenadas 
     *         {@code [fila, columna]} de una casilla válida a la que la torre puede desplazarse.
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        return directionalMovements(row, col, board, new int[][]{ {-1,0}, {1,0}, {0,-1}, {0,1} }, true);
    }
    
}
