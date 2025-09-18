package ec.gob.sri.api.catalogo.application.service;

import ec.gob.sri.api.catalogo.application.dto.PeriodoResponse;
import ec.gob.sri.api.catalogo.domain.repository.PeriodoRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PeriodoMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class PeriodoService {

    @Inject
    PeriodoRepository periodoRepository;

    @Inject
    PeriodoMapper periodoMapper;

    public Uni<List<PeriodoResponse>> consultarPorCodigoPeriodicidad(BigDecimal codPeriodicidad) {
        return periodoRepository.consultarPorCodigoPeriodicidad(codPeriodicidad)
                .map(periodoMapper::toResponseList);
    }

}

