package ec.gob.sri.api.catalogo.domain.model.enums;

/**
 * Enumeración para el estado de una plantilla de formulario
 */
public enum EstadoPlantilla {
    /**
     * Activo
     */
    A("Activo"),

    /**
     * Inactivo
     */
    I("Inactivo"),

    /**
     * EN CONSTRUCCIÓN
     */
    EC("EN CONSTRUCCIÓN"),

    /**
     * EN REVISIÓN
     */
    ER("EN REVISIÓN"),

    /**
     * APROBADO
     */
    AP("APROBADO"),

    /**
     * PUBLICADO
     */
    PU("PUBLICADO");

    private final String descripcion;

    EstadoPlantilla(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el enum desde un código
     */
    public static EstadoPlantilla fromCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (EstadoPlantilla estado : EstadoPlantilla.values()) {
            if (estado.name().equals(codigo)) {
                return estado;
            }
        }
        throw new IllegalArgumentException(
                "Estado inválido: " + codigo + ". Los valores permitidos son: A, I, EC, ER, AP, RE, PU");
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene el código del estado
     */
    public String getCodigo() {
        return this.name();
    }

    /**
     * Valida si la transición de estado es permitida
     * 
     * @param estadoActual Estado actual de la plantilla
     * @param nuevoEstado  Nuevo estado al que se quiere cambiar
     * @return true si la transición es válida, false en caso contrario
     */
    public static boolean esTransicionValida(EstadoPlantilla estadoActual, EstadoPlantilla nuevoEstado) {
        if (estadoActual == null || nuevoEstado == null) {
            return false;
        }

        return switch (estadoActual) {
            case EC -> nuevoEstado == ER; // EN CONSTRUCCIÓN -> EN REVISIÓN
            case ER -> nuevoEstado == AP || nuevoEstado == EC; // EN REVISIÓN -> APROBADO o EN CONSTRUCCIÓN (rechazado)
            case AP -> nuevoEstado == PU; // APROBADO -> PUBLICADO
            default -> false; // Otras transiciones no permitidas
        };
    }

    /**
     * Obtiene el mensaje de error para una transición inválida
     */
    public static String getMensajeTransicionInvalida(EstadoPlantilla estadoActual, EstadoPlantilla nuevoEstado) {
        if (estadoActual == null || nuevoEstado == null) {
            return "Estado actual o nuevo estado no puede ser nulo";
        }

        return switch (estadoActual) {
            case EC -> "Solo se puede cambiar de 'EN CONSTRUCCIÓN' a 'EN REVISIÓN'";
            case ER -> "Solo se puede cambiar de 'EN REVISIÓN' a 'APROBADO' o volver a 'EN CONSTRUCCIÓN' (rechazado)";
            case AP -> "Solo se puede cambiar de 'APROBADO' a 'PUBLICADO'";
            default -> "No se permiten cambios de estado desde '" + estadoActual.getDescripcion() + "'";
        };
    }
}
