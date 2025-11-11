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

    @Schema(description = "Número de página actual", example = "1")
    @JsonProperty("pagina")
    public Integer pagina;

    @Schema(description = "Límite de registros por página", example = "10")
    @JsonProperty("limite")
    public Integer limite;
}
