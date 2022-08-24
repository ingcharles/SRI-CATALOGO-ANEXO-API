/**
 * Clase ParametroAmbienteServiceImpl.java 24 de ago. de 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.service;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ec.gob.sri.api.catalogo.modelo.ParametroAmbiente;
import ec.gob.sri.api.catalogo.repository.ParametroAmbienteRepository;
import ec.gob.sri.api.catalogo.service.to.ParametroAmbienteTo;

/**
 * 
 */
@ApplicationScoped
public class ParametroAmbienteServiceImpl implements IParametroAmbienteService {

	@Inject
	ParametroAmbienteRepository parametroAmbienteRepository;

	private Function<ParametroAmbiente, ParametroAmbienteTo> mapParametroAmbienteTo = (parametroAmbiente -> new ParametroAmbienteTo(parametroAmbiente.getValor()));
	/*
	 * (non-Javadoc)
	 * 
	 * @see ec.gob.sri.api.catalogo.service.IParametroAmbienteService#
	 * consultarParametrosPorNombreCodigoAplicacion(java.lang.String,
	 * java.lang.String)
	 */

	@Override
	public Set<ParametroAmbienteTo> consultarParametrosPorNombreCodigoAplicacion(String nombreParametro, String codigoAplicacion) {
		return parametroAmbienteRepository.consultarPorNombreYCodigoAplicacion(nombreParametro, codigoAplicacion)
				.map(parametroAmbiente -> mapParametroAmbienteTo.apply(parametroAmbiente)).collect(Collectors.toSet());
	}

}
