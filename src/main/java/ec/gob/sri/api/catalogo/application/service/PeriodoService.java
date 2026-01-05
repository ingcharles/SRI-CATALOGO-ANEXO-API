package ec.gob.sri.api.catalogo.application.service;

import java.math.BigDecimal;
import java.util.List;

import ec.gob.sri.api.catalogo.application.dto.PeriodoResponse;
import ec.gob.sri.api.catalogo.domain.repository.PeriodoRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PeriodoMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PeriodoService {

    private final PeriodoRepository periodoRepository;
    private final PeriodoMapper periodoMapper;

    public PeriodoService(PeriodoRepository periodoRepository, PeriodoMapper periodoMapper) {
        this.periodoRepository = periodoRepository;
        this.periodoMapper = periodoMapper;
    }

    public Uni<List<PeriodoResponse>> consultarPorCodigoPeriodicidad(BigDecimal codPeriodicidad) {
        return periodoRepository.consultarPorCodigoPeriodicidad(codPeriodicidad)
                .map(periodoMapper::toResponseList);
    }
}
