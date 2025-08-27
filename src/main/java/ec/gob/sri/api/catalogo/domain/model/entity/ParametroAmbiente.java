package ec.gob.sri.api.catalogo.domain.model.entity;

import ec.gob.sri.api.catalogo.domain.model.enums.Ambiente;
import java.time.LocalDate;
import java.util.Objects;

public class ParametroAmbiente {
    private Long codigoParametro;
    private String nombreParametro;
    private String codigoAplicacion;
    private String ambiente;
    private String valor;
    private String estado; // ACT | INA
    private String eliminado;
    

    public Long getCodigoParametro() { return codigoParametro; }
    public void setCodigoParametro(Long codigoParametro) { this.codigoParametro = codigoParametro; }

    public String getNombreParametro() { return nombreParametro; }
    public void setNombreParametro(String nombreParametro) { this.nombreParametro = nombreParametro; }

    public String getCodigoAplicacion() { return codigoAplicacion; }
    public void setCodigoAplicacion(String codigoAplicacion) { this.codigoAplicacion = codigoAplicacion; }

    public String getAmbiente() { return ambiente; }
    public void setAmbiente(String ambiente) { this.ambiente = ambiente; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEliminado() { return eliminado; }
    public void setEliminado(String eliminado) { this.eliminado = eliminado; }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParametroAmbiente)) return false;
        ParametroAmbiente that = (ParametroAmbiente) o;
        return Objects.equals(codigoParametro, that.codigoParametro);
    }
    @Override
    public int hashCode() { return Objects.hashCode(codigoParametro); }
}
