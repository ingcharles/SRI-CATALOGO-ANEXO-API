/**
 * Clase ErrorResponse.java 03 dic. 2025
 * Copyright 2025 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para respuestas de error en las APIs REST
 *
 * @author SRI
 */
@Schema(name = "ErrorResponse", description = "Respuesta de error")
public class ErrorResponse {

    @Schema(description = "Código de error")
    public String codigo;

    @Schema(description = "Mensaje del error")
    public String mensaje;

    /**
     * Constructor sin parámetros
     */
    public ErrorResponse() {
    }

    /**
     * Constructor con parámetros
     *
     * @param codigo  Código del error
     * @param mensaje Mensaje descriptivo del error
     */
    public ErrorResponse(String codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }
}
