package ec.gob.sri.api.catalogo.infraestructure.rest.constant;

/**
 * Constantes de mensajes de error utilizados en los recursos REST
 */
public final class ErrorMessages {

    private ErrorMessages() {
        // Clase de utilidad, no debe ser instanciada
    }

    public static final String ERROR_INESPERADO = "Error inesperado procesando la solicitud";
    public static final String ERROR_CODE_404 = "ERR-404";
    public static final String ERROR_CODE_500 = "ERR-500";

    // Ejemplos JSON para documentación OpenAPI
    public static final String EXAMPLE_ERROR_500 = "{ \"codigo\": \"" + ERROR_CODE_500 + "\", \"mensaje\": \""
            + ERROR_INESPERADO + "\" }";
}
