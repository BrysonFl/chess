package view;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Clase principal que representa la ventana contenedora del juego de ajedrez. 
 * Configura las propiedades de la interfaz gráfica de usuario (GUI), tales como 
 * el tamaño del área útil de la pantalla, el diseño de distribución, el título y
 * la adición del tablero principal del juego.
 * 
 * @author brysonfl
 */
public class WindowGame extends JFrame {
    
     /**
     * Construye e inicializa la ventana principal del juego, configurando los parámetros 
     * de la interfaz gráfica para ocupar el área útil de la pantalla, diseño de componentes, título ("Chess Royale"),
     * el comportamiento de cierre y agregando el componente del tablero para iniciar la partida.
     */
    public WindowGame() {
        GraphicsConfiguration screen = getGraphicsConfiguration();
        Rectangle availableBounds = new Rectangle(screen.getBounds());
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(screen);
        availableBounds.x += screenInsets.left;
        availableBounds.y += screenInsets.top;
        availableBounds.width -= screenInsets.left + screenInsets.right;
        availableBounds.height -= screenInsets.top + screenInsets.bottom;
        setBounds(availableBounds);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Chess Royale");
        
        Board board = new Board();

        // Centra el tablero y limita su lado al espacio disponible y a 1000 px.
        JPanel boardContainer = new JPanel(null) {
            @Override
            public void doLayout() {
                int side = Math.max(0, Math.min(1000, Math.min(getWidth(), getHeight())));
                board.setBounds((getWidth() - side) / 2, (getHeight() - side) / 2,
                        side, side);
            }
        };
        boardContainer.setPreferredSize(new Dimension(1000, 1000));
        boardContainer.setMinimumSize(new Dimension(0, 0));
        boardContainer.add(board);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(16, 16, 16, 0);
        add(boardContainer, constraints);

        constraints.gridx = 1;
        constraints.weightx = 0;
        constraints.insets = new Insets(16, 0, 16, 16);
        add(board.getMovementsPanel(), constraints);
        setVisible(true);
    }
    
}
