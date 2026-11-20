package view.pieces;

import view.Board;
import enums.PieceType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Square;

import javax.swing.*;

/**
 * Representa la pieza Peón dentro del juego de ajedrez.
 * <p>
 * Esta clase extiende {@link Piece} y gestiona las reglas de movimiento específicas
 * del peón, incluyendo el avance simple hacia adelante, el avance doble desde su
 * fila inicial, las capturas diagonales estándar, la captura al paso (<i>en passant</i>)
 * y el proceso de coronación o promoción al alcanzar la última fila del tablero.
 * </p>
 *
 * @author brysonfl
 * @see Piece
 * @see PieceType#PAWN
 * @see Board
 */
public class Pawn extends Piece {

    /**
     * Construye una nueva instancia de un peón con el color especificado.
     *
     * @param isWhite indica si el peón pertenece al bando de las blancas ({@code true})
     *                o al de las negras ({@code false}).
     */
    public Pawn(boolean isWhite) {
        super(isWhite, PieceType.PAWN);
    }

    /**
     * Calcula y retorna una lista con todos los movimientos válidos según la geometría
     * y estado del tablero para el peón desde su posición actual.
     * <p>
     * Evalúa los siguientes desplazamientos:
     * </p>
     * <ul>
     *   <li>Avance frontal de una casilla si el destino se encuentra vacío.</li>
     *   <li>Avance frontal de dos casillas si el peón está en su fila inicial y tanto
     *       la casilla intermedia como la de destino están libres.</li>
     *   <li>Capturas diagonales de una casilla hacia adelante si contienen una pieza enemiga capturable.</li>
     *   <li>Captura al paso (<i>en passant</i>) si el peón se encuentra en la fila correspondiente
     *       y existe un peón adversario adyacente marcado como objetivo al paso en el {@link Board}.</li>
     * </ul>
     *
     * @param row   la fila actual en la que se encuentra el peón (de {@code 0} a {@code 7}).
     * @param col   la columna actual en la que se encuentra el peón (de {@code 0} a {@code 7}).
     * @param board el tablero actual del juego ({@link Board}), utilizado para inspeccionar
     *              el estado de las casillas y el objetivo de captura al paso.
     * @return una lista de arreglos {@code Integer[]} donde cada elemento contiene las coordenadas
     *         {@code [fila, columna]} de una casilla destino válida para el peón.
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>();
        Square[][] squares = board.getSquares();
        
        int direction = isWhite() ? -1 : 1;
        int startRow = isWhite() ? 6 : 1;
        
        int targetRow = row + direction;
        
        if (UtilValidations.validateBoard(targetRow, col) && squares[targetRow][col].getPiece() == null) {
            movements.add(new Integer[] { targetRow, col });
            
            int doubleTargetRow = row + (2 * direction);
            
            if (row == startRow && UtilValidations.validateBoard(doubleTargetRow, col) && squares[doubleTargetRow][col].getPiece() == null) {
                movements.add(new Integer[]{ doubleTargetRow, col });
            }
        }
        
        int[] captureCols = new int[]{ col - 1, col + 1};
        
        for (int captureSingleCol : captureCols) {
            if (UtilValidations.validateBoard(targetRow, captureSingleCol)
                    && squares[targetRow][captureSingleCol].getPiece() != null
                    && canCapture(squares[targetRow][captureSingleCol].getPiece())) {
                movements.add(new Integer[]{ targetRow, captureSingleCol });
            }
        }
        
        int rowPassant = isWhite() ? 3 : 4;

        if (rowPassant == row) {
            for (int captureSingleCol : captureCols) {
                if (UtilValidations.validateBoard(targetRow, captureSingleCol)
                        && squares[targetRow][captureSingleCol].getPiece() == null
                        && board.getEnPassantSquare() == squares[row][captureSingleCol]
                        && squares[row][captureSingleCol].getPiece() instanceof Pawn
                        && canCapture(squares[row][captureSingleCol].getPiece())) {
                    movements.add(new Integer[]{ targetRow, captureSingleCol });
                }
            }
        }
        
        return movements;
    }

    /**
     * Determina si un desplazamiento realizado por el peón corresponde a una captura al paso (<i>en passant</i>).
     * <p>
     * Un movimiento de peón se considera captura al paso cuando cambia de columna (movimiento diagonal)
     * hacia una casilla que no tenía una pieza capturada directamente en ella.
     * </p>
     *
     * @param beforeSquare  la casilla de origen ({@link Square}) desde la cual partió el peón.
     * @param currentSquare la casilla de destino ({@link Square}) a la que llegó el peón.
     * @param capture       {@code true} si existía una pieza directamente en la casilla de destino;
     *                      {@code false} si la casilla de destino estaba vacía.
     * @return {@code true} si el movimiento fue diagonal hacia una casilla vacía (captura al paso);
     *         {@code false} en caso contrario.
     */
    public boolean validateIsPassant(Square beforeSquare, Square currentSquare, boolean capture) {
        return beforeSquare.getCol() != currentSquare.getCol() && !capture;
    }

    /**
     * Solicita la selección de la pieza por la cual se promoverá el peón al coronar.
     * <p>
     * Si el entorno gráfico está activo, muestra un cuadro de diálogo modal ({@link JOptionPane})
     * permitiendo elegir entre {@link Queen} (Reina), {@link Rook} (Torre), {@link Bishop} (Alfil)
     * o {@link Knight} (Caballo). En entornos sin pantalla (<i>headless</i>) o si se cierra el diálogo
     * sin elegir otra opción, selecciona por defecto una {@link Queen}.
     * </p>
     *
     * @param white indica el color de la nueva pieza promovida ({@code true} para blanca,
     *              {@code false} para negra).
     * @param board el componente {@link Board} que actúa como contenedor padre del cuadro de diálogo.
     * @return la nueva instancia de {@link Piece} seleccionada para reemplazar al peón coronado.
     */
    public Piece choosePromotion(boolean white, Board board) {
        String[] options = { "Reina", "Torre", "Alfil", "Caballo" };

        int choice = GraphicsEnvironment.isHeadless() ? 0 : JOptionPane.showOptionDialog(board,
                "Elige la pieza para coronar el peón", "Promoción", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        return switch (choice) {
            case 1 -> new Rook(white);
            case 2 -> new Bishop(white);
            case 3 -> new Knight(white);
            default -> new Queen(white);
        };
    }
    
}
