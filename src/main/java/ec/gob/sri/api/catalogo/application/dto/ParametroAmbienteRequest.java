package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;


@Schema(name = "ParametroAmbienteRequest")
public class ParametroAmbienteRequest {
    public String nombreParametro;
    public String codigoAplicacion;
    public String ambiente;
    public String valor;
}

