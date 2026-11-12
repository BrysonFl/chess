package utils;

import enums.PieceType;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import view.Board;
import view.Square;
import view.pieces.*;

import static org.junit.jupiter.api.Assertions.*;

class CheckRulesTest {
    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            board = new Board();
            for (Square[] row : board.getSquares()) {
                for (Square square : row) square.setPiece(null);
            }
        });
    }

    private void put(int row, int col, Piece piece) {
        board.getSquares()[row][col].setPiece(piece);
    }

    private boolean safe(int r, int c, int tr, int tc) {
        return CheckRules.leavesKingSafe(board, board.getSquares()[r][c], board.getSquares()[tr][tc]);
    }

    @ParameterizedTest
    @EnumSource(PieceType.class)
    void detectsChecksFromEveryPieceForBothColors(PieceType type) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            for (boolean white : new boolean[]{true, false}) {
                for (Square[] row : board.getSquares()) for (Square square : row) square.setPiece(null);
                put(4, 4, new King(!white));
                switch (type) {
                    case PAWN -> put(white ? 5 : 3, 3, new Pawn(white));
                    case ROOK -> put(4, 0, new Rook(white));
                    case BISHOP -> put(0, 0, new Bishop(white));
                    case QUEEN -> put(4, 0, new Queen(white));
                    case KNIGHT -> put(2, 3, new Knight(white));
                    case KING -> put(3, 3, new King(white));
                }
                assertTrue(CheckRules.isInCheck(board, !white), type.toString());
            }
        });
    }

    @Test
    void blockedRayAndPawnAdvanceDoNotGiveCheck() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            put(4, 4, new King(true));
            put(4, 0, new Rook(false));
            put(4, 2, new Pawn(true));
            put(3, 4, new Pawn(false));
            assertFalse(CheckRules.isInCheck(board, true));
            put(4, 2, null);
            assertTrue(CheckRules.isInCheck(board, true));
        });
    }

    @Test
    void pinnedPieceMustKeepBlockingAndSimulationPreservesBoard() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            put(7, 4, new King(true));
            Rook pinned = new Rook(true);
            put(6, 4, pinned);
            put(0, 4, new Rook(false));
            assertFalse(safe(6, 4, 6, 3));
            assertTrue(safe(6, 4, 5, 4));
            assertTrue(safe(6, 4, 0, 4));
            assertSame(pinned, board.getSquares()[6][4].getPiece());
            assertNull(board.getSquares()[6][3].getPiece());
            assertInstanceOf(Rook.class, board.getSquares()[0][4].getPiece());
        });
    }

    @Test
    void checkCanBeBlockedOrCapturedButUnrelatedMoveIsRejected() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            put(7, 4, new King(true));
            put(0, 4, new Rook(false));
            put(5, 0, new Rook(true));
            assertTrue(CheckRules.isInCheck(board, true));
            assertTrue(safe(5, 0, 5, 4));
            assertFalse(safe(5, 0, 5, 1));
            put(5, 0, null);
            put(0, 0, new Rook(true));
            assertTrue(safe(0, 0, 0, 4));
        });
    }

    @Test
    void kingCannotCaptureDefendedPieceOrEnemyKingOrEnterPawnAttack() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            put(4, 4, new King(true));
            put(3, 4, new Rook(false));
            put(0, 4, new Rook(false));
            assertFalse(safe(4, 4, 3, 4));
            put(3, 4, new King(false));
            assertFalse(safe(4, 4, 3, 4));
            put(3, 4, null);
            put(0, 4, null);
            put(2, 2, new Pawn(false));
            assertFalse(safe(4, 4, 3, 3));
            assertTrue(safe(4, 4, 3, 4));
        });
    }

    @Test
    void enPassantCannotExposeRookAttack() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            put(3, 7, new King(true));
            put(3, 0, new Rook(false));
            put(3, 4, new Pawn(true));
            put(3, 3, new Pawn(false));
            assertFalse(CheckRules.isInCheck(board, true));
            assertFalse(safe(3, 4, 2, 3));
            assertNotNull(board.getSquares()[3][3].getPiece());
        });
    }

    @Test
    void indicatorsClearAfterCheckIsResolved() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            put(7, 4, new King(true));
            put(0, 4, new Rook(false));
            CheckRules.updateIndicators(board);
            assertTrue(board.getSquares()[7][4].isInCheck());
            put(5, 4, new Bishop(true));
            CheckRules.updateIndicators(board);
            for (Square[] row : board.getSquares()) {
                for (Square square : row) assertFalse(square.isInCheck());
            }
        });
    }
}
