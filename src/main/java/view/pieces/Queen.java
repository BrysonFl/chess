package view.pieces;

import enums.PieceType;
import java.util.ArrayList;
import java.util.List;
import utils.UtilValidations;
import view.Board;

/**
 * Representa la pieza de ajedrez Reina o Dama ({@link PieceType#QUEEN}).
 * <p>
 * Esta clase extiende {@link Piece} y combina las capacidades de movimiento de la torre
 * ({@link Rook}) y del alfil ({@link Bishop}). Al ser la pieza de mayor alcance en el tablero,
 * puede desplazarse cualquier número de casillas desocupadas en línea recta a lo largo de
 * las ocho direcciones (horizontal, vertical y las cuatro diagonales).
 * </p>
 *
 * @author brysonfl
 * @see Piece
 * @see PieceType#QUEEN
 * @see Rook
 * @see Bishop
 * @see Board
 */
public class Queen extends Piece {
    
    /**
     * Construye una nueva instancia de {@code Queen} con el color especificado.
     *
     * @param isWhite define si la reina pertenece al bando blanco ({@code true})
     *                o negro ({@code false}).
     */
    public Queen(boolean isWhite) {
        super(isWhite, PieceType.QUEEN);
    }

    /**
     * Calcula y retorna todos los movimientos válidos disponibles para la reina
     * desde su posición actual en el tablero.
     * <p>
     * Evalúa secuencialmente las ocho direcciones posibles (diagonales superiores,
     * vertical hacia arriba, diagonales inferiores, vertical hacia abajo y horizontales
     * hacia derecha e izquierda). En cada dirección avanza casilla por casilla mientras
     * las coordenadas sean válidas según {@link UtilValidations#validateBoard(int, int)},
     * deteniéndose al encontrar una pieza: si es capturable conforme a {@link #canCapture(Piece)},
     * incluye esa casilla antes de interrumpir el recorrido de dicha trayectoria.
     * </p>
     *
     * @param row   el índice de la fila actual (0 a 7) en la que se encuentra la reina.
     * @param col   el índice de la columna actual (0 a 7) en la que se encuentra la reina.
     * @param board el tablero actual del juego ({@link Board}), utilizado para verificar
     *              la ocupación de las casillas en cada dirección.
     * @return una lista de arreglos {@code Integer[]} donde cada elemento contiene las
     *         coordenadas {@code [fila, columna]} de una casilla válida de destino.
     * @see UtilValidations#validateBoard(int, int)
     */
    @Override
    public List<Integer[]> validMovements(int row, int col, Board board) {
        List<Integer[]> movements = new ArrayList<>();
        
        int targetCol;
        targetCol = col;
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            targetCol++;
            
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][targetCol].getPiece())) {
                    movements.add(new Integer[]{ targetRow, targetCol });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        targetCol = col;
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][targetCol].getPiece())) {
                    movements.add(new Integer[]{ targetRow, targetCol });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        targetCol = col;
        
        for (int targetRow = row - 1; targetRow >= 0; targetRow--) {
            targetCol--;
            
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][targetCol].getPiece())) {
                    movements.add(new Integer[]{ targetRow, targetCol });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        targetCol = col;
        
        for (int targetRow = row + 1; targetRow < 8; targetRow++) {
            targetCol--;
            
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][targetCol].getPiece())) {
                    movements.add(new Integer[]{ targetRow, targetCol });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        targetCol = col;
        
        for (int targetRow = row + 1; targetRow < 8; targetRow++) {
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][targetCol].getPiece())) {
                    movements.add(new Integer[]{ targetRow, targetCol });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        targetCol = col;
        
        for (int targetRow = row + 1; targetRow < 8; targetRow++) {
            targetCol++;
            
            if (!UtilValidations.validateBoard(targetRow, targetCol)) break;
            if (board.getSquares()[targetRow][targetCol].getPiece() != null) {
                if (canCapture(board.getSquares()[targetRow][targetCol].getPiece())) {
                    movements.add(new Integer[]{ targetRow, targetCol });
                }
                break;
            }
            
            movements.add(new Integer[]{ targetRow, targetCol });
        }
        
        for (int targetColFor = col + 1; targetColFor < 8; targetColFor++) {
            if (!UtilValidations.validateBoard(row, targetColFor)) break;
            if (board.getSquares()[row][targetColFor].getPiece() != null) {
                if (canCapture(board.getSquares()[row][targetColFor].getPiece())) {
                    movements.add(new Integer[]{ row, targetColFor });
                }
                break;
            }
            
            movements.add(new Integer[]{ row, targetColFor });
        }
        
        for (int targetColFor = col - 1; targetColFor >= 0; targetColFor--) {
            if (!UtilValidations.validateBoard(row, targetColFor)) break;
            if (board.getSquares()[row][targetColFor].getPiece() != null) {
                if (canCapture(board.getSquares()[row][targetColFor].getPiece())) {
                    movements.add(new Integer[]{ row, targetColFor });
                }
                break;
            }
            
            movements.add(new Integer[]{ row, targetColFor });
        }
        
        return movements;
    }
    
}
