package ec.gob.sri.api.catalogo.application.service;

import java.util.List;

import ec.gob.sri.api.catalogo.application.dto.PeriodicidadResponse;
import ec.gob.sri.api.catalogo.domain.repository.PeriodicidadRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PeriodicidadMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PeriodicidadService {

    private final PeriodicidadRepository periodicidadRepository;
    private final PeriodicidadMapper periodicidadMapper;

    public PeriodicidadService(PeriodicidadRepository periodicidadRepository, PeriodicidadMapper periodicidadMapper) {
        this.periodicidadRepository = periodicidadRepository;
        this.periodicidadMapper = periodicidadMapper;
    }

    public Uni<List<PeriodicidadResponse>> consultaTodos() {
        return periodicidadRepository.consultarTodos()
                .map(periodicidadMapper::toResponseList);
    }
}
