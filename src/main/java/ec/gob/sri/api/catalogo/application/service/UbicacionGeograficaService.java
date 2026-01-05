package ec.gob.sri.api.catalogo.application.service;

import java.util.List;

import ec.gob.sri.api.catalogo.application.dto.UbicacionGeograficaResponse;
import ec.gob.sri.api.catalogo.domain.repository.UbicacionGeograficaRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.UbicacionGeograficaMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UbicacionGeograficaService {

    private final UbicacionGeograficaRepository ubicacionGeograficaRepository;
    private final UbicacionGeograficaMapper ubicacionGeograficaMapper;

    public UbicacionGeograficaService(UbicacionGeograficaRepository ubicacionGeograficaRepository,
            UbicacionGeograficaMapper ubicacionGeograficaMapper) {
        this.ubicacionGeograficaRepository = ubicacionGeograficaRepository;
        this.ubicacionGeograficaMapper = ubicacionGeograficaMapper;
    }

    public Uni<List<UbicacionGeograficaResponse>> consultarTodos() {
        return ubicacionGeograficaRepository.consultarTodos()
                .map(ubicacionGeograficaMapper::toResponseList);
    }

    public Uni<List<UbicacionGeograficaResponse>> consultarPorCodigoNivelGeografico(String codNivelGeografico) {
        return ubicacionGeograficaRepository.consultarPorCodigoNivelGeografico(codNivelGeografico)
                .map(ubicacionGeograficaMapper::toResponseList);
    }
}
