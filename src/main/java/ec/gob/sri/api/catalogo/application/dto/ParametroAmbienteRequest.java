package ec.gob.sri.api.catalogo.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ParametroAmbienteRequest")
@Getter
@Setter
public class ParametroAmbienteRequest {
    private String nombreParametro;
    private String codigoAplicacion;
    private String ambiente;
    private String valor;
}
