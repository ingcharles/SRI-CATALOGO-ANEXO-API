package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para parámetros de consulta al listar formularios
 */
@Schema(name = "ConsultarFormulariosRequest", description = "Parámetros para listar formularios")
public class ConsultarFormulariosRequest {

    @Schema(description = "Número de página", defaultValue = "1")
    @JsonProperty("pagina")
    public Integer pagina = 1;

    @Schema(description = "Límite de registros por página", defaultValue = "10")
    @JsonProperty("limite")
    public Integer limite = 10;

    @Schema(description = "Código de plantilla formulario para filtrar")
    @JsonProperty("codigoPlantillaFormulario")
    public Long codigoPlantillaFormulario;

    @Schema(description = "Código de usuario para filtrar")
    @JsonProperty("codigoUsuario")
    public String codigoUsuario;

    @Schema(description = "Identificación de usuario para filtrar")
    @JsonProperty("identificacionUsuario")
    public String identificacionUsuario;

    @Schema(description = "Texto de búsqueda (busca en codigoUsuario, identificacionUsuario)")
    @JsonProperty("buscar")
    public String buscar;

    @Schema(description = "Campo por el cual ordenar (codigoFormulario, fechaCreacion, fechaActualizacion)")
    @JsonProperty("ordenarPor")
    public String ordenarPor = "fechaCreacion";

    @Schema(description = "Orden ascendente o descendente (asc, desc)")
    @JsonProperty("orden")
    public String orden = "desc";
}
