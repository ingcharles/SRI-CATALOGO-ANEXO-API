package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.PlantillaFormularioEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositorio Panache para PlantillaFormularioEntity
 */
@ApplicationScoped
public class PlantillaFormularioPanacheRepository implements PanacheRepositoryBase<PlantillaFormularioEntity, Long> {
}
