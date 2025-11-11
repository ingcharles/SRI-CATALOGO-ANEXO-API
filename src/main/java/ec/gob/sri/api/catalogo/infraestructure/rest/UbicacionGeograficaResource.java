/**
 * Clase UbicacionGeograficaController.java 27 ago. 2025
 * Copyright 2025 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.infraestructure.rest;


import ec.gob.sri.api.catalogo.application.dto.UbicacionGeograficaResponse;
import ec.gob.sri.api.catalogo.application.service.UbicacionGeograficaService;
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
import org.jboss.resteasy.reactive.RestPath;


/***
 * @author
 *
 */

@Path("/ubicacion-geografica")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Parámetro Ambiente", description = "Consulta de ubicaciones geográficas")
public class UbicacionGeograficaResource {

    @Inject
    UbicacionGeograficaService ubicacionGeograficaService;

    @GET
    @Operation(summary = "Consultar de ubicaciones geográficas",
            description = "Retorna la listade ubicaciones geográficas.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Listado obtenido correctamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UbicacionGeograficaResponse[].class),
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo 200",
                                            value = """
                                                    [
                                                      {
                                                        "codigoUbicacionGeografica": 20305,
                                                        "codigoNivelGeografico": 3,
                                                        "descripcion": "EL TAMBO",
                                                        "eliminado": "N",
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
                                                    { "codigo": "ERR-400", "mensaje": "Petición inválida" }
                                                    """
                                    )
                            }
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "No se encontraron ubicaciones geográficas para los criterios solicitados"
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
    public Uni<Response> consultarTodos() {

        return ubicacionGeograficaService.consultarTodos()
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


    @GET
    @Path("/{codNivelGeografico}")
    @Operation(summary = "Consultar por código nivel geográfico", description = "Obtiene un parámetro por su código nivel geografico.")
    @APIResponse(responseCode = "200", description = "Encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UbicacionGeograficaResponse.class)))
    @APIResponse(responseCode = "404", description = "No encontrado")
    @APIResponse(responseCode = "400", description = "Petición inválida")
    public Uni<Response> consultarPorCodigoNivelGeografico(@RestPath String codNivelGeografico) {

        return ubicacionGeograficaService.consultarPorCodigoNivelGeografico(codNivelGeografico)
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
