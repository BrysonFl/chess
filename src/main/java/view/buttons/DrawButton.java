package view.buttons;

import controller.GameController;

/**
 * Representa el botón de la interfaz gráfica que permite ofrecer tablas por mutuo acuerdo.
 * <p>
 * Esta clase extiende {@link ButtonBase} y vincula su evento de activación con el
 * método {@link GameController#offerDraw()}, permitiendo a un jugador proponer un
 * empate a su oponente durante el transcurso de la partida.
 * </p>
 *
 * <p>La acción de partida se vincula mediante {@link #configureAction}. Cada
 * configuración añade oyentes; debe realizarse una vez por instancia y controlador.</p>
 *
 * @author brysonfl
 * @see ButtonBase
 * @see GameController#offerDraw()
 */
public class DrawButton extends ButtonBase {

    /**
     * Construye un nuevo botón para ofrecer tablas con el título especificado.
     *
     * @param title el texto o etiqueta que se mostrará en el botón.
     */
    public DrawButton(String title) {
        super(title);

        this.addActionListener(event -> {

        });
    }

    /**
     * Configura la acción del botón para ofrecer tablas mediante el controlador del juego.
     * <p>
     * Registra un {@link java.awt.event.ActionListener} que invoca
     * {@link GameController#offerDraw()} cuando el usuario presiona el botón.
     * </p>
     *
     * @param controller el controlador principal de la partida encargado de gestionar
     *                   la oferta de tablas.
     */
    @Override
    public void configureAction(GameController controller) {
        addActionListener(event -> controller.offerDraw());
    }
}
