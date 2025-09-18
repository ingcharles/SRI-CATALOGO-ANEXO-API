package ec.gob.sri.api.catalogo.application.service;

import ec.gob.sri.api.catalogo.application.dto.PeriodicidadResponse;
import ec.gob.sri.api.catalogo.domain.repository.PeriodicidadRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PeriodicidadMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PeriodicidadService {

    @Inject 
    PeriodicidadRepository periodicidadRepository;
    
    @Inject 
    PeriodicidadMapper periodicidadMapper;

    public Uni<List<PeriodicidadResponse>> consultaTodos(){
        return periodicidadRepository.consultarTodos()
                   .map(periodicidadMapper::toResponseList);
    }
}

