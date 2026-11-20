package exceptions;

/**
 * Excepción en tiempo de ejecución que indica que un recurso, elemento o archivo
 * requerido por la aplicación no pudo ser localizado.
 * <p>
 * Esta clase extiende {@link RuntimeException}, por lo que representa una excepción
 * no verificada (<i>unchecked</i>) utilizada principalmente durante la carga de iconos
 * o recursos gráficos de las piezas y componentes del tablero.
 * </p>
 *
 * @author brysonfl
 * @see RuntimeException
 */
public class NotFoundException extends RuntimeException {

    /**
     * Construye una nueva instancia de {@code NotFoundException} con un mensaje
     * descriptivo del error ocurrido.
     *
     * @param message el mensaje detallado que explica qué recurso o elemento no fue encontrado.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
