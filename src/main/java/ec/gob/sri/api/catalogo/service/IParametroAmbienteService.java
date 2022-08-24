/**
 * Clase IParametroAmbienteService.java 24 de ago. de 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.service;

import java.util.Set;

import ec.gob.sri.api.catalogo.service.to.ParametroAmbienteTo;

/**
 * @author cfcg070314
 */
public interface IParametroAmbienteService {

	public Set<ParametroAmbienteTo> consultarParametrosPorNombreCodigoAplicacion(final String nombreParametro, final String codigoAplicacion);

}
