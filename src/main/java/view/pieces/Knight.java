package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Board;

/**
 * Representa la pieza de ajedrez Caballo ({@link PieceType#KNIGHT}).
 * <p>
 * Esta clase extiende {@link Piece} y modela el movimiento en forma de "L" característico
 * del caballo (dos casillas en un eje y una casilla en el eje perpendicular). A diferencia
 * de las demás piezas del juego, el caballo puede saltar sobre piezas propias o rivales,
 * evaluando hasta 8 posibles casillas de destino desde su posición actual.
 * </p>
 *
 * <p>Los destinos son pseudolegales: respetan desplazamientos y ocupación, pero
 * no comprueban el turno ni la seguridad del rey propio. El
 * {@link controller.GameController} realiza ese filtrado antes de permitir una jugada.</p>
 *
 * @author brysonfl
 * @see Piece
 * @see PieceType#KNIGHT
 * @see Board
 */
public class Knight extends Piece {

    /**
     * Desplazamientos relativos {@code {deltaFila, deltaColumna}} que definen los 8 saltos
     * posibles en forma de "L" de un caballo en el tablero.
     */
    private static final int[][] OFFSETS = {
        {-2, 1}, {-2, -1},
        {2, 1}, {2, -1},
        {-1, 2}, {-1, -2},
        {1, 2}, {1, -2}
    };
    
    /**
     * Construye una nueva instancia de {@code Knight} con el color especificado.
     *
     * @param isWhite define si el caballo pertenece al bando blanco ({@code true})
     *                o negro ({@code false}).
     */
    public Knight(boolean isWhite) {
        super(isWhite, PieceType.KNIGHT);
    }

    /**
     * Calcula y retorna una lista con los destinos permitidos por las reglas de movimiento
     * para el caballo desde su posición actual en el tablero.
     * <p>
     * Recorre la matriz de desplazamientos {@link #OFFSETS}, descarta las coordenadas que
     * caigan fuera de los límites del tablero mediante {@link UtilValidations#validateBoard(int, int)}
     * y agrega las casillas que se encuentren vacías o que contengan una pieza enemiga
     * capturable según {@link #canCapture(Piece)}. La verificación de que el movimiento
     * no deje al propio rey en jaque recae en el controlador de la partida.
     * </p>
     *
     * @param row   el índice de la fila actual (0 a 7) en la que se encuentra el caballo.
     * @param col   el índice de la columna actual (0 a 7) en la que se encuentra el caballo.
     * @param board el tablero actual del juego ({@link Board}), utilizado para consultar
     *              el estado de las casillas de destino.
     * @return una lista de arreglos {@code Integer[]} donde cada elemento contiene las
     *         coordenadas {@code [fila, columna]} de una casilla válida de destino.
     * @see UtilValidations#validateBoard(int, int)
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>(8);

        for (int[] offset : OFFSETS) {
            int targetRow = row + offset[0];
            int targetCol = col + offset[1];

            if (!UtilValidations.validateBoard(targetRow, targetCol)) {
                continue;
            }

            Piece targetPiece = board.getSquares()[targetRow][targetCol].getPiece();

            if (targetPiece == null || canCapture(targetPiece)) {
                movements.add(new Integer[]{targetRow, targetCol});
            }
        }

        return movements;
    }
    
}
