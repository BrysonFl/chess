/**
 * Implementa los controles para reclamar tablas, ofrecerlas y abandonar.
 *
 * <p>Los botones comparten {@link view.buttons.ButtonBase} y delegan las acciones
 * en {@link controller.GameController}. Configurar una acción añade oyentes;
 * debe hacerse una vez por botón. El abandono confirmado incluye una cuenta
 * regresiva y abre una nueva partida.</p>
 */
package view.buttons;
