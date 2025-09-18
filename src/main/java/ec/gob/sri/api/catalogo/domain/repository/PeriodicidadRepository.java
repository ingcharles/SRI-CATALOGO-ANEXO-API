package ec.gob.sri.api.catalogo.domain.repository;


import ec.gob.sri.api.catalogo.domain.model.entity.Periodicidad;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface PeriodicidadRepository {

    Uni<List<Periodicidad>> consultarTodos();
}
