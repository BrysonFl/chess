package controller;

import java.lang.reflect.Method;
import view.Board;
import view.Square;
import view.pieces.*;
import view.pieces.king.King;

/** Verificaciones de reglas ejecutables sin abrir ventanas. */
public class ChessRulesCheck {
    private static Object call(GameController controller, String name, Class<?>[] types, Object... args) throws Exception {
        Method method = GameController.class.getDeclaredMethod(name, types);
        method.setAccessible(true);
        return method.invoke(controller, args);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void move(Board board, int row, int col, int targetRow, int targetCol) {
        board.getSquares()[row][col].doClick(0);
        require(board.getSquares()[targetRow][targetCol].isHighlight(), "Movimiento legal esperado");
        board.getSquares()[targetRow][targetCol].doClick(0);
    }

    private static void clear(Board board) {
        for (Square[] row : board.getSquares()) for (Square square : row) square.setPiece(null);
    }

    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeAndWait(() -> {
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
                    move(board, 7, 6, 5, 5); move(board, 0, 6, 2, 5);
                    move(board, 5, 5, 7, 6); move(board, 2, 5, 0, 6);
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
