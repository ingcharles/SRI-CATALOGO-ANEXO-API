package ec.gob.sri.api.catalogo.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "UbicacionGeograficaRequest")
@Getter
@Setter
public class UbicacionGeograficaRequest {

    private BigDecimal codigoNivelGeografico;

}
