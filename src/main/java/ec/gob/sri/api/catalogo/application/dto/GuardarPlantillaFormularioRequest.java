package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para guardar un formulario/plantilla
 */
@Schema(name = "GuardarFormulario", description = "Datos para guardar un nuevo formulario")
public class GuardarPlantillaFormularioRequest {

    @NotBlank(message = "El código es obligatorio")
    @Schema(description = "Código del formulario", required = true)
    @JsonProperty("codigo")
    public String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del formulario", required = true)
    @JsonProperty("nombre")
    public String nombre;

    @Schema(description = "Descripción del formulario")
    @JsonProperty("descripcion")
    public String descripcion;

    @NotBlank(message = "La versión es obligatoria")
    @Schema(description = "Versión del formulario", required = true)
    @JsonProperty("version")
    public String version;

    @NotNull(message = "Las páginas son obligatorias")
    @Schema(description = "Estructura JSON dinámica de las elementos del formulario (cualquier estructura válida)", required = true)
    @JsonProperty("elementosJson")
    public Object elementosJson;

    @Schema(description = "Estructura XML dinámica de las elementos del formulario")
    @JsonProperty("elementosXml")
    public String elementosXml;

    @Schema(description = "Estado del formulario (A = Activo, I = Inactivo)", defaultValue = "A")
    @JsonProperty("estado")
    public String estado;
}
