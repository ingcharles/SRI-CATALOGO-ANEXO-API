package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO para la solicitud de cambio de estado de una plantilla de formulario
 */
@Schema(name = "CambiarEstadoPlantillaRequest", description = "Datos para cambiar el estado de una plantilla")
public class CambiarEstadoPlantillaRequest {

    @Schema(description = "Nuevo estado de la plantilla (EC=EN CONSTRUCCIÓN, ER=EN REVISIÓN, AP=APROBADO, PU=PUBLICADO)", required = true)
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(EC|ER|AP|RE|PU)$", message = "Estado inválido. Los valores permitidos son: EC, ER, AP, RE, PU")
    public String estado;

    @Schema(description = "Motivo del cambio de estado (requerido cuando se rechaza: ER -> EC)")
    public String motivo;
}
