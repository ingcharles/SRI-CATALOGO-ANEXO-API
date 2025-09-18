package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.domain.model.entity.Periodo;
import ec.gob.sri.api.catalogo.domain.repository.PeriodoRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PeriodoMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class PeriodoRepositoryImpl implements PeriodoRepository {

    @Inject
    PeriodoPanacheRepository periodoPanacheRepository;
    @Inject
    PeriodoMapper periodoMapper;


    @Override
    @WithSession
    public Uni<List<Periodo>> consultarPorCodigoPeriodicidad(BigDecimal codPeriodicidad) {

       /* List<PeriodoEntity> lista = periodoPanacheRepository
                .find("eliminado = 'N' and estado = 'A' and periodicidadEntity.codigoPeriodicidad = ?1 ORDER BY descripcion ASC", codPeriodicidad)
                .list()
                .await().indefinitely();*/

        return periodoPanacheRepository.find("eliminado = 'N' and  estado = 'A' and periodicidadEntity.codigoPeriodicidad = ?1 ORDER BY descripcion ASC", codPeriodicidad)
                .list()
                .map(periodoMapper::toDomainList);
    }
}