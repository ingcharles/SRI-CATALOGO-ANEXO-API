/**
 * Clase ParametroAmbienteController.java 24 ago. 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.controller;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ec.gob.sri.api.catalogo.service.IParametroAmbienteService;
import ec.gob.sri.api.catalogo.service.to.ParametroAmbienteTo;
import io.smallrye.mutiny.Uni;

/**
 * @author cfcg070314
 */
@Path("/parametros")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParametroAmbienteController {

	@Inject
	IParametroAmbienteService parametroAmbienteService;

	@GET
	@Path("/{nombreParametro}")
	public Uni<List<ParametroAmbienteTo>> consultar(@PathParam("nombreParametro") String nombreParametro,
			@QueryParam("codigoApp") String codigoAplicacion) {
		return parametroAmbienteService.consultarParametrosPorNombreCodigoAplicacion(nombreParametro, codigoAplicacion);
	}
}
