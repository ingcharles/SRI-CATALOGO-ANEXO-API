package ec.gob.sri.api.catalogo.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para parámetros de consulta al listar formularios
 */
@Schema(name = "ListarFormularios", description = "Parámetros para listar formularios")
public class ListarFormulariosRequest {

    @Schema(description = "Número de página", example = "1", defaultValue = "1")
    @JsonProperty("pagina")
    public Integer pagina = 1;

    @Schema(description = "Límite de registros por página", example = "10", defaultValue = "10")
    @JsonProperty("limite")
    public Integer limite = 10;

    @Schema(description = "Texto de búsqueda", example = "anexo")
    @JsonProperty("buscar")
    public String buscar;

    @Schema(description = "Campo por el cual ordenar (nombre, codigo, fechaCreacion, fechaActualizacion)", example = "nombre")
    @JsonProperty("ordenarPor")
    public String ordenarPor = "fechaCreacion";

    @Schema(description = "Orden ascendente o descendente (asc, desc)", example = "desc")
    @JsonProperty("orden")
    public String orden = "desc";
}
