/**
 * Clase ParametroAmbienteController.java 24 ago. 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.controller;

import java.util.List;

import ec.gob.sri.api.catalogo.service.IParametroAmbienteService;
import ec.gob.sri.api.catalogo.service.to.ParametroAmbienteTo;
import io.quarkus.hibernate.reactive.panache.common.WithSessionOnDemand;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * @author cfcg070314
 */
@Path("/parametros")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@WithSessionOnDemand
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
