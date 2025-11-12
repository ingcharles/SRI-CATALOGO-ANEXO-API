package ec.gob.sri.api.catalogo.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

/**
 * DTO para la respuesta de listar formularios
 */
@Schema(name = "ListarFormulariosResponse", description = "Respuesta paginada con lista de formularios")
public class ListarFormulariosResponse {

    @Schema(description = "Lista de formularios")
    @JsonProperty("formularios")
    public List<GuardarFormularioResponse> formularios;

    @Schema(description = "Total de registros", example = "100")
    @JsonProperty("total")
    public Long total;

    @Schema(description = "Total de páginas", example = "10")
    @JsonProperty("totalPaginas")
    public Integer totalPaginas;

    @Schema(description = "Número de página actual (inicia en 0)", example = "0")
    @JsonProperty("pagina")
    public Integer pagina;

    @Schema(description = "Tamaño de página (registros por página)", example = "10")
    @JsonProperty("tamanio")
    public Integer tamanio;

    @Schema(description = "Indica si es la primera página", example = "true")
    @JsonProperty("esPrimera")
    public Boolean esPrimera;

    @Schema(description = "Indica si es la última página", example = "false")
    @JsonProperty("esUltima")
    public Boolean esUltima;
}
