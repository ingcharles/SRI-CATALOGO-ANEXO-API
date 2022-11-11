/**
 * Clase NoEncontradoExcepcion.java 03 oct. 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.service.excepcion;

/**
 * @author cfcg070314
 */
public class NoEncontradoExcepcion extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 8608627855331408710L;

	public final String mensaje;

	public NoEncontradoExcepcion(String mensaje) {
		this.mensaje = mensaje;
	}

}
