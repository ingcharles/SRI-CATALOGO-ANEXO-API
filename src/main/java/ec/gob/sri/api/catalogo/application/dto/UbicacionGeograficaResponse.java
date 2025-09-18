package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UbicacionGeograficaResponse")
public class UbicacionGeograficaResponse {
    public String valor;
    public String etiqueta;

    //public BigDecimal codigoNivelGeografico;


    //public String eliminado;

    //public String estado;
}