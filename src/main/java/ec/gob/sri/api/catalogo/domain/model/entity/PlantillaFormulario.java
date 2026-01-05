package ec.gob.sri.api.catalogo.domain.model.entity;

import ec.gob.sri.api.catalogo.domain.model.enums.EstadoPlantilla;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio para Plantilla Formulario
 */
@Getter
@Setter
public class PlantillaFormulario {

    private Long codigoPlantillaFormulario;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String version;
    private String elementosJson;
    private String elementosXml;
    private String eliminado;
    private EstadoPlantilla estado;
    private String motivo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private LocalDateTime fechaRevision;
    private LocalDateTime fechaAprobacion;
    private LocalDateTime fechaPublicacion;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PlantillaFormulario that))
            return false;
        return Objects.equals(codigoPlantillaFormulario, that.codigoPlantillaFormulario);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigoPlantillaFormulario);
    }
}
