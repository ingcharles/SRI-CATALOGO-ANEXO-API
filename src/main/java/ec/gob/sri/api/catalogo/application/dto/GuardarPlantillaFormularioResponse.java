package ec.gob.sri.api.catalogo.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Map;

/**
 * DTO para la respuesta de formulario guardado/consultado
 */
@Schema(name = "GuardarFormularioResponse", description = "Respuesta con los datos del formulario")
public class GuardarPlantillaFormularioResponse {

    @Schema(description = "Identificador único del formulario")
    @JsonProperty("codigoPlantillaFormulario")
    public Long codigoPlantillaFormulario;

    @Schema(description = "Código del formulario", required = true)
    @JsonProperty("codigo")
    public String codigo;

    @Schema(description = "Nombre del formulario", required = true)
    @JsonProperty("nombre")
    public String nombre;

    @Schema(description = "Descripción del formulario")
    @JsonProperty("descripcion")
    public String descripcion;

    @Schema(description = "Versión del formulario", required = true)
    @JsonProperty("version")
    public String version;

    @Schema(description = "Estado del formulario")
    @JsonProperty("estado")
    public String estado;

    @Schema(description = "Motivo del estado")
    @JsonProperty("motivo")
    public String motivo;

    @Schema(description = "Estructura JSON dinámica de las elementos del formulario (JSON)", required = true)
    @JsonProperty("elementosJson")
    public List<Map<String, Object>> elementosJson;

    @Schema(description = "Estructura XML dinámica de las elementos del formulario")
    @JsonProperty("elementosXml")
    public String elementosXml;

    @Schema(description = "Fecha de creación")
    @JsonProperty("fechaCreacion")
    public String fechaCreacion;

    @Schema(description = "Fecha de última actualización")
    @JsonProperty("fechaActualizacion")
    public String fechaActualizacion;

    @Schema(description = "Fecha de revisión")
    @JsonProperty("fechaRevision")
    public String fechaRevision;

    @Schema(description = "Fecha de aprobación")
    @JsonProperty("fechaAprobacion")
    public String fechaAprobacion;

    @Schema(description = "Fecha de publicación")
    @JsonProperty("fechaPublicacion")
    public String fechaPublicacion;
}
