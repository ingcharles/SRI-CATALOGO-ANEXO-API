package ec.gob.sri.api.catalogo.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para actualizar un formulario/plantilla
 */
@Schema(name = "ActualizarFormulario", description = "Datos para actualizar un formulario existente")
public class ActualizarFormularioRequest {

   /* @NotNull(message = "El código de plantilla formulario es obligatorio")
    @Schema(description = "Identificador del formulario", example = "1", required = true)
    @JsonProperty("codigoPlantillaFormulario")
    public Long codigoPlantillaFormulario;*/

    @Schema(description = "Código del formulario", example = "FORM-ANEXO-001")
    @JsonProperty("codigo")
    public String codigo;

    @Schema(description = "Nombre del formulario", example = "Formulario de Anexo")
    @JsonProperty("nombre")
    public String nombre;

    @Schema(description = "Descripción del formulario", example = "Formulario para registro de anexos")
    @JsonProperty("descripcion")
    public String descripcion;

    @Schema(description = "Versión del formulario", example = "1.0.0")
    @JsonProperty("version")
    public String version;

    @Schema(description = "Estado del formulario (A=Activo, I=Inactivo)", example = "A")
    @JsonProperty("estado")
    public String estado;

    @Schema(description = "Estructura JSON dinámica de las páginas del formulario")
    @JsonProperty("elementos")
    public Object elementos;
}
