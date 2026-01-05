package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import java.math.BigDecimal;
import java.util.List;

import ec.gob.sri.api.catalogo.domain.model.entity.Periodo;
import ec.gob.sri.api.catalogo.domain.repository.PeriodoRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PeriodoMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PeriodoRepositoryImpl implements PeriodoRepository {

    private final PeriodoPanacheRepository periodoPanacheRepository;
    private final PeriodoMapper periodoMapper;

    public PeriodoRepositoryImpl(PeriodoPanacheRepository periodoPanacheRepository, PeriodoMapper periodoMapper) {
        this.periodoPanacheRepository = periodoPanacheRepository;
        this.periodoMapper = periodoMapper;
    }

    @Override
    @WithSession
    public Uni<List<Periodo>> consultarPorCodigoPeriodicidad(BigDecimal codPeriodicidad) {

        return periodoPanacheRepository.find(
                "eliminado = 'N' and  estado = 'A' and periodicidadEntity.codigoPeriodicidad = ?1 ORDER BY descripcion ASC",
                codPeriodicidad)
                .list()
                .map(periodoMapper::toDomainList);
    }
}