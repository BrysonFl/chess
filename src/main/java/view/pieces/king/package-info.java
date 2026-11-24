/**
 * Modela los desplazamientos del rey y la planificación del enroque.
 *
 * <p>{@link view.pieces.king.King} separa los destinos adyacentes de la validación
 * del enroque, cuya seguridad depende de predicados del controlador.
 * {@link view.pieces.king.CastleMovement} describe el traslado de la torre
 * mediante referencias a casillas mutables; no ejecuta el movimiento.</p>
 */
package view.pieces.king;
