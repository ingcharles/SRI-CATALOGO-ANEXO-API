package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import java.util.List;

import ec.gob.sri.api.catalogo.domain.model.entity.ParametroAmbiente;
import ec.gob.sri.api.catalogo.domain.repository.ParametroAmbienteRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.ParametroAmbienteMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParametroAmbienteRepositoryImpl implements ParametroAmbienteRepository {

    private final ParametroAmbientePanacheRepository repo;
    private final ParametroAmbienteMapper mapper;

    public ParametroAmbienteRepositoryImpl(ParametroAmbientePanacheRepository repo, ParametroAmbienteMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @WithSession
    public Uni<List<ParametroAmbiente>> consultarPorAmbienteYCodigoAplicacion(String nombreParametro,
            String codigoAplicacion) {
        return repo.find("eliminado = 'N' and  estado = 'A' and ambiente = ?1 and codigoAplicacion = ?2",
                nombreParametro, codigoAplicacion)
                .list()
                .map(mapper::toDomainList);
    }
}