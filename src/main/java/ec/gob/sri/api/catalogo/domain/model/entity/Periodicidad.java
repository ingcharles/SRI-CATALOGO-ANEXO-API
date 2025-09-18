package ec.gob.sri.api.catalogo.domain.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class Periodicidad {
    private Long codigoPeriodicidad;

    private String abreviacion;

    private String descripcion;

    private String eliminado;

    private String estado;

    private BigDecimal tipoPeriodicidad;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Periodicidad that)) return false;
        return Objects.equals(codigoPeriodicidad, that.codigoPeriodicidad);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigoPeriodicidad);
    }
}
