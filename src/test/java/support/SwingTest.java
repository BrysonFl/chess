package support;

import javax.swing.SwingUtilities;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import view.Board;
import view.Square;

/**
 * Clase de utilidad para pruebas que facilita la ejecución segura de operaciones
 * en el hilo de despacho de eventos de Swing (EDT) y la preparación de tableros de prueba.
 * <p>
 * Proporciona métodos estáticos para sincronizar aserciones de interfaz gráfica en el EDT
 * propagando errores de JUnit, así como para instanciar tableros limpios sin piezas.
 * </p>
 *
 * <p>El método onEdt debe invocarse fuera del EDT porque utiliza invokeAndWait.
 * emptyBoard conserva las casillas y el controlador del tablero recién creado.</p>
 *
 * @author brysonfl
 * @see SwingUtilities
 * @see Board
 */
public final class SwingTest {

    /**
     * Constructor privado para evitar la instanciación de esta clase de utilidad.
     */
    private SwingTest() {}

    /**
     * Ejecuta una acción de forma síncrona dentro del hilo de despacho de eventos de Swing (EDT)
     * y propaga cualquier {@link Error} (como fallos de aserción) o excepción ocurrida durante su ejecución.
     *
     * @param action la tarea ({@link Runnable}) que se ejecutará en el EDT.
     * @throws Exception si el hilo es interrumpido o si ocurre un error al despachar la tarea en Swing.
     */
    public static void onEdt(Runnable action) throws Exception {
        FutureTask<Void> task = new FutureTask<>(action, null);
        SwingUtilities.invokeAndWait(task);

        try {
            task.get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof Error error) throw error;
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * Crea y retorna una nueva instancia de {@link Board} con todas sus casillas vacías (sin piezas).
     *
     * @return un objeto {@link Board} de {@code 8x8} casillas sin ninguna pieza colocada.
     */
    public static Board emptyBoard() {
        Board board = new Board(null);

        for (Square[] row : board.getSquares())
            for (Square square : row) square.setPiece(null);

        return board;
    }
}
