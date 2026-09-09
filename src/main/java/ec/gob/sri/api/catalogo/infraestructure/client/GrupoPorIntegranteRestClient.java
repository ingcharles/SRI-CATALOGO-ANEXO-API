package ec.gob.sri.api.catalogo.infraestructure.client;

import ec.gob.sri.api.catalogo.application.dto.client.GrupoPorIntegranteDTO;
import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestPath;

/**
 * Cliente REST para consumir servicios de Grupo por Integrante.
 *
 * <p>
 * Este cliente propaga automáticamente el token de acceso (Bearer token) del
 * usuario autenticado al
 * servicio externo gracias a la anotación @AccessToken.
 * </p>
 */
@Path("/grupoPorIntegrante")
@RegisterRestClient(configKey = "grupo-por-integrante-rest")
@AccessToken // Propaga automáticamente el Bearer token al servicio externo
@Produces(MediaType.APPLICATION_JSON)
public interface GrupoPorIntegranteRestClient {

    /**
     * Lista grupos por código de usuario.
     *
     * @param codigoUsuario Código del usuario a consultar
     * @return Lista de DTOs con la información de los grupos por integrante
     */
    @GET
    @Path("/codigoUsuario/{codigoUsuario}")
    List<GrupoPorIntegranteDTO> listarGruposPorCodigoUsuario(
            @RestPath("codigoUsuario") String codigoUsuario);
}
