package ec.gob.sri.api.catalogo.application.service;

import ec.gob.sri.api.catalogo.application.dto.UbicacionGeograficaResponse;
import ec.gob.sri.api.catalogo.domain.repository.UbicacionGeograficaRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.UbicacionGeograficaMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UbicacionGeograficaService {

    @Inject
    UbicacionGeograficaRepository ubicacionGeograficaRepository;

    @Inject
    UbicacionGeograficaMapper ubicacionGeograficaMapper;

    public Uni<List<UbicacionGeograficaResponse>> consultarTodos() {
        return ubicacionGeograficaRepository.consultarTodos()
                .map(ubicacionGeograficaMapper::toResponseList);
    }

    public Uni<List<UbicacionGeograficaResponse>> consultarPorCodigoNivelGeografico(String codNivelGeografico) {
        return ubicacionGeograficaRepository.consultarPorCodigoNivelGeografico(codNivelGeografico)
                .map(ubicacionGeograficaMapper::toResponseList);
    }
}

