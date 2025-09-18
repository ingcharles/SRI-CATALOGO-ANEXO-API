package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.domain.model.entity.Periodicidad;
import ec.gob.sri.api.catalogo.domain.repository.PeriodicidadRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PeriodicidadMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PeriodicidadRepositoryImpl implements PeriodicidadRepository {

    @Inject PeriodicidadPanacheRepository periodicidadPanacheRepository;
    @Inject PeriodicidadMapper periodicidadMapper;

  
    @Override
    @WithSession
    public Uni<List<Periodicidad>> consultarTodos() {
        return periodicidadPanacheRepository.find("eliminado = 'N' and  estado = 'A'")
                          .list()
                   .map(periodicidadMapper::toDomainList);
    }
}