package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * DTO para la respuesta de formulario guardado/consultado
 */
@Schema(name = "FormularioResponse", description = "Respuesta con los datos del formulario")
public class FormularioResponse {

    @Schema(description = "Identificador único del formulario")
    @JsonProperty("codigoFormulario")
    public Long codigoFormulario;

    @Schema(description = "Código de la plantilla de formulario", required = true)
    @JsonProperty("codigoPlantillaFormulario")
    public Long codigoPlantillaFormulario;

    @Schema(description = "Código del usuario")
    @JsonProperty("codigoUsuario")
    public String codigoUsuario;

    @Schema(description = "Identificación del usuario")
    @JsonProperty("identificacionUsuario")
    public String identificacionUsuario;

    @Schema(description = "Estado del formulario")
    @JsonProperty("estado")
    public String estado;

    @Schema(description = "Estructura JSON dinámica de los elementos del formulario", required = true)
    @JsonProperty("elementos")
    @JsonRawValue
    public String elementos;

    @Schema(description = "Estructura JSON de los elementos de la plantilla asociada")
    @JsonProperty("elementosJsonPlantilla")
    @JsonRawValue
    public String elementosJsonPlantilla;

    @Schema(description = "Nombre de la plantilla asociada")
    @JsonProperty("nombrePlantilla")
    public String nombrePlantilla;

    @Schema(description = "Descripción de la plantilla asociada")
    @JsonProperty("descripcionPlantilla")
    public String descripcionPlantilla;

    @Schema(description = "Versión de la plantilla asociada")
    @JsonProperty("versionPlantilla")
    public String versionPlantilla;

    @Schema(description = "Fecha de creación")
    @JsonProperty("fechaCreacion")
    public String fechaCreacion;

    @Schema(description = "Fecha de última actualización")
    @JsonProperty("fechaActualizacion")
    public String fechaActualizacion;
}
