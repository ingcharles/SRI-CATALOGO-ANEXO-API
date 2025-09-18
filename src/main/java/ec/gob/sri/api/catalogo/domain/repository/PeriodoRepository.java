package ec.gob.sri.api.catalogo.domain.repository;


import ec.gob.sri.api.catalogo.domain.model.entity.Periodo;
import io.smallrye.mutiny.Uni;

import java.math.BigDecimal;
import java.util.List;

public interface PeriodoRepository {

    Uni<List<Periodo>> consultarPorCodigoPeriodicidad(BigDecimal codPeriodicidad);
}
