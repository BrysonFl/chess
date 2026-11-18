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
 * Clase principal que representa la ventana contenedora del juego de ajedrez. 
 * Configura las propiedades de la interfaz gráfica de usuario (GUI), tales como 
 * el tamaño del área útil de la pantalla, el diseño de distribución, el título y 
 * la adición del tablero principal del juego junto con el panel de movimientos.
 * 
 * @author brysonfl
 */
public class WindowGame extends JFrame {
    
     /**
     * Construye e inicializa la ventana principal del juego, configurando los parámetros 
     * de la interfaz gráfica como área útil de la pantalla, diseño de componentes, título ("Chess Royale"), 
     * el comportamiento de cierre y agregando el tablero y el panel de historial de movimientos.
     */
    public WindowGame() {
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
        setTitle("Chess Royale");

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
