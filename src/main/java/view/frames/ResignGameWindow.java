package view.frames;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Gestiona los cuadros de diálogo modales asociados al abandono de una partida
 * y a la cuenta regresiva previa al inicio de una nueva partida.
 * <p>
 * Esta clase proporciona métodos auxiliares basados en {@link JOptionPane} y
 * {@link JDialog} para solicitar confirmación al jugador antes de rendirse y
 * para mostrar un temporizador visual segundo a segundo antes de reiniciar el juego.
 * </p>
 *
 * <p>Los diálogos requieren un entorno gráfico y deben utilizarse desde el hilo de
 * eventos de Swing. La cuenta regresiva utiliza un temporizador Swing; la acción
 * final se ejecuta al regresar del diálogo modal, después de liberar sus recursos.</p>
 *
 * @author brysonfl
 * @see view.buttons.ResignButton
 * @see controller.GameController
 */
public class ResignGameWindow {

    /**
     * Construye una nueva instancia del gestor de diálogos de abandono de partida.
     */
    public ResignGameWindow() {
    }

    /**
     * Muestra un cuadro de diálogo modal de confirmación para verificar si el jugador
     * desea abandonar la partida actual.
     * <p>
     * El diálogo presenta las opciones estándar de sí o no ({@link JOptionPane#YES_NO_OPTION})
     * centradas en la pantalla y libera sus recursos gráficos al cerrarse.
     * </p>
     *
     * @param parentComponent el componente padre ({@link Component}) sobre el cual se
     *                        asocia el diálogo, o {@code null} si no se especifica ninguno.
     * @return {@code true} si el usuario confirma el abandono seleccionando la opción afirmativa
     *         ({@link JOptionPane#YES_OPTION}); {@code false} si cancela o cierra el diálogo.
     */
    public boolean confirmResignation(Component parentComponent) {
        JOptionPane optionPane = new JOptionPane("¿Quieres abandonar la partida?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        JDialog dialog = optionPane.createDialog(parentComponent, "Abandonar");
        dialog.setLocationRelativeTo(null);

        try {
            dialog.setVisible(true);
            return Integer.valueOf(JOptionPane.YES_OPTION).equals(optionPane.getValue());
        } finally {
            dialog.dispose();
        }
    }

    /**
     * Muestra un cuadro de diálogo modal con una cuenta regresiva en segundos y ejecuta
     * una acción de retorno al finalizar.
     * <p>
     * Durante la cuenta regresiva se actualiza dinámicamente el mensaje mediante un
     * {@link Timer} que se dispara cada segundo ({@code 1000} ms) y se bloquea el cierre
     * manual de la ventana ({@link WindowConstants#DO_NOTHING_ON_CLOSE}). Al llegar el
     * contador a cero, el diálogo se cierra automáticamente y se invoca la tarea
     * {@code onFinish} si fue proporcionada.
     * </p>
     *
     * @param parentComponent el componente padre ({@link Component}) de referencia para
     *                        crear el diálogo modal.
     * @param seconds         la cantidad inicial de segundos que durará la cuenta regresiva.
     * @param onFinish        la acción ({@link Runnable}) que se ejecutará inmediatamente
     *                        después de que termine la cuenta regresiva, o {@code null} si
     *                        no se requiere ejecutar ninguna acción adicional.
     */
    public void showCountdown(Component parentComponent, int seconds, Runnable onFinish) {
        AtomicInteger remainingSeconds = new AtomicInteger(seconds);
        JLabel messageLabel = new JLabel(formatCountdownMessage(remainingSeconds.get()), SwingConstants.CENTER);
        JOptionPane optionPane = new JOptionPane(messageLabel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

        JDialog dialog = optionPane.createDialog(parentComponent, "Nueva partida");
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setLocationRelativeTo(null);

        Timer timer = new Timer(1000, event -> {
            int remaining = remainingSeconds.decrementAndGet();

            if (remaining > 0) {
                messageLabel.setText(formatCountdownMessage(remaining));
            } else {
                ((Timer) event.getSource()).stop();
                dialog.dispose();
            }
        });

        try {
            timer.start();
            dialog.setVisible(true);
        } finally {
            timer.stop();
            dialog.dispose();
        }

        if (onFinish != null) {
            onFinish.run();
        }
    }

    /**
     * Formatea el mensaje informativo que indica los segundos restantes para iniciar
     * una nueva partida, ajustando la concordancia singular o plural.
     *
     * @param seconds el número de segundos restantes en el temporizador.
     * @return una cadena de texto formateada con el tiempo restante.
     */
    private String formatCountdownMessage(int seconds) {
        return "Se iniciará una nueva partida en " + seconds + (seconds == 1 ? " segundo..." : " segundos...");
    }
}
