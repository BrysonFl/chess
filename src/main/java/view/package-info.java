/**
 * Reúne los componentes Swing de la partida.
 *
 * <p>{@link view.WindowGame} integra el tablero y el historial.
 * {@link view.Board} mantiene una matriz mutable de 8 por 8 casillas,
 * con las blancas abajo; {@link view.Square} conserva la pieza y los indicadores
 * visuales. Las reglas se delegan en {@link controller.GameController}.
 * Las actualizaciones de interfaz deben realizarse en el hilo de eventos de Swing.</p>
 */
package view;
