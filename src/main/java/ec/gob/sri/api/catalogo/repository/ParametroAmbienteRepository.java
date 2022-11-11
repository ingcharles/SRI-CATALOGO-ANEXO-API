/**
 * Clase ParametroAmbienteRepository.java 24 de ago. de 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import ec.gob.sri.api.catalogo.modelo.ParametroAmbiente;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

/**
 * @author cfcg070314
 */
@ApplicationScoped
public class ParametroAmbienteRepository implements PanacheRepository<ParametroAmbiente> {

	public Uni<List<ParametroAmbiente>> consultarPorNombreYCodigoAplicacion(final String nombreParametro, final String codigoAplicacion) {
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("estado", "A");
		parametros.put("eliminado", "N");
		parametros.put("nombreParametro", nombreParametro);
		parametros.put("codigoAplicacion", codigoAplicacion);

		return list(
				"genericoEntidad.estado = :estado AND genericoEntidad.eliminado = :eliminado AND nombreParametro = :nombreParametro AND codigoAplicacion = :codigoAplicacion",
				parametros);
	}

}
