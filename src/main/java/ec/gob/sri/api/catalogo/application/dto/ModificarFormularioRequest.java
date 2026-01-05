package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para actualizar un formulario existente
 */
@Schema(name = "ModificarFormularioRequest", description = "Datos para actualizar un formulario existente")
public class ModificarFormularioRequest {

    @Schema(description = "Código del usuario")
    @JsonProperty("codigoUsuario")
    public String codigoUsuario;

    @Schema(description = "Identificación del usuario")
    @JsonProperty("identificacionUsuario")
    public String identificacionUsuario;

    @Schema(description = "Estado del formulario (A=Activo, I=Inactivo)")
    @JsonProperty("estado")
    public String estado;

    @Schema(description = "Estructura JSON dinámica de los elementos del formulario")
    @JsonProperty("elementos")
    public Object elementos;
}
