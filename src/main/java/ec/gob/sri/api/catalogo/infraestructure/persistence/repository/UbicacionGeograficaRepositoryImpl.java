package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import java.util.List;

import ec.gob.sri.api.catalogo.domain.model.entity.UbicacionGeografica;
import ec.gob.sri.api.catalogo.domain.repository.UbicacionGeograficaRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.UbicacionGeograficaMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UbicacionGeograficaRepositoryImpl implements UbicacionGeograficaRepository {

    private final UbicacionGeograficaPanacheRepository ubicacionGeograficaPanacheRepository;
    private final UbicacionGeograficaMapper ubicacionGeograficaMapper;

    public UbicacionGeograficaRepositoryImpl(UbicacionGeograficaPanacheRepository ubicacionGeograficaPanacheRepository,
            UbicacionGeograficaMapper ubicacionGeograficaMapper) {
        this.ubicacionGeograficaPanacheRepository = ubicacionGeograficaPanacheRepository;
        this.ubicacionGeograficaMapper = ubicacionGeograficaMapper;
    }

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
        return ubicacionGeograficaPanacheRepository
                .find("eliminado = 'N' and  estado = 'A' and codigoNivelGeografico = ?1 ORDER BY descripcion ASC",
                        codNivelGeografico)
                .list()
                .map(ubicacionGeograficaMapper::toDomainList);
    }
}