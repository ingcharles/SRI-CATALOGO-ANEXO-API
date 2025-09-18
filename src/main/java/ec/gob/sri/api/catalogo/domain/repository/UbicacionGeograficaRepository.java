package ec.gob.sri.api.catalogo.domain.repository;


import ec.gob.sri.api.catalogo.domain.model.entity.UbicacionGeografica;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface UbicacionGeograficaRepository {

    Uni<List<UbicacionGeografica>> consultarTodos();

    Uni<List<UbicacionGeografica>> consultarPorCodigoNivelGeografico(String codNivelGeografico);
}
