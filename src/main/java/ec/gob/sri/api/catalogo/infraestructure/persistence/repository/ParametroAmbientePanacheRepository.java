package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.ParametroAmbienteEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParametroAmbientePanacheRepository implements PanacheRepository<ParametroAmbienteEntity> {
}
