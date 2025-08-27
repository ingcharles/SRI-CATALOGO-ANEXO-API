package ec.gob.sri.api.catalogo.application.service;

import ec.gob.sri.api.catalogo.application.dto.ParametroAmbienteResponse;
import ec.gob.sri.api.catalogo.domain.repository.ParametroAmbienteRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.ParametroAmbienteMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class GestionarParametroAmbiente {

    @Inject 
    ParametroAmbienteRepository repositorio;
    
    @Inject 
    ParametroAmbienteMapper mapper;

    public Uni<List<ParametroAmbienteResponse>> consultarPorAmbienteYCodigoAplicacion(String ambiente, String codigoApp){
        return repositorio.consultarPorAmbienteYCodigoAplicacion(ambiente, codigoApp)
                   .map(mapper::toResponseList);
    }
}

