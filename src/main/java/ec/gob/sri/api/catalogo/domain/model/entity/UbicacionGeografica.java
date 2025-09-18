package ec.gob.sri.api.catalogo.domain.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class UbicacionGeografica {
    private String codigoUbicacionGeografica;

    private BigDecimal codigoNivelGeografico;

    private String descripcion;

    private String eliminado;

    private String estado;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UbicacionGeografica)) return false;
        UbicacionGeografica that = (UbicacionGeografica) o;
        return Objects.equals(codigoUbicacionGeografica, that.codigoUbicacionGeografica);
    }
    @Override
    public int hashCode() { return Objects.hashCode(codigoUbicacionGeografica); }
}
