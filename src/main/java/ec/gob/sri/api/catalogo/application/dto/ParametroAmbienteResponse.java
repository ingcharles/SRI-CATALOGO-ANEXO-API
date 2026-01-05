package ec.gob.sri.api.catalogo.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ParametroAmbienteResponse")
@Getter
@Setter
public class ParametroAmbienteResponse {
    private Long codigoParametro;
    private String nombreParametro;
    private String codigoAplicacion;
    private String ambiente;
    private String valor;
    private String estado;
}