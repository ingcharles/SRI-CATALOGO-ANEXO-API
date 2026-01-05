package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.FormularioEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositorio Panache para FormularioEntity
 */
@ApplicationScoped
public class FormularioPanacheRepository implements PanacheRepositoryBase<FormularioEntity, Long> {
}
