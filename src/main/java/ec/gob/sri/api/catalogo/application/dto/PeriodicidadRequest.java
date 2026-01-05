package ec.gob.sri.api.catalogo.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "PeriodicidadRequest")
@Getter
@Setter
public class PeriodicidadRequest {
    private long codigoPeriodicidad;
    private String abreviacion;
    private String descripcion;
    private String eliminado;
    private String estado;
    private BigDecimal tipoPeriodicidad;
}
