package view;

import java.awt.GridBagLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * Clase principal que representa la ventana contenedora del juego de ajedrez. 
 * Configura las propiedades de la interfaz gráfica de usuario (GUI), tales como 
 * el tamaño en pantalla completa, el diseño de distribución, el título y 
 * la adición del tablero principal del juego.
 * 
 * @author brysonfl
 */
public class WindowGame extends JFrame {
    
     /**
     * Construye e inicializa la ventana principal del juego, configurando los parámetros 
     * de la interfaz gráfica como pantalla completa, diseño de componentes, título ("Chess Royale"), 
     * el comportamiento de cierre y agregando el componente del tablero para iniciar la partida.
     */
    public WindowGame() {
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Chess Royale");
        
        add(new Board());
        setVisible(true);
    }
    
}
