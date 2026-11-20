package view.buttons;

import controller.GameController;
import view.WindowGame;
import view.frames.ResignGameWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Representa el botón de la interfaz gráfica que permite a un jugador rendirse en la partida.
 * <p>
 * Esta clase extiende {@link ButtonBase} y coordina el flujo completo de rendición:
 * solicita confirmación al usuario mediante {@link ResignGameWindow}, notifica la
 * rendición al controlador ({@link GameController#resign()}), muestra una cuenta
 * regresiva y finalmente cierra la ventana actual para iniciar una nueva partida
 * en {@link WindowGame}.
 * </p>
 *
 * @author brysonfl
 * @see ButtonBase
 * @see ResignGameWindow
 * @see WindowGame
 * @see GameController#resign()
 */
public class ResignButton extends ButtonBase {

    /**
     * Duración en segundos de la cuenta regresiva previa al inicio de una nueva partida
     * tras confirmarse la rendición.
     */
    private static final int NEW_GAME_COUNTDOWN_SECONDS = 5;

    /**
     * Construye un nuevo botón de rendición con el título especificado.
     *
     * @param title el texto o etiqueta que se mostrará en el botón.
     */
    public ResignButton(String title) {
        super(title);
    }

    /**
     * Configura la acción del botón para gestionar la rendición del jugador y el
     * reinicio de la partida.
     * <p>
     * Registra un {@link java.awt.event.ActionListener} que abre un diálogo de
     * confirmación a través de {@link ResignGameWindow#confirmResignation(Component)}.
     * Si el jugador confirma su decisión, se ejecuta {@link GameController#resign()}
     * y se inicia una cuenta regresiva de {@link #NEW_GAME_COUNTDOWN_SECONDS} segundos,
     * al término de la cual se libera la ventana actual y se crea una nueva instancia
     * de {@link WindowGame}.
     * </p>
     *
     * @param controller el controlador principal de la partida encargado de registrar
     *                   la rendición del jugador activo.
     */
    @Override
    public void configureAction(GameController controller) {
        addActionListener(event -> {
            ResignGameWindow resignGameWindow = new ResignGameWindow();

            if (resignGameWindow.confirmResignation(this)) {
                controller.resign();

                resignGameWindow.showCountdown(this, NEW_GAME_COUNTDOWN_SECONDS, () -> {
                    Window currentWindow = SwingUtilities.getWindowAncestor(this);
                    if (currentWindow != null) {
                        currentWindow.dispose();
                    }

                    new WindowGame("Chess Royale - Iniciaste un juego nuevo");
                });
            }
        });
    }
}
