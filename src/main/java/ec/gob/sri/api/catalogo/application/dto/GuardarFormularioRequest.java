package ec.gob.sri.api.catalogo.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para guardar un formulario/plantilla
 */
@Schema(name = "GuardarFormulario", description = "Datos para guardar un nuevo formulario")
public class GuardarFormularioRequest {

    @NotBlank(message = "El código es obligatorio")
    @Schema(description = "Código del formulario", example = "FORM-ANEXO-001", required = true)
    @JsonProperty("codigo")
    public String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del formulario", example = "Formulario de Anexo", required = true)
    @JsonProperty("nombre")
    public String nombre;

    @Schema(description = "Descripción del formulario", example = "Formulario para registro de anexos")
    @JsonProperty("descripcion")
    public String descripcion;

    @NotBlank(message = "La versión es obligatoria")
    @Schema(description = "Versión del formulario", example = "1.0.0", required = true)
    @JsonProperty("version")
    public String version;

    @NotNull(message = "Las páginas son obligatorias")
    @Schema(description = "Estructura JSON dinámica de las elementos del formulario (cualquier estructura válida)", required = true)
    @JsonProperty("elementos")
    public Object elementos;

    @Schema(description = "Estado del formulario (A = Activo, I = Inactivo)", example = "A", defaultValue = "A")
    @JsonProperty("estado")
    public String estado;
}
