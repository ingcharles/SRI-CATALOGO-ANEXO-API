package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ParametroAmbienteResponse")
public class ParametroAmbienteResponse {
    public Long codigoParametro;
    public String nombreParametro;
    public String codigoAplicacion;
    public String ambiente;
    public String valor;
    public String estado;
}