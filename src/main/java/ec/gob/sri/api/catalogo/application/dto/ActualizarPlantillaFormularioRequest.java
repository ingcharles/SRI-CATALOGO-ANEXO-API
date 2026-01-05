package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para actualizar un formulario/plantilla
 */
@Schema(name = "ActualizarFormulario", description = "Datos para actualizar un formulario existente")
public class ActualizarPlantillaFormularioRequest {

    @Schema(description = "Código del formulario")
    @JsonProperty("codigo")
    public String codigo;

    @Schema(description = "Nombre del formulario")
    @JsonProperty("nombre")
    public String nombre;

    @Schema(description = "Descripción del formulario")
    @JsonProperty("descripcion")
    public String descripcion;

    @Schema(description = "Versión del formulario")
    @JsonProperty("version")
    public String version;

    @Schema(description = "Estado del formulario (A=Activo, I=Inactivo)")
    @JsonProperty("estado")
    public String estado;

    @Schema(description = "Estructura JSON dinámica de las páginas del formulario")
    @JsonProperty("elementosJson")
    public Object elementosJson;

    @Schema(description = "Estructura XML dinámica de las páginas del formulario")
    @JsonProperty("elementosXml")
    public String elementosXml;
}
