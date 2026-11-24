/**
 * Coordina las reglas y el ciclo de vida de una partida de ajedrez.
 *
 * <p>{@link controller.GameController} recibe los clics de las casillas, filtra destinos
 * pseudolegales, ejecuta jugadas y determina el resultado. Conserva el turno,
 * el contador de medios movimientos y las repeticiones. Trabaja sobre el estado
 * mutable de {@link view.Board}; las llamadas deben ser secuenciales.</p>
 */
package controller;
