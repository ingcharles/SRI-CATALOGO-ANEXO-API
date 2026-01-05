package ec.gob.sri.api.catalogo.domain.model.entity;

import ec.gob.sri.api.catalogo.domain.model.enums.EstadoPlantilla;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio para Formulario
 */
@Getter
@Setter
public class Formulario {

    private Long codigoFormulario;
    private Long codigoPlantillaFormulario;
    private String codigoUsuario;
    private String identificacionUsuario;
    private String elementos;
    private String elementosJsonPlantilla;
    private String elementosXmlPlantilla;
    private String nombrePlantilla;
    private String descripcionPlantilla;
    private String versionPlantilla;
    private String eliminado;
    private EstadoPlantilla estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Formulario that))
            return false;
        return Objects.equals(codigoFormulario, that.codigoFormulario);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigoFormulario);
    }
}
