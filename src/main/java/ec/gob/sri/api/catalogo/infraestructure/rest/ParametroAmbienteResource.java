/**
 * Clase ParametroAmbienteController.java 27 ago. 2025
 * Copyright 2025 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.infraestructure.rest;


import ec.gob.sri.api.catalogo.application.dto.ParametroAmbienteResponse;
import ec.gob.sri.api.catalogo.application.service.GestionarParametroAmbiente;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.*;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response; 
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;



/***
 * @author
 * 
 */

@Path("/parametros")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Parámetro Ambiente", description = "Consulta de parámetros por ambiente/aplicación")
public class ParametroAmbienteResource {

    @Inject
    GestionarParametroAmbiente gestionarParametro;

    @Inject
    ParametroAmbienteMapper mapper;


    @GET
    @Operation(summary = "Consultar por ambiente y aplicación",
               description = "Retorna el parámetro por ambiente y código de aplicación.")
    @APIResponses({
    @APIResponse(
        responseCode = "200",
        description = "Listado obtenido correctamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ParametroAmbienteResponse[].class),
            examples = {
                @ExampleObject(
                    name = "Ejemplo 200",
                    value = """
                    [
                      {
                        "codigoParametro": 101,
                        "nombreParametro": "URL_SERVICIO",
                        "codigoAplicacion": "ADM",
                        "ambiente": "PRO",
                        "valor": "https://api.sri.gob.ec/servicio",
                        "estado": "A"
                      }
                    ]
                    """
                )
            }
        )
    ),
    @APIResponse(
        responseCode = "400",
        description = "Petición inválida (parámetros con formato o valores incorrectos)",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            examples = {
                @ExampleObject(
                    name = "Ejemplo 400",
                    value = """
                    { "code": "ERR-400", "details": "El valor de 'ambiente' debe ser uno de: DEV, QA, PRO" }
                    """
                )
            }
        )
    ),
    @APIResponse(
        responseCode = "404",
        description = "No se encontraron parámetros para los criterios solicitados"
    ),
    @APIResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            examples = {
                @ExampleObject(
                    name = "Ejemplo 500",
                    value = """
                    { "code": "ERR-500", "details": "Error inesperado procesando la solicitud" }
                    """
                )
            }
        )
    )
    })
    public Uni<Response> consultarPorAmbienteYCodigoAplicacion(
            @QueryParam("nombreParametro") @Parameter(required = true, example = "PRO") String ambiente,
            @QueryParam("codigoApp") @Parameter(required = true, example = "ADM") String  codigoAplicacion) {

             return gestionarParametro.consultarPorAmbienteYCodigoAplicacion(ambiente, codigoAplicacion)
        .onItem().transform(list -> {
            if (list == null || list.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(list).build(); 
        })
        .onFailure(IllegalArgumentException.class)
        .recoverWithItem(err -> Response.status(Response.Status.BAD_REQUEST)
            .header("description", err.getMessage())
            .build());
    }
}
