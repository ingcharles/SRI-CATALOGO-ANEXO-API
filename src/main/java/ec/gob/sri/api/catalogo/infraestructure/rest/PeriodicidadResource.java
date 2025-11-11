/**
 * Clase PeriodicidadController.java 27 ago. 2025
 * Copyright 2025 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.infraestructure.rest;


import ec.gob.sri.api.catalogo.application.dto.PeriodicidadResponse;
import ec.gob.sri.api.catalogo.application.service.PeriodicidadService;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PeriodicidadMapper;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;


/***
 * @author
 * 
 */

@Path("/periodicidad")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Periodicidad", description = "Consulta de periodicidad")
public class PeriodicidadResource {

    @Inject
    PeriodicidadService periodicidadService;

    @GET
    @Operation(summary = "Consultar los periodos",
               description = "Retorna todos las periodicidades.")
    @APIResponses({
    @APIResponse(
        responseCode = "200",
        description = "Listado obtenido correctamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = PeriodicidadResponse[].class),
            examples = {
                @ExampleObject(
                    name = "Ejemplo 200",
                    value = """
                    [
                      {
                        "codigoPeriodicidad": 1,
                        "abreviacion": "A",
                        "descripcion": "ANUAL",
                        "eliminado": "N",
                        "estado": "A",
                        "tipoPeriodicidad": 2
                      }
                    ]
                    """
                )
            }
        )
    ),
    @APIResponse(
        responseCode = "400",
        description = "Petición inválida (periodicidad con formato o valores incorrectos)",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            examples = {
                @ExampleObject(
                    name = "Ejemplo 400",
                    value = """
                    { "codigo": "ERR-400", "mensaje": "Petición inválida" }
                    """
                )
            }
        )
    ),
    @APIResponse(
        responseCode = "404",
        description = "No se encontraron periodicidades"
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
                    { "codigo": "ERR-500", "mensaje": "Error inesperado procesando la solicitud" }
                    """
                )
            }
        )
    )
    })
    public Uni<Response> consultarPeriodos() {

             return periodicidadService.consultaTodos()
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
