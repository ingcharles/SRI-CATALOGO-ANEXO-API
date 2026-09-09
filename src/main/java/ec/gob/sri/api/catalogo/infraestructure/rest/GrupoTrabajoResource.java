package ec.gob.sri.api.catalogo.infraestructure.rest;

import ec.gob.sri.api.catalogo.application.dto.ErrorResponse;
import ec.gob.sri.api.catalogo.application.dto.client.GrupoPorIntegranteDTO;
import ec.gob.sri.api.catalogo.application.service.GestionarGrupoTrabajoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Recurso REST para operaciones relacionadas con grupos de trabajo
 */
@Path("/grupoTrabajo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Grupos de Trabajo", description = "Operaciones relacionadas con grupos de trabajo")
public class GrupoTrabajoResource {

    private static final Logger logger = LoggerFactory.getLogger(GrupoTrabajoResource.class);

    @Inject
    GestionarGrupoTrabajoService gestionarGrupoTrabajoService;

    @GET
    @Path("/codigoUsuario/{codigoUsuario}")
    @Operation(summary = "Obtener grupos por código de usuario", description = "Consulta los grupos asociados a un código de usuario específico")
    @APIResponse(responseCode = "200", description = "Grupos encontrados exitosamente", content = @Content(schema = @Schema(implementation = GrupoPorIntegranteDTO[].class)))
    @APIResponse(responseCode = "204", description = "No se encontraron grupos")
    @APIResponse(responseCode = "400", description = "Código de usuario inválido")
    @APIResponse(responseCode = "500", description = "Error interno del servidor")
    public Response obtenerGruposPorUsuario(
        @Parameter(description = "Código del usuario", required = true) @RestPath("codigoUsuario") String codigoUsuario) {

        logger.info("REST - Consultando grupos para el usuario: {}", codigoUsuario);

        try {
            List<GrupoPorIntegranteDTO> grupos = gestionarGrupoTrabajoService.obtenerGruposPorUsuario(
                codigoUsuario);

            if (grupos != null && !grupos.isEmpty()) {
                return Response.ok(grupos).build();
            } else {
                return Response.noContent().build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("400", e.getMessage()))
                .build();
        } catch (Exception e) {
            logger.error("Error al consultar grupos para el usuario: {}", codigoUsuario, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("500", "Error al procesar la solicitud"))
                .build();
        }
    }
}
