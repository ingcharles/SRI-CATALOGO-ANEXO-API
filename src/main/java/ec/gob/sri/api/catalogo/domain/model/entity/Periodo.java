package ec.gob.sri.api.catalogo.domain.model.entity;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class Periodo {

    private Long codigoPeriodo;

    private BigDecimal anioFiscal;

    private BigDecimal codigoPeriodicidad;

    private String descripcion;

    private String eliminado;

    private String estado;

    private LocalDate fechaFinal;

    private LocalDate fechaInicial;

    private BigDecimal numeroPeriodo;

    private String procesado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Periodo that)) return false;
        return Objects.equals(codigoPeriodicidad, that.codigoPeriodicidad);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigoPeriodicidad);
    }
}
