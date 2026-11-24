package controller;

import org.junit.jupiter.api.Test;
import view.*;
import view.pieces.*;
import view.pieces.king.King;
import static org.junit.jupiter.api.Assertions.*;
import static support.SwingTest.*;

/**
 * Pruebas unitarias y de flujo de partida para {@link GameController}.
 * <p>
 * Evalúa la alternancia de turnos, el bloqueo de jugadas tras la rendición,
 * la ejecución y restricciones de seguridad del rey en la captura al paso (<i>en passant</i>),
 * las tablas automáticas por quíntuple repetición y las regresiones generales de reglas.
 * </p>
 *
 * <p>Las jugadas se realizan mediante los oyentes registrados en las casillas,
 * para comprobar el flujo de selección y ejecución además del estado resultante.</p>
 *
 * @author brysonfl
 * @see GameController
 * @see ChessRulesCheck
 */
class GameControllerTest {

    /**
     * Constructor por defecto para el conjunto de pruebas de {@link GameController}.
     */
    GameControllerTest() {
    }

    /**
     * Obtiene la instancia de {@link GameController} asociada a las casillas de un tablero.
     *
     * @param b el tablero ({@link Board}) del cual extraer el controlador.
     * @return el {@link GameController} registrado como oyente de eventos en el tablero.
     */
    private static GameController controller(Board b) {
        return (GameController) b.getSquares()[0][0].getActionListeners()[0];
    }

    /**
     * Ejecuta un movimiento en el tablero simulando los clics de selección de origen y destino,
     * asegurando previamente que la casilla de destino esté marcada como válida.
     *
     * @param b  el tablero ({@link Board}) sobre el que se realiza la jugada.
     * @param r  la fila de la casilla de origen.
     * @param c  la columna de la casilla de origen.
     * @param tr la fila de la casilla de destino.
     * @param tc la columna de la casilla de destino.
     */
    private static void move(Board b, int r, int c, int tr, int tc) {
        b.getSquares()[r][c].doClick(0);
        assertTrue(b.getSquares()[tr][tc].isHighlight(), "Destino legal esperado");
        b.getSquares()[tr][tc].doClick(0);
    }

    /**
     * Ejecuta la batería de comprobaciones de {@link ChessRulesCheck} para prevenir regresiones
     * en las reglas fundamentales del juego.
     *
     * @throws Exception si falla alguna verificación dentro de {@link ChessRulesCheck#main(String[])}.
     */
    @Test void existingRuleRegressions() throws Exception {
        ChessRulesCheck.main(new String[0]);
    }

    /**
     * Verifica que el controlador respete el orden de turnos entre blancas y negras,
     * y que una vez declarada la rendición ({@link GameController#resign()}) no se permitan
     * nuevos movimientos ni cambios en el resultado de la partida.
     *
     * @throws Exception si ocurre un error durante la ejecución en el EDT.
     */
    @Test void turnsAndResignationBlockFurtherPlay() throws Exception {
        onEdt(() -> {
            Board b = new Board(null);
            GameController c = controller(b);

            b.getSquares()[1][0].doClick(0);
            assertFalse(b.getSquares()[2][0].isHighlight());

            move(b, 6, 4, 4, 4);

            b.getSquares()[6][3].doClick(0);
            assertFalse(b.getSquares()[5][3].isHighlight());

            c.resign();
            String result = c.getResult();
            assertTrue(result.contains("blancas"));

            c.resign();
            c.claimDraw();
            c.offerDraw();
            assertEquals(result, c.getResult());

            b.getSquares()[1][0].doClick(0);
            assertFalse(b.getSquares()[2][0].isHighlight());
        });
    }

    /**
     * Comprueba que al ejecutar una captura al paso (<i>en passant</i>), el peón atacante
     * ocupe la casilla destino, el peón adversario capturado al paso sea retirado del tablero
     * y la referencia a la casilla al paso se limpie.
     *
     * @throws Exception si ocurre un error durante la ejecución en el EDT.
     */
    @Test void enPassantRemovesOnlyCapturedPawn() throws Exception {
        onEdt(() -> {
            Board b = new Board(null);
            move(b, 6, 4, 4, 4);
            move(b, 1, 0, 2, 0);
            move(b, 4, 4, 3, 4);
            move(b, 1, 3, 3, 3);

            Piece pawn = b.getSquares()[3][4].getPiece();
            move(b, 3, 4, 2, 3);

            assertSame(pawn, b.getSquares()[2][3].getPiece());
            assertNull(b.getSquares()[3][3].getPiece());
            assertNull(b.getEnPassantSquare());
        });
    }

    /**
     * Verifica que una captura al paso no sea permitida si al retirar ambos peones de la fila
     * el propio rey queda expuesto en jaque horizontal ante una pieza enemiga, y comprueba
     * que la simulación restaure correctamente el peón enemigo en su casilla.
     *
     * @throws Exception si ocurre un error durante la ejecución en el EDT.
     */
    @Test void enPassantCannotExposeKing() throws Exception {
        onEdt(() -> {
            Board b = emptyBoard();
            b.getSquares()[3][7].setPiece(new King(true));
            b.getSquares()[0][7].setPiece(new King(false));
            b.getSquares()[3][0].setPiece(new Rook(false));
            b.getSquares()[3][4].setPiece(new Pawn(true));

            Square victim = b.getSquares()[3][3];
            Piece pawn = new Pawn(false);
            victim.setPiece(pawn);

            b.setEnPassantSquare(victim);
            b.getSquares()[3][4].doClick(0);

            assertFalse(b.getSquares()[2][3].isHighlight());
            assertSame(pawn, victim.getPiece(), "La simulación restaura la pieza");
        });
    }

    /**
     * Comprueba que la repetición quíntuple de la misma posición declare tablas de manera
     * automática sin requerir reclamación manual por parte de los jugadores.
     *
     * @throws Exception si ocurre un error durante la ejecución en el EDT.
     */
    @Test void fivefoldRepetitionIsAutomatic() throws Exception {
        onEdt(() -> {
            Board b = new Board(null);
            for (int i = 0; i < 4; i++) {
                move(b, 7, 6, 5, 5);
                move(b, 0, 6, 2, 5);
                move(b, 5, 5, 7, 6);
                move(b, 2, 5, 0, 6);
            }
            assertTrue(controller(b).getResult().contains("quíntuple"));
        });
    }
}
