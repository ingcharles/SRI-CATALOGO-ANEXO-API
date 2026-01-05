package ec.gob.sri.api.catalogo.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PeriodoResponse")
@Getter
@Setter
public class PeriodoResponse {
    private long valor;

    private String etiqueta;

}