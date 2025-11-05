package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PeriodoResponse")
public class PeriodoResponse {
    public long valor;


    public String etiqueta;


}