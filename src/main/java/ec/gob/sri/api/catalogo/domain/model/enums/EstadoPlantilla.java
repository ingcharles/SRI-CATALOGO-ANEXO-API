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
    I("Inactivo");

    private final String descripcion;

    EstadoPlantilla(String descripcion) {
        this.descripcion = descripcion;
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
        throw new IllegalArgumentException("Estado inválido: " + codigo + ". Los valores permitidos son: A, I");
    }
}
