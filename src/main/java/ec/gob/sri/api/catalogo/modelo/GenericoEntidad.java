/**
 * Clase GenericoEntidad.java 24 ago. 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.modelo;

import java.io.Serializable;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 *
 * @author cfcg070314
 */
@Embeddable
@Access(AccessType.PROPERTY)
public class GenericoEntidad implements Serializable, Comparable<GenericoEntidad> {

	private static final long serialVersionUID = 600033366699901111L;

	@Column(name = "ELIMINADO")
	private String eliminado;

	@Column(name = "ESTADO")
	private String estado;

	public GenericoEntidad() {

	}

	public GenericoEntidad(String eliminado, String estado) {
		this.eliminado = eliminado;
		this.estado = estado;
	}

	public String getEliminado() {
		return eliminado;
	}

	public void setEliminado(String eliminado) {
		this.eliminado = eliminado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public int compareTo(GenericoEntidad t) {
		return this.estado.compareTo(t.getEstado());
	}

	@Override
	public String toString() {
		return "GenericoEntidad [eliminado=" + eliminado + ", estado=" + estado + "]";
	}
	
}
