/**
 * Clase IParametroAmbienteService.java 24 de ago. de 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.service;

import java.util.List;

import ec.gob.sri.api.catalogo.service.to.ParametroAmbienteTo;
import io.smallrye.mutiny.Uni;

/**
 * @author cfcg070314
 */
public interface IParametroAmbienteService {

	public Uni<List<ParametroAmbienteTo>> consultarParametrosPorNombreCodigoAplicacion(final String nombreParametro, final String codigoAplicacion);

}
