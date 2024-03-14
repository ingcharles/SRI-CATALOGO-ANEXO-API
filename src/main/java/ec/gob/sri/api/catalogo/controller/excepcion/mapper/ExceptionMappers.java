/**
 * Clase ExceptionMappers.java 03 oct. 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.controller.excepcion.mapper;

import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import ec.gob.sri.api.catalogo.service.excepcion.NoEncontradoExcepcion;

/**
 * @author cfcg070314
 */
public class ExceptionMappers {

	@ServerExceptionMapper
	public RestResponse<String> mapException(NoEncontradoExcepcion ex) {
		return RestResponse.status(Response.Status.NOT_FOUND, "No encontrado: " + ex.mensaje);
	}

}
