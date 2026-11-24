package exceptions;

/**
 * Excepción en tiempo de ejecución que indica que un recurso, elemento o archivo
 * requerido por la aplicación no pudo ser localizado.
 * <p>
 * Esta clase extiende {@link RuntimeException}, por lo que representa una excepción
 * no verificada (<i>unchecked</i>). La carga de iconos de las piezas contempla su
 * captura, aunque actualmente un recurso ausente se trata como una
 * {@link NullPointerException} dentro de esa rutina.
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
