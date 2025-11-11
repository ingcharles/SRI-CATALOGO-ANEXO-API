package ec.gob.sri.api.catalogo.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para la respuesta de formulario guardado/consultado
 */
@Schema(name = "GuardarFormularioResponse", description = "Respuesta con los datos del formulario")
public class GuardarFormularioResponse {

    @Schema(description = "Identificador único del formulario", example = "1")
    @JsonProperty("codigoPlantillaFormulario")
    public Long codigoPlantillaFormulario;

    @Schema(description = "Código del formulario", example = "FORM-ANEXO-001", required = true)
    @JsonProperty("codigo")
    public String codigo;

    @Schema(description = "Nombre del formulario", example = "Formulario de Anexo", required = true)
    @JsonProperty("nombre")
    public String nombre;

    @Schema(description = "Descripción del formulario", example = "Formulario para registro de anexos")
    @JsonProperty("descripcion")
    public String descripcion;

    @Schema(description = "Versión del formulario", example = "1.0.0", required = true)
    @JsonProperty("version")
    public String version;

    @Schema(description = "Estructura JSON dinámica de las páginas del formulario", required = true)
    @JsonProperty("paginas")
    @JsonRawValue
    public String paginas;

    @Schema(description = "Fecha de creación", example = "2025-11-10T10:30:00")
    @JsonProperty("fechaCreacion")
    public String fechaCreacion;

    @Schema(description = "Fecha de última actualización", example = "2025-11-10T10:30:00")
    @JsonProperty("fechaActualizacion")
    public String fechaActualizacion;
}
