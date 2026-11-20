import view.WindowGame;

/**
 * Clase principal e inicializadora de la aplicación de ajedrez (Chess Royale).
 * <p>
 * Contiene el punto de entrada ({@link #main(String[])}) encargado de iniciar la ejecución
 * del programa, instanciando y desplegando la interfaz gráfica principal representada
 * por {@link WindowGame}.
 * </p>
 *
 * @author brysonfl
 * @see WindowGame
 */
public class GameMain {

    /**
     * Constructor por defecto de la clase principal {@code GameMain}.
     */
    public GameMain() {
    }

    /**
     * Punto de entrada principal de la aplicación de ajedrez.
     * <p>
     * Crea una nueva instancia de {@link WindowGame} con una cadena vacía como título,
     * lo que hace que la ventana utilice su título predeterminado ({@code "Chess Royale"})
     * y se muestre inmediatamente en pantalla.
     * </p>
     *
     * @param args argumentos pasados desde la línea de comandos al iniciar la aplicación
     *             (actualmente no utilizados).
     */
    public static void main(String[] args) {

        WindowGame windowGame = new WindowGame("");

    }
}
