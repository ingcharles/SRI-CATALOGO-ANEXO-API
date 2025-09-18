package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.math.BigDecimal;


@Schema(name = "PeriodicidadRequest")
public class PeriodicidadRequest {
    public long codigoPeriodicidad;

    public String abreviacion;
    public String descripcion;
    public String eliminado;
    public String estado;
    public BigDecimal tipoPeriodicidad;
}

