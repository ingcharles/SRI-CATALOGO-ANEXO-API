/**
 * Clase ParametroAmbienteServiceImpl.java 24 de ago. de 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import ec.gob.sri.api.catalogo.modelo.ParametroAmbiente;
import ec.gob.sri.api.catalogo.repository.ParametroAmbienteRepository;
import ec.gob.sri.api.catalogo.service.to.ParametroAmbienteTo;
import io.smallrye.mutiny.Uni;

/**
 * 
 */
@ApplicationScoped
public class ParametroAmbienteServiceImpl implements IParametroAmbienteService {

	@Inject
	ParametroAmbienteRepository parametroAmbienteRepository;

	private Function<ParametroAmbiente, ParametroAmbienteTo> mapParametroAmbienteTo = (parametroAmbiente -> new ParametroAmbienteTo(
			parametroAmbiente.getValor(), parametroAmbiente.getAmbiente()));
	/*
	 * (non-Javadoc)
	 * 
	 * @see ec.gob.sri.api.catalogo.service.IParametroAmbienteService#
	 * consultarParametrosPorNombreCodigoAplicacion(java.lang.String,
	 * java.lang.String)
	 */

	@Override
	public Uni<List<ParametroAmbienteTo>> consultarParametrosPorNombreCodigoAplicacion(String nombreParametro, String codigoAplicacion) {
		return parametroAmbienteRepository.consultarPorNombreYCodigoAplicacion(nombreParametro, codigoAplicacion).onItem()
				.transform(parametroAmbienteLista -> parametroAmbienteLista.stream().map(item -> mapParametroAmbienteTo.apply(item))
						.collect(Collectors.toList()));

	}

}
