package ec.gob.sri.api.catalogo.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "UbicacionGeograficaResponse")
@Getter
@Setter
public class UbicacionGeograficaResponse {
    private String valor;
    private String etiqueta;

}