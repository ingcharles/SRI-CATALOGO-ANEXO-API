package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.domain.model.entity.PlantillaFormulario;
import ec.gob.sri.api.catalogo.domain.repository.PlantillaFormularioRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.PlantillaFormularioEntity;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PlantillaFormularioMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación del repositorio de PlantillaFormulario
 */
@ApplicationScoped
public class PlantillaFormularioRepositoryImpl implements PlantillaFormularioRepository {

    @Inject
    PlantillaFormularioPanacheRepository panacheRepository;

    @Inject
    PlantillaFormularioMapper mapper;

    @Override
    @WithTransaction
    public Uni<PlantillaFormulario> guardar(PlantillaFormulario plantilla) {
        PlantillaFormularioEntity entity = mapper.toEntity(plantilla);
        // El ID se genera automáticamente por la secuencia de Oracle
        entity.setFechaCreacion(LocalDateTime.now());
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setAudUsuarioCrea("SYSTEM"); // TODO: Obtener usuario del contexto
        entity.setAudFechaCrea(LocalDateTime.now());

        return panacheRepository.persist(entity)
                .map(mapper::toDomain);
    }

    @Override
    @WithTransaction
    public Uni<PlantillaFormulario> actualizar(PlantillaFormulario plantilla) {
        return panacheRepository.findById(plantilla.getCodigoPlantillaFormulario())
                .onItem().ifNotNull().transformToUni(entity -> {
                    if (plantilla.getCodigo() != null) {
                        entity.setCodigo(plantilla.getCodigo());
                    }
                    if (plantilla.getNombre() != null) {
                        entity.setNombre(plantilla.getNombre());
                    }
                    if (plantilla.getDescripcion() != null) {
                        entity.setDescripcion(plantilla.getDescripcion());
                    }
                    if (plantilla.getVersion() != null) {
                        entity.setVersion(plantilla.getVersion());
                    }
                    if (plantilla.getPaginas() != null) {
                        entity.setPaginas(plantilla.getPaginas());
                    }
                    entity.setFechaActualizacion(LocalDateTime.now());
                    entity.setAudUsuarioModifica("SYSTEM"); // TODO: Obtener usuario del contexto
                    entity.setAudFechaModifica(LocalDateTime.now());

                    return panacheRepository.persist(entity)
                            .map(mapper::toDomain);
                });
    }

    @Override
    @WithSession
    public Uni<PlantillaFormulario> buscarPorId(Long codigoPlantillaFormulario) {
        return panacheRepository.find("codigoPlantillaFormulario = ?1 AND eliminado = 'N'", codigoPlantillaFormulario)
                .firstResult()
                .map(entity -> entity != null ? mapper.toDomain(entity) : null);
    }

    @Override
    @WithSession
    public Uni<PlantillaFormulario> buscarPorCodigoYVersion(String codigo, String version) {
        return panacheRepository.find("codigo = ?1 AND version = ?2 AND eliminado = 'N'", codigo, version)
                .firstResult()
                .map(entity -> entity != null ? mapper.toDomain(entity) : null);
    }

    @Override
    @WithSession
    public Uni<List<PlantillaFormulario>> listar(Page page, Sort sort, String buscar) {
        StringBuilder query = new StringBuilder("eliminado = 'N'");
        Map<String, Object> params = new HashMap<>();

        if (buscar != null && !buscar.isEmpty()) {
            query.append(
                    " AND (LOWER(nombre) LIKE :buscar OR LOWER(codigo) LIKE :buscar OR LOWER(descripcion) LIKE :buscar)");
            params.put("buscar", "%" + buscar.toLowerCase() + "%");
        }

        if (params.isEmpty()) {
            return panacheRepository.find(query.toString(), sort)
                    .page(page)
                    .list()
                    .map(mapper::toDomainList);
        } else {
            return panacheRepository.find(query.toString(), sort, params)
                    .page(page)
                    .list()
                    .map(mapper::toDomainList);
        }
    }

    @Override
    @WithSession
    public Uni<Long> contar(String buscar) {
        StringBuilder query = new StringBuilder("eliminado = 'N' AND estado = 'A'");
        Map<String, Object> params = new HashMap<>();

        if (buscar != null && !buscar.isEmpty()) {
            query.append(
                    " AND (LOWER(nombre) LIKE :buscar OR LOWER(codigo) LIKE :buscar OR LOWER(descripcion) LIKE :buscar)");
            params.put("buscar", "%" + buscar.toLowerCase() + "%");
        }

        if (params.isEmpty()) {
            return panacheRepository.count(query.toString());
        } else {
            return panacheRepository.count(query.toString(), params);
        }
    }

    @Override
    @WithTransaction
    public Uni<Boolean> eliminar(Long codigoPlantillaFormulario) {
        return panacheRepository.findById(codigoPlantillaFormulario)
                .onItem().ifNotNull().transformToUni(entity -> {
                    entity.setEliminado("S");
                    entity.setEstado("I");
                    entity.setAudUsuarioElimina("SYSTEM"); // TODO: Obtener usuario del contexto
                    entity.setAudFechaElimina(LocalDateTime.now());
                    entity.setFechaActualizacion(LocalDateTime.now());

                    return panacheRepository.persist(entity)
                            .map(e -> true);
                })
                .onItem().ifNull().continueWith(false);
    }
}
