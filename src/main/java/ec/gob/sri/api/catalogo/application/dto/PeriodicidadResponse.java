package ec.gob.sri.api.catalogo.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PeriodicidadResponse")
@Getter
@Setter
public class PeriodicidadResponse {
    private Long valor;

    private String etiqueta;

}