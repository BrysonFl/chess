package view;

import controller.GameController;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * Representa la ventana principal de la aplicación de ajedrez.
 * <p>
 * Esta clase extiende {@link JFrame} y actúa como el contenedor raíz de la interfaz
 * gráfica de usuario (GUI). Se encarga de calcular el área útil de la pantalla
 * (descontando barras de tareas o paneles del sistema), configurar el administrador
 * de diseño ({@link GridBagLayout}), establecer las propiedades de la ventana e
 * integrar los componentes visuales principales: el tablero de juego ({@link Board})
 * y el panel de historial de movimientos ({@link MovementsPanel}).
 * </p>
 *
 * <p>Requiere un entorno con pantalla. Su constructor muestra la ventana y el cierre
 * termina la aplicación mediante {@link JFrame#EXIT_ON_CLOSE}. El título debe ser
 * distinto de {@code null}.</p>
 *
 * @author brysonfl
 * @see Board
 * @see MovementsPanel
 */
public class WindowGame extends JFrame {

    /**
     * Construye, configura y muestra la ventana principal del juego de ajedrez.
     * <p>
     * Inicializa las dimensiones de la ventana ajustándolas al área máxima disponible
     * del monitor principal, define un fondo blanco, bloquea el redimensionamiento y
     * posiciona el tablero junto al panel de registro de jugadas.
     * </p>
     *
     * @param message el título personalizado que se mostrará en la barra de la ventana.
     *                Si la cadena está vacía ({@code message.isEmpty()}), se asignará
     *                el título por defecto {@code "Chess Royale"}.
     */
    public WindowGame(String message) {
        Rectangle screenBounds = new Rectangle(getGraphicsConfiguration().getBounds());
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        screenBounds.x += screenInsets.left;
        screenBounds.y += screenInsets.top;
        screenBounds.width -= screenInsets.left + screenInsets.right;
        screenBounds.height -= screenInsets.top + screenInsets.bottom;
        setBounds(screenBounds);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle(message.isEmpty() ? "Chess Royale" : message);

        MovementsPanel movementsPanel = new MovementsPanel();
        Board board = new Board(movementsPanel);

        add(board);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 24, 0, 0);

        add(movementsPanel, constraints);
        setVisible(true);
    }

}
