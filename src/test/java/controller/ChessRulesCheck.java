package controller;

import java.lang.reflect.Method;
import view.Board;
import view.Square;
import view.pieces.*;
import view.pieces.king.King;

import javax.swing.*;

/**
 * Utilidad de verificación integral de las reglas del ajedrez ejecutable sin necesidad
 * de abrir ventanas gráficas visibles.
 * <p>
 * Comprueba escenarios críticos de juego como el jaque mate (Mate del loco), el bloqueo
 * del tablero tras finalizar la partida, las restricciones y ejecución del enroque,
 * la promoción de peones, la reclamación de tablas por triple repetición y el rey ahogado.
 * </p>
 *
 * @author brysonfl
 * @see GameController
 * @see Board
 */
public class ChessRulesCheck {

    /**
     * Constructor por defecto para la clase de verificación de reglas {@code ChessRulesCheck}.
     */
    public ChessRulesCheck() {
    }

    /**
     * Invoca mediante reflexión un método declarado en {@link GameController}.
     *
     * @param controller la instancia de {@link GameController} sobre la cual se invocará el método.
     * @param name       el nombre exacto del método que se desea invocar.
     * @param types      el arreglo con los tipos de los parámetros formales del método.
     * @param args       los argumentos que se pasarán en la invocación.
     * @return el valor de retorno producido por el método invocado, o {@code null} si es {@code void}.
     * @throws Exception si el método no existe, no es accesible o lanza una excepción al ejecutarse.
     */
    private static Object call(GameController controller, String name, Class<?>[] types, Object... args) throws Exception {
        Method method = GameController.class.getDeclaredMethod(name, types);
        method.setAccessible(true);
        return method.invoke(controller, args);
    }

    /**
     * Valida que una condición booleana se cumpla, lanzando un {@link AssertionError} en caso contrario.
     *
     * @param condition la condición lógica que debe evaluarse como {@code true}.
     * @param message   el mensaje descriptivo del error en caso de que la condición falle.
     * @throws AssertionError si {@code condition} es {@code false}.
     */
    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    /**
     * Simula un movimiento en el tablero haciendo clic sobre la casilla de origen,
     * verificando que la casilla de destino esté resaltada como jugada legal y haciendo clic en ella.
     *
     * @param board     el tablero ({@link Board}) donde se realiza la jugada.
     * @param row       la fila de la casilla de origen.
     * @param col       la columna de la casilla de origen.
     * @param targetRow la fila de la casilla de destino.
     * @param targetCol la columna de la casilla de destino.
     */
    private static void move(Board board, int row, int col, int targetRow, int targetCol) {
        board.getSquares()[row][col].doClick(0);
        require(board.getSquares()[targetRow][targetCol].isHighlight(), "Movimiento legal esperado");
        board.getSquares()[targetRow][targetCol].doClick(0);
    }

    /**
     * Elimina todas las piezas de las casillas del tablero especificado, dejándolo completamente vacío.
     *
     * @param board el tablero ({@link Board}) cuyas casillas serán limpiadas.
     */
    private static void clear(Board board) {
        for (Square[] row : board.getSquares()) {
            for (Square square : row) {
                square.setPiece(null);
            }
        }
    }

    /**
     * Punto de entrada que ejecuta secuencialmente las verificaciones de reglas de ajedrez
     * en el hilo de despacho de eventos de Swing (EDT).
     *
     * @param args argumentos de la línea de comandos (no utilizados).
     * @throws Exception si ocurre un error de ejecución o falla alguna verificación en el hilo de Swing.
     */
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            try {
                Board board = new Board(null);
                GameController controller = (GameController) board.getSquares()[0][0].getActionListeners()[0];
                move(board, 6, 5, 5, 5);
                move(board, 1, 4, 3, 4);
                move(board, 6, 6, 4, 6);
                move(board, 0, 3, 4, 7);
                require(controller.getResult().startsWith("Jaque mate"), "Mate del loco");
                board.getSquares()[6][0].doClick(0);
                require(!board.getSquares()[5][0].isHighlight(), "Partida terminada");

                board = new Board(null);
                controller = (GameController) board.getSquares()[0][0].getActionListeners()[0];
                clear(board);
                board.getSquares()[7][4].setPiece(new King(true));
                board.getSquares()[7][7].setPiece(new Rook(true));
                board.getSquares()[0][4].setPiece(new King(false));
                require((boolean) call(controller, "canCastle", new Class<?>[]{Square.class, int.class}, board.getSquares()[7][4], 7), "Enroque disponible");
                board.getSquares()[0][5].setPiece(new Rook(false));
                require(!(boolean) call(controller, "canCastle", new Class<?>[]{Square.class, int.class}, board.getSquares()[7][4], 7), "Enroque a través de jaque prohibido");
                board.getSquares()[0][5].setPiece(null);
                move(board, 7, 4, 7, 6);
                require(board.getSquares()[7][5].getPiece() instanceof Rook, "Torre del enroque");
                require(board.getSquares()[7][7].getPiece() == null, "Origen de torre vacío");

                board = new Board(null);
                controller = (GameController) board.getSquares()[0][0].getActionListeners()[0];
                clear(board);
                board.getSquares()[7][7].setPiece(new King(true));
                board.getSquares()[0][7].setPiece(new King(false));
                board.getSquares()[1][0].setPiece(new Pawn(true));
                move(board, 1, 0, 0, 0);
                require(board.getSquares()[0][0].getPiece() instanceof Queen, "Promoción");

                board = new Board(null);
                controller = (GameController) board.getSquares()[0][0].getActionListeners()[0];

                for (int i = 0; i < 2; i++) {
                    move(board, 7, 6, 5, 5);
                    move(board, 0, 6, 2, 5);
                    move(board, 5, 5, 7, 6);
                    move(board, 2, 5, 0, 6);
                }

                require(controller.getResult().isEmpty(), "Triple repetición no automática");
                controller.claimDraw();
                require(controller.getResult().startsWith("Tablas"), "Reclamación por repetición");

                board = new Board(null);
                controller = (GameController) board.getSquares()[0][0].getActionListeners()[0];
                clear(board);
                board.getSquares()[7][0].setPiece(new King(true));
                board.getSquares()[5][1].setPiece(new King(false));
                board.getSquares()[6][2].setPiece(new Queen(false));
                call(controller, "updateGameState", new Class<?>[]{});
                require(controller.getResult().contains("ahogado"), "Ahogado");
                System.out.println("Reglas verificadas: mate, bloqueo final, enroque, promoción, repetición y ahogado.");
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }
}
