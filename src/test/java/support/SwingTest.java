package support;

import javax.swing.SwingUtilities;
import java.util.concurrent.FutureTask;
import view.Board;
import view.Square;

public final class SwingTest {
    private SwingTest() {}
    public static void onEdt(Runnable action) throws Exception {
        FutureTask<Void> task = new FutureTask<>(action, null);
        SwingUtilities.invokeAndWait(task);
        try { task.get(); }
        catch (java.util.concurrent.ExecutionException e) {
            if (e.getCause() instanceof Error error) throw error;
            throw new RuntimeException(e.getCause());
        }
    }
    public static Board emptyBoard() {
        Board board = new Board(null);
        for (Square[] row : board.getSquares()) for (Square square : row) square.setPiece(null);
        return board;
    }
}
