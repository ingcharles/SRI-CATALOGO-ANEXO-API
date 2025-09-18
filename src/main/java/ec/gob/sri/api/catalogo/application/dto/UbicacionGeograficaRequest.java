package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;


@Schema(name = "UbicacionGeograficaRequest")
public class UbicacionGeograficaRequest {

    public BigDecimal codigoNivelGeografico;

}

