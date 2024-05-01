/**
 * Clase ParametroAmbiente.java 24 de ago. de 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.modelo;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author cfcg070314
 *
 */
@Entity
@Table(name = "ADM_PARAMETRO_AMBIENTE")
public class ParametroAmbiente {

	@Id
	@Column(name = "CODIGO_PARAMETRO_AMBIENTE")
	private Long codigoParametro;

	@Column(name = "NOMBRE_PARAMETRO")
	private String nombreParametro;

	@Column(name = "CODIGO_APLICACION")
	private String codigoAplicacion;

	@Column(name = "AMBIENTE")
	private String ambiente;

	@Column(name = "VALOR_TEXTO")
	private String valor;

	@Embedded
	private GenericoEntidad genericoEntidad;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ParametroAmbiente)) {
			return false;
		}
		ParametroAmbiente parametroAmbiente = (ParametroAmbiente) o;
		return Objects.equals(getCodigoParametro(), parametroAmbiente.getCodigoParametro());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigoParametro());
	}

	public Long getCodigoParametro() {
		return codigoParametro;
	}

	public void setCodigoParametro(Long codigoParametro) {
		this.codigoParametro = codigoParametro;
	}

	public String getNombreParametro() {
		return nombreParametro;
	}

	public void setNombreParametro(String nombreParametro) {
		this.nombreParametro = nombreParametro;
	}

	public String getCodigoAplicacion() {
		return codigoAplicacion;
	}

	public void setCodigoAplicacion(String codigoAplicacion) {
		this.codigoAplicacion = codigoAplicacion;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public GenericoEntidad getGenericoEntidad() {
		return genericoEntidad;
	}

	public void setGenericoEntidad(GenericoEntidad genericoEntidad) {
		this.genericoEntidad = genericoEntidad;
	}

	@Override
	public String toString() {
		return "ParametroAmbiente [codigoParametro=" + codigoParametro + ", nombreParametro=" + nombreParametro
				+ ", codigoAplicacion=" + codigoAplicacion + ", ambiente=" + ambiente + ", valor=" + valor
				+ ", genericoEntidad=" + genericoEntidad + "]";
	}

}
