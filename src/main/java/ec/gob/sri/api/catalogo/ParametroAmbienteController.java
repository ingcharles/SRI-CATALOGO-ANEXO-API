/**
 * Clase ParametroAmbienteController.java 24 ago. 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ec.gob.sri.api.catalogo.service.IParametroAmbienteService;

/**
 * @author cfcg070314
 */
@Path("/parametros")
@ApplicationScoped
@Transactional
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParametroAmbienteController {

	@Inject
	IParametroAmbienteService parametroAmbienteService;

	@GET
	@Path("/{nombreParametro}")
	public Response consultar(@PathParam("nombreParametro") String nombreParametro, @QueryParam("codigoApp") @NotNull @NotBlank @NotEmpty String codigoAplicacion) {
		return Response.ok(parametroAmbienteService.consultarParametrosPorNombreCodigoAplicacion(nombreParametro, codigoAplicacion)).build();
	}
}
