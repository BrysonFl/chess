package view.buttons;

import controller.GameController;

/**
 * Representa el botón de la interfaz gráfica que permite reclamar tablas en la partida.
 * <p>
 * Esta clase extiende {@link ButtonBase} y vincula su evento de clic con la acción
 * {@link GameController#claimDraw()}, permitiendo al jugador activo reclamar un empate
 * cuando se cumplen las condiciones reglamentarias aplicables (como la regla de los
 * 50 movimientos o la triple repetición de posición).
 * </p>
 *
 * <p>La acción de partida se vincula mediante {@link #configureAction}. Cada
 * configuración añade oyentes; debe realizarse una vez por instancia y controlador.</p>
 *
 * @author brysonfl
 * @see ButtonBase
 * @see GameController#claimDraw()
 */
public class ClaimDrawButton extends ButtonBase {

    /**
     * Construye un nuevo botón para reclamar tablas con el título especificado.
     *
     * @param title el texto o etiqueta que se mostrará en el botón.
     */
    public ClaimDrawButton(String title) {
        super(title);
    }

    /**
     * Configura la acción del botón para reclamar tablas a través del controlador del juego.
     * <p>
     * Registra un {@link java.awt.event.ActionListener} que invoca
     * {@link GameController#claimDraw()} cuando el usuario presiona el botón.
     * </p>
     *
     * @param controller el controlador principal de la partida encargado de procesar
     *                   la reclamación de tablas.
     */
    @Override
    public void configureAction(GameController controller) {
        addActionListener(event -> controller.claimDraw());
    }
}
