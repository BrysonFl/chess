import org.junit.jupiter.api.*;
import view.WindowGame;
import view.frames.ResignGameWindow;
import view.buttons.ResignButton;
import view.Board;
import controller.GameController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import static org.junit.jupiter.api.Assertions.*;
import static support.SwingTest.*;

/**
 * Conjunto de pruebas de integración para las ventanas y cuadros de diálogo reales de la interfaz gráfica.
 * <p>
 * Verifica la creación de la ventana principal desde el punto de entrada de la aplicación,
 * la personalización del título en {@link WindowGame} y la interacción con el cuadro de diálogo
 * de confirmación de abandono ({@link ResignGameWindow} y {@link ResignButton}).
 * Estas pruebas requieren un entorno gráfico activo y se ejecutan con {@code -PuiTests=true}.
 * </p>
 *
 * @author brysonfl
 * @see WindowGame
 * @see ResignGameWindow
 * @see ResignButton
 */
@Tag("ui")
class WindowIntegrationTest {

    /**
     * Constructor por defecto para el conjunto de pruebas de integración de ventanas.
     */
    WindowIntegrationTest() {
    }

    /**
     * Verifica antes de cada prueba que el entorno de ejecución disponga de pantalla gráfica
     * (no sea <i>headless</i>), omitiendo la prueba en caso contrario.
     */
    @BeforeEach void needsDisplay() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());
    }

    /**
     * Cierra y libera todas las ventanas abiertas en el hilo de despacho de eventos (EDT)
     * después de cada prueba para evitar interferencias entre casos.
     *
     * @throws Exception si ocurre un error durante la ejecución en el hilo de eventos de Swing.
     */
    @AfterEach void closeWindows() throws Exception {
        onEdt(() -> {
            for (Window w : Window.getWindows()) {
                w.dispose();
            }
        });
    }

    /**
     * Comprueba que el método principal {@link GameMain#main(String[])} cree y muestre
     * una instancia visible de {@link WindowGame}.
     *
     * @throws Exception si ocurre una excepción al ejecutar la prueba en el EDT.
     */
    @Test void applicationEntryPointCreatesVisibleGame() throws Exception {
        onEdt(() -> {
            GameMain.main(new String[0]);
            assertTrue(java.util.Arrays.stream(Window.getWindows()).anyMatch(w -> w instanceof WindowGame && w.isShowing()));
        });
    }

    /**
     * Verifica que {@link WindowGame} asigne correctamente un título personalizado cuando
     * se proporciona una cadena no vacía en su constructor y que la ventana sea visible.
     *
     * @throws Exception si ocurre una excepción al ejecutar la prueba en el EDT.
     */
    @Test void customWindowTitle() throws Exception {
        onEdt(() -> {
            WindowGame w = new WindowGame("Prueba");
            assertEquals("Prueba", w.getTitle());
            assertTrue(w.isShowing());
        });
    }

    /**
     * Busca recursivamente un componente {@link JOptionPane} dentro de un contenedor
     * y establece el valor de respuesta indicado.
     *
     * @param c      el contenedor raíz ({@link Container}) donde buscar el panel de opciones.
     * @param answer el código de opción (por ejemplo, {@link JOptionPane#YES_OPTION} o {@link JOptionPane#NO_OPTION})
     *               que se asignará al diálogo.
     */
    private static void answer(Container c, int answer) {
        for (Component child : c.getComponents()) {
            if (child instanceof JOptionPane pane) {
                pane.setValue(answer);
            } else if (child instanceof Container container) {
                answer(container, answer);
            }
        }
    }

    /**
     * Crea e inicia un temporizador ({@link Timer}) que responde automáticamente a cualquier
     * cuadro de diálogo modal ({@link JDialog}) visible con la opción especificada.
     *
     * @param choice la opción de respuesta a simular en el diálogo ({@link JOptionPane#YES_OPTION},
     *               {@link JOptionPane#NO_OPTION} o {@link JOptionPane#CLOSED_OPTION}).
     * @return el objeto {@link Timer} ya iniciado que gestionará la respuesta automática.
     */
    private static Timer responder(int choice) {
        Timer timer = new Timer(100, e -> {
            for (Window w : Window.getWindows()) {
                if (w instanceof JDialog && w.isShowing()) {
                    if (choice == JOptionPane.CLOSED_OPTION) {
                        w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
                    } else {
                        answer(w, choice);
                    }
                }
            }
        });

        timer.start();

        return timer;
    }

    /**
     * Comprueba que {@link ResignGameWindow#confirmResignation(Component)} retorne {@code true}
     * únicamente cuando el usuario elige la opción afirmativa ({@link JOptionPane#YES_OPTION})
     * y {@code false} cuando selecciona "No" o cierra la ventana del diálogo.
     *
     * @throws Exception si ocurre una excepción al ejecutar la prueba en el EDT.
     */
    @Test void resignationDialogHandlesYesNoAndClose() throws Exception {
        onEdt(() -> {
            for (int choice : new int[]{JOptionPane.YES_OPTION, JOptionPane.NO_OPTION, JOptionPane.CLOSED_OPTION}) {
                Timer timer = responder(choice);
                try {
                    assertEquals(choice == JOptionPane.YES_OPTION, new ResignGameWindow().confirmResignation(null));
                } finally {
                    timer.stop();
                }
            }
        });
    }

    /**
     * Verifica que al presionar el botón de abandono ({@link ResignButton}) y cancelar
     * la confirmación en el diálogo, la partida continúe activa sin registrar un resultado final.
     *
     * @throws Exception si ocurre una excepción al ejecutar la prueba en el EDT.
     */
    @Test void cancellingResignationKeepsGameActive() throws Exception {
        onEdt(() -> {
            GameController controller = new GameController(new Board(null), null);
            ResignButton button = new ResignButton("Abandonar");
            button.configureAction(controller);

            Timer timer = responder(JOptionPane.NO_OPTION);

            try {
                button.doClick(0);
                assertEquals("", controller.getResult());
            } finally {
                timer.stop();
            }
        });
    }
}
