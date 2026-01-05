/**
 * Clase PeriodoController.java 27 ago. 2025
 * Copyright 2025 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.infraestructure.rest;

import ec.gob.sri.api.catalogo.application.dto.PeriodoResponse;
import ec.gob.sri.api.catalogo.application.service.PeriodoService;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestPath;

import java.math.BigDecimal;

/***
 * @author
 *
 */

@Path("/periodo")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Parámetro codigoPeriodicidad", description = "Consulta los periodos")
public class PeriodoResource {

    private final PeriodoService periodoService;

    public PeriodoResource(PeriodoService periodoService) {
        this.periodoService = periodoService;
    }

    @GET
    @Path("/{codPeriodicidad}")
    @Operation(summary = "Consultar por código nivel geográfico", description = "Obtiene un parámetro por su código nivel geografico.")
    @APIResponse(responseCode = "200", description = "Encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PeriodoResponse.class)))
    @APIResponse(responseCode = "404", description = "No encontrado")
    @APIResponse(responseCode = "400", description = "Petición inválida")
    public Uni<Response> consultarPorCodigoPeriodicidad(@RestPath BigDecimal codPeriodicidad) {


        return periodoService.consultarPorCodigoPeriodicidad(codPeriodicidad)
                .onItem().transform(list -> {
                    if (list == null || list.isEmpty()) {
                        return Response.status(Response.Status.NOT_FOUND).build();
                    }
                    return Response.ok(list).build();
                })
                .onFailure(IllegalArgumentException.class)
                .recoverWithItem(err ->
                        Response.status(Response.Status.BAD_REQUEST)
                                .header("description", err.getMessage())
                                .build()
                );
    }

}
