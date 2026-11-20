package view.buttons;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

/**
 * Clase base abstracta para los botones de acción de la interfaz gráfica de ajedrez.
 * <p>
 * Esta clase extiende {@link JButton} y define el aspecto visual y dimensiones
 * estándar ({@code 200x55} píxeles con cursor de mano {@link Cursor#HAND_CURSOR})
 * compartidos por los controles de acción de la partida. Asimismo, establece el
 * contrato mediante {@link #configureAction(GameController)} para vincular el
 * comportamiento de cada botón con el controlador del juego ({@link GameController}).
 * </p>
 *
 * @author brysonfl
 * @see ClaimDrawButton
 * @see DrawButton
 * @see ResignButton
 * @see GameController
 */
public abstract class ButtonBase extends JButton {

    /**
     * Construye e inicializa un botón base con el texto especificado.
     * <p>
     * Configura el tamaño preferido del botón en {@code 200x55} píxeles y
     * asigna el cursor de tipo mano ({@link Cursor#HAND_CURSOR}) al pasar sobre él.
     * </p>
     *
     * @param title el texto o etiqueta que se mostrará en el botón.
     */
    protected ButtonBase(String title) {
        super(title);

        setPreferredSize(new Dimension(200, 55));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Configura y vincula la acción del botón con el controlador de la partida.
     * <p>
     * Las subclases deben implementar este método para registrar los escuchadores
     * de eventos ({@link java.awt.event.ActionListener}) que invocarán la lógica
     * correspondiente en el {@link GameController}.
     * </p>
     *
     * @param controller el controlador de la partida sobre el cual se ejecutará
     *                   la acción asociada a este botón.
     */
    protected abstract void configureAction(GameController controller);

}
