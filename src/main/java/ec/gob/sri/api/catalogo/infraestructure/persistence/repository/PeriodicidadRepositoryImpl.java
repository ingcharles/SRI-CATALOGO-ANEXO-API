package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import java.util.List;

import ec.gob.sri.api.catalogo.domain.model.entity.Periodicidad;
import ec.gob.sri.api.catalogo.domain.repository.PeriodicidadRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PeriodicidadMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PeriodicidadRepositoryImpl implements PeriodicidadRepository {

    private final PeriodicidadPanacheRepository periodicidadPanacheRepository;
    private final PeriodicidadMapper periodicidadMapper;

    public PeriodicidadRepositoryImpl(PeriodicidadPanacheRepository periodicidadPanacheRepository,
            PeriodicidadMapper periodicidadMapper) {
        this.periodicidadPanacheRepository = periodicidadPanacheRepository;
        this.periodicidadMapper = periodicidadMapper;
    }

    @Override
    @WithSession
    public Uni<List<Periodicidad>> consultarTodos() {
        return periodicidadPanacheRepository.find("eliminado = 'N' and  estado = 'A'")
                .list()
                .map(periodicidadMapper::toDomainList);
    }
}