package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para guardar un nuevo formulario
 */
@Schema(name = "CrearFormularioRequest", description = "Datos para crear un nuevo formulario")
public class CrearFormularioRequest {

    @NotNull(message = "El código de plantilla formulario es obligatorio")
    @Schema(description = "Código de la plantilla de formulario", required = true)
    @JsonProperty("codigoPlantillaFormulario")
    public Long codigoPlantillaFormulario;

    @Schema(description = "Código del usuario")
    @JsonProperty("codigoUsuario")
    public String codigoUsuario;

    @Schema(description = "Identificación del usuario")
    @JsonProperty("identificacionUsuario")
    public String identificacionUsuario;

    @NotNull(message = "Los elementos son obligatorios")
    @Schema(description = "Estructura JSON dinámica de los elementos del formulario (cualquier estructura válida)", required = true)
    @JsonProperty("elementos")
    public Object elementos;

    @Schema(description = "Estado del formulario (A = Activo, I = Inactivo)", defaultValue = "A")
    @JsonProperty("estado")
    public String estado;
}
