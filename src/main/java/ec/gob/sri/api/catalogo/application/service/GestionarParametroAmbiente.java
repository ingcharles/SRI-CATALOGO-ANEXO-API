package ec.gob.sri.api.catalogo.application.service;

import java.util.List;

import ec.gob.sri.api.catalogo.application.dto.ParametroAmbienteResponse;
import ec.gob.sri.api.catalogo.domain.repository.ParametroAmbienteRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.ParametroAmbienteMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GestionarParametroAmbiente {

    private final ParametroAmbienteRepository repositorio;
    private final ParametroAmbienteMapper mapper;

    public GestionarParametroAmbiente(ParametroAmbienteRepository repositorio, ParametroAmbienteMapper mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    public Uni<List<ParametroAmbienteResponse>> consultarPorAmbienteYCodigoAplicacion(String ambiente,
            String codigoApp) {
        return repositorio.consultarPorAmbienteYCodigoAplicacion(ambiente, codigoApp)
                .map(mapper::toResponseList);
    }
}
