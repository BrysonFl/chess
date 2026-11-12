package controller;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.List;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.Board;
import view.Square;
import view.pieces.Pawn;
import view.pieces.Piece;
import view.pieces.Rook;
import enums.PieceType;

import static org.junit.jupiter.api.Assertions.*;

/** Prueba los métodos privados a través de actionPerformed, sin cambiar su visibilidad. */
class GameControllerTest {
    private Board board;
    private GameController controller;

    @BeforeEach
    void setUp() throws Exception {
        resetState();
        SwingUtilities.invokeAndWait(() -> {
            board = new Board();
            controller = new GameController(board);
            for (Square[] row : board.getSquares()) {
                for (Square square : row) {
                    square.setPiece(null);
                }
            }
        });
    }

    @AfterEach
    void tearDown() throws Exception {
        resetState();
    }

    // El controlador actual comparte estado estático. Se restaura incluso si falla una prueba.
    private static void resetState() throws Exception {
        state("whitesMove").set(null, true);
        state("generalMovements").set(null, 0);
        state("movementsWithoutCaptures").set(null, 0);
        state("beforeSquare").set(null, null);
    }

    private static Field state(String name) throws Exception {
        Field field = GameController.class.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    private Square square(int row, int col) {
        return board.getSquares()[row][col];
    }

    private void click(int row, int col) throws Exception {
        SwingUtilities.invokeAndWait(() -> controller.actionPerformed(
                new ActionEvent(square(row, col), ActionEvent.ACTION_PERFORMED, "click")));
    }

    private void put(int row, int col, Piece piece) throws Exception {
        SwingUtilities.invokeAndWait(() -> square(row, col).setPiece(piece));
    }

    private void assertNoHighlights() {
        for (Square[] row : board.getSquares()) {
            for (Square square : row) {
                assertFalse(square.isHighlight(), "Resaltado inesperado: " + square);
            }
        }
    }

    @Test
    void constructorUsesProvidedBoard() throws Exception {
        assertSame(board, state("board").get(controller));
    }

    @Test
    void actionPerformedIgnoresEmptySquareWithoutSelection() throws Exception {
        click(4, 4);
        assertNull(state("beforeSquare").get(null));
        assertEquals(0, state("generalMovements").get(null));
        assertEquals(true, state("whitesMove").get(null));
        assertNoHighlights();
    }

    @Test
    void actionPerformedSelectsPiece() throws Exception {
        put(6, 3, new Pawn(true));
        click(6, 3);
        assertSame(square(6, 3), state("beforeSquare").get(null));
        assertTrue(square(5, 3).isHighlight());
        assertTrue(square(4, 3).isHighlight());
        assertEquals(0, state("generalMovements").get(null));
    }

    @Test
    void actionPerformedCancelsSelectionOnInvalidEmptyDestination() throws Exception {
        put(6, 3, new Pawn(true));
        click(6, 3);
        click(4, 4);
        assertNull(state("beforeSquare").get(null));
        assertNotNull(square(6, 3).getPiece());
        assertNull(square(4, 4).getPiece());
        assertEquals(0, state("generalMovements").get(null));
        assertNoHighlights();
    }

    @Test
    void actionPerformedReplacesSelectionWithAnotherFriendlyPiece() throws Exception {
        put(6, 2, new Pawn(true));
        put(6, 5, new Pawn(true));
        click(6, 2);
        click(6, 5);
        assertSame(square(6, 5), state("beforeSquare").get(null));
        assertFalse(square(5, 2).isHighlight());
        assertTrue(square(5, 5).isHighlight());
    }

    @Test
    void validatePieceMovementsHighlightsOnlyReturnedCoordinates() throws Exception {
        Piece piece = new Piece(true, PieceType.ROOK) {
            @Override
            public List<Integer[]> validMovements(int row, int col, Board actualBoard) {
                assertEquals(3, row);
                assertEquals(3, col);
                assertSame(board, actualBoard);
                return List.of(new Integer[]{2, 3}, new Integer[]{3, 5});
            }
        };
        put(3, 3, piece);
        click(3, 3);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals((row == 2 && col == 3) || (row == 3 && col == 5),
                        square(row, col).isHighlight());
            }
        }
    }

    @Test
    void validatePieceMovementsRejectsBlackOnWhiteTurn() throws Exception {
        put(1, 3, new Pawn(false));
        click(1, 3);
        assertNoHighlights();
        click(2, 3);
        assertNotNull(square(1, 3).getPiece());
        assertNull(square(2, 3).getPiece());
        assertEquals(0, state("generalMovements").get(null));
    }

    @Test
    void validatePieceMovementsRejectsWhiteOnBlackTurn() throws Exception {
        put(6, 3, new Pawn(true));
        click(6, 3);
        click(5, 3);
        click(5, 3);
        assertNoHighlights();
    }

    @Test
    void validatePieceMovementsAcceptsBlackAfterWhiteMove() throws Exception {
        put(6, 3, new Pawn(true));
        put(1, 4, new Pawn(false));
        click(6, 3);
        click(5, 3);
        click(1, 4);
        assertTrue(square(2, 4).isHighlight());
        assertTrue(square(3, 4).isHighlight());
    }

    @Test
    void validatePieceMovementsHandlesPieceWithoutMoves() throws Exception {
        put(6, 3, new Pawn(true));
        put(5, 3, new Pawn(false));
        click(6, 3);
        assertNoHighlights();
    }

    @Test
    void movePieceTransfersPieceAndChangesTurnAndCounter() throws Exception {
        Piece pawn = new Pawn(true);
        put(6, 3, pawn);
        click(6, 3);
        click(5, 3);
        assertSame(pawn, square(5, 3).getPiece());
        assertNull(square(6, 3).getPiece());
        assertNull(state("beforeSquare").get(null));
        assertEquals(1, state("generalMovements").get(null));
        assertEquals(false, state("whitesMove").get(null));
        assertNoHighlights();
    }

    @Test
    void movePieceSupportsPawnDoubleAdvance() throws Exception {
        Piece pawn = new Pawn(true);
        put(6, 3, pawn);
        click(6, 3);
        click(4, 3);
        assertSame(pawn, square(4, 3).getPiece());
        assertNull(square(6, 3).getPiece());
    }

    @Test
    void movePieceCapturesWithNonPawn() throws Exception {
        // Aísla el controlador: Rook.validMovements actualmente omite las capturas.
        Piece rook = new Rook(true) {
            @Override
            public List<Integer[]> validMovements(int row, int col, Board board) {
                return java.util.Collections.singletonList(new Integer[]{4, 5});
            }
        };
        put(4, 3, rook);
        put(4, 5, new Pawn(false));
        click(4, 3);
        click(4, 5);
        assertSame(rook, square(4, 5).getPiece());
        assertNull(square(4, 3).getPiece());
        assertEquals(1, state("generalMovements").get(null));
        assertNoHighlights();
    }

    @Test
    void movePiecePawnDiagonalCaptureDoesNotRemoveAdjacentPiece() throws Exception {
        Piece pawn = new Pawn(true);
        Piece adjacent = new Rook(false);
        put(4, 3, pawn);
        put(3, 4, new Pawn(false));
        put(4, 4, adjacent);
        click(4, 3);
        click(3, 4);
        assertSame(pawn, square(3, 4).getPiece());
        assertSame(adjacent, square(4, 4).getPiece());
    }

    @Test
    void movePieceExecutesWhiteEnPassantImmediatelyAfterDoubleAdvance() throws Exception {
        Piece white = new Pawn(true);
        put(6, 0, new Pawn(true));
        put(3, 3, white);
        put(1, 4, new Pawn(false));
        click(6, 0);
        click(5, 0);
        click(1, 4);
        click(3, 4);
        click(3, 3);
        assertTrue(square(2, 4).isHighlight());
        click(2, 4);
        assertSame(white, square(2, 4).getPiece());
        assertNull(square(3, 3).getPiece());
        assertNull(square(3, 4).getPiece());
        assertEquals(3, state("generalMovements").get(null));
    }

    @Test
    void movePieceExecutesBlackEnPassantImmediatelyAfterDoubleAdvance() throws Exception {
        Piece black = new Pawn(false);
        put(4, 3, black);
        put(6, 4, new Pawn(true));
        click(6, 4);
        click(4, 4);
        click(4, 3);
        assertTrue(square(5, 4).isHighlight());
        click(5, 4);
        assertSame(black, square(5, 4).getPiece());
        assertNull(square(4, 3).getPiece());
        assertNull(square(4, 4).getPiece());
        assertEquals(true, state("whitesMove").get(null));
        assertEquals(2, state("generalMovements").get(null));
    }

    @Test
    void clearHighlightsClearsAllSquaresOnEmptyClick() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            for (Square[] row : board.getSquares()) {
                for (Square square : row) {
                    square.setHighlight(true);
                }
            }
            square(4, 4).setHighlight(false);
        });
        click(4, 4);
        assertNoHighlights();
    }

    @Test
    void clearHighlightsRemovesPreviousMovesWhenSelectingOpponent() throws Exception {
        put(6, 3, new Pawn(true));
        put(1, 4, new Pawn(false));
        click(6, 3);
        click(1, 4);
        assertNoHighlights();
        assertEquals(0, state("generalMovements").get(null));
    }

    @Test
    void historyRecordsOnlyCompletedMovesInPlayerColumns() throws Exception {
        put(6, 4, new Pawn(true));
        put(1, 4, new Pawn(false));
        javax.swing.JScrollPane scroll = (javax.swing.JScrollPane)
                board.getMovementsPanel().getComponent(1);
        javax.swing.JTable history = (javax.swing.JTable) scroll.getViewport().getView();
        click(6, 4);
        click(4, 3); // Destino inválido: no debe registrar una jugada.
        assertEquals(0, history.getRowCount());
        click(6, 4);
        click(4, 4);
        assertEquals(1, history.getRowCount());
        assertEquals("P e2 → e4", history.getValueAt(0, 1));
        assertEquals("", history.getValueAt(0, 2));
        click(1, 4);
        click(3, 4);
        assertEquals(1, history.getRowCount());
        assertEquals("P e7 → e5", history.getValueAt(0, 2));
        click(4, 4);
        click(4, 3);
        assertEquals(1, history.getRowCount());
    }

    @Test
    void historyMarksPawnCapture() throws Exception {
        put(4, 3, new Pawn(true));
        put(3, 4, new Pawn(false));
        click(4, 3);
        click(3, 4);
        javax.swing.JScrollPane scroll = (javax.swing.JScrollPane)
                board.getMovementsPanel().getComponent(1);
        javax.swing.JTable history = (javax.swing.JTable) scroll.getViewport().getView();
        assertEquals("P d4 × e5", history.getValueAt(0, 1));
    }


    @Test
    void checkingMoveShowsHaloAndOnlyAllowsResponsesThatSaveKing() throws Exception {
        put(7, 7, new view.pieces.King(true));
        put(0, 4, new view.pieces.King(false));
        put(6, 0, new Rook(true));
        put(1, 0, new Rook(false));
        click(6, 0);
        click(6, 4);
        assertTrue(square(0, 4).isInCheck());
        javax.swing.JScrollPane scroll = (javax.swing.JScrollPane)
                board.getMovementsPanel().getComponent(1);
        javax.swing.JTable history = (javax.swing.JTable) scroll.getViewport().getView();
        assertEquals("T a2 → e2 +", history.getValueAt(0, 1));
        click(1, 0);
        assertFalse(square(2, 0).isHighlight());
        assertTrue(square(1, 4).isHighlight());
        click(1, 4);
        assertFalse(square(0, 4).isInCheck());
        assertInstanceOf(Rook.class, square(1, 4).getPiece());
    }

}
