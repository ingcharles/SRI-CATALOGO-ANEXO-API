package ec.gob.sri.api.catalogo.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para la respuesta de listar formularios
 */
@Schema(name = "ConsultarFormulariosResponse", description = "Respuesta paginada con lista de formularios")
public class ConsultarFormulariosResponse {

  @Schema(description = "Lista de formularios")
  @JsonProperty("formularios")
  public List<FormularioResponse> formularios;

  @Schema(description = "Total de registros")
  @JsonProperty("total")
  public Long total;

  @Schema(description = "Total de páginas")
  @JsonProperty("totalPaginas")
  public Integer totalPaginas;

  @Schema(description = "Número de página actual (inicia en 1)")
  @JsonProperty("pagina")
  public Integer pagina;

  @Schema(description = "Tamaño de página (registros por página)")
  @JsonProperty("tamanio")
  public Integer tamanio;

}
