package ec.gob.sri.api.catalogo.application.dto;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para la respuesta de listar formularios
 */
@Schema(name = "ListarFormulariosResponse", description = "Respuesta paginada con lista de formularios")
public class ListarPlantillaFormulariosResponse {

    @Schema(description = "Lista de formularios")
    @JsonProperty("formularios")
    public List<GuardarPlantillaFormularioResponse> formularios;

    @Schema(description = "Total de registros")
    @JsonProperty("total")
    public Long total;

    @Schema(description = "Total de páginas")
    @JsonProperty("totalPaginas")
    public Integer totalPaginas;

    @Schema(description = "Número de página actual (inicia en 0)")
    @JsonProperty("pagina")
    public Integer pagina;

    @Schema(description = "Tamaño de página (registros por página)")
    @JsonProperty("tamanio")
    public Integer tamanio;

    @Schema(description = "Indica si es la primera página")
    @JsonProperty("esPrimera")
    public Boolean esPrimera;

    @Schema(description = "Indica si es la última página")
    @JsonProperty("esUltima")
    public Boolean esUltima;
}
