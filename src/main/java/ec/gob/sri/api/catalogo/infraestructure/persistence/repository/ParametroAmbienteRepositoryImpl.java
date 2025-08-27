package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.domain.model.entity.ParametroAmbiente;
import ec.gob.sri.api.catalogo.domain.repository.ParametroAmbienteRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.ParametroAmbienteEntity;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.ParametroAmbienteMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ParametroAmbienteRepositoryImpl implements ParametroAmbienteRepository {

    @Inject ParametroAmbientePanacheRepository repo;
    @Inject ParametroAmbienteMapper mapper;

  
    @Override
    @WithSession
    public Uni<List<ParametroAmbiente>> consultarPorAmbienteYCodigoAplicacion(String nombreParametro, String codigoAplicacion) {
        return repo.find("eliminado = 'N' and  estado = 'A' and nombreParametro = ?1 and codigoAplicacion = ?2",
                          nombreParametro, codigoAplicacion)
                          .list()
                   .map(mapper::toDomainList); 
    }
}