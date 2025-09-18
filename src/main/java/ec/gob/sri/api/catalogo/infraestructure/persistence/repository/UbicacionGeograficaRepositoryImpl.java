package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.domain.model.entity.UbicacionGeografica;
import ec.gob.sri.api.catalogo.domain.repository.UbicacionGeograficaRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.UbicacionGeograficaMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UbicacionGeograficaRepositoryImpl implements UbicacionGeograficaRepository {

    @Inject
    UbicacionGeograficaPanacheRepository ubicacionGeograficaPanacheRepository;
    @Inject
    UbicacionGeograficaMapper ubicacionGeograficaMapper;


    @Override
    @WithSession
    public Uni<List<UbicacionGeografica>> consultarTodos() {
        return ubicacionGeograficaPanacheRepository.find("eliminado = 'N' and  estado = 'A'")
                .list()
                .map(ubicacionGeograficaMapper::toDomainList);
    }

    @Override
    @WithSession
    public Uni<List<UbicacionGeografica>> consultarPorCodigoNivelGeografico(String codNivelGeografico) {
        return ubicacionGeograficaPanacheRepository.find("eliminado = 'N' and  estado = 'A' and codigoNivelGeografico = ?1 ORDER BY descripcion ASC", codNivelGeografico)
                .list()
                .map(ubicacionGeograficaMapper::toDomainList);
    }
}