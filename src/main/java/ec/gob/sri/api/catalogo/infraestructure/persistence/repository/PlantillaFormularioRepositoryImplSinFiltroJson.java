/*
package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.domain.model.entity.PlantillaFormulario;
import ec.gob.sri.api.catalogo.domain.repository.PlantillaFormularioRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.PlantillaFormularioEntity;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PlantillaFormularioMapper;
import ec.gob.sri.api.catalogo.application.dto.ResultadoPaginado;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * Implementación del repositorio de PlantillaFormulario
 *//*

@ApplicationScoped
public class PlantillaFormularioRepositoryImplSinFiltroJson implements
    PlantillaFormularioRepository {

  private final PlantillaFormularioPanacheRepository panacheRepository;
  private final PlantillaFormularioMapper mapper;

  public PlantillaFormularioRepositoryImplSinFiltroJson(
      PlantillaFormularioPanacheRepository panacheRepository,
      PlantillaFormularioMapper mapper) {
    this.panacheRepository = panacheRepository;
    this.mapper = mapper;
  }

  @Override
  @WithTransaction
  public Uni<PlantillaFormulario> guardar(PlantillaFormulario plantilla) {
    PlantillaFormularioEntity entity = mapper.toEntity(plantilla);
    entity.setFechaCreacion(LocalDateTime.now());
    entity.setFechaActualizacion(LocalDateTime.now());
    entity.setAudUsuarioCrea(
        "SYSTEM");
    entity.setAudFechaCrea(LocalDateTime.now());

    return panacheRepository.persist(entity)
        .map(mapper::toDomain);
  }

  @Override
  @WithTransaction
  public Uni<PlantillaFormulario> actualizar(PlantillaFormulario plantilla) {
    return panacheRepository.findById(plantilla.getCodigoPlantillaFormulario())
        .onItem().ifNotNull().transformToUni(entity -> {
          mapper.updateEntityFromDomain(plantilla, entity);
          entity.setFechaActualizacion(LocalDateTime.now());
          entity.setAudFechaModifica(LocalDateTime.now());

          return panacheRepository.persist(entity)
              .map(mapper::toDomain);
        });
  }

  @Override
  @WithTransaction
  public Uni<PlantillaFormulario> actualizarEstado(Long codigoPlantillaFormulario,
      PlantillaFormulario plantilla) {
    return panacheRepository.findById(codigoPlantillaFormulario)
        .onItem().ifNotNull().transformToUni(entity -> {
          if (plantilla.getEstado() != null) {
            entity.setEstado(plantilla.getEstado().name());
          }
          if (plantilla.getMotivo() != null) {
            entity.setMotivo(plantilla.getMotivo());
          }
          if (plantilla.getFechaRevision() != null) {
            entity.setFechaRevision(plantilla.getFechaRevision());
          }
          if (plantilla.getFechaAprobacion() != null) {
            entity.setFechaAprobacion(plantilla.getFechaAprobacion());
          }
          if (plantilla.getFechaPublicacion() != null) {
            entity.setFechaPublicacion(plantilla.getFechaPublicacion());
          }
          entity.setFechaActualizacion(LocalDateTime.now());
          entity.setAudUsuarioModifica("SYSTEM");
          entity.setAudFechaModifica(LocalDateTime.now());

          return panacheRepository.persist(entity)
              .map(mapper::toDomain);
        });
  }

  @Override
  @WithTransaction
  public Uni<PlantillaFormulario> buscarPorId(Long codigoPlantillaFormulario) {
    // ✅ Usando NamedQuery (pre-compilada, mejor performance)
    return panacheRepository.find("#PlantillaFormularioEntity.buscarPorId",
            Parameters.with("codigoPlantillaFormulario", codigoPlantillaFormulario))
        .firstResult()
        .map(entity -> entity != null ? mapper.toDomain(entity) : null);
  }

  @Override
  @WithTransaction
  public Uni<PlantillaFormulario> buscarPorCodigoYVersion(String codigo, String version) {
    return panacheRepository.find("codigo = ?1 AND version = ?2 AND eliminado = 'N'", codigo,
            version)
        .firstResult()
        .map(entity -> entity != null ? mapper.toDomain(entity) : null);
  }

  @Override
  @WithTransaction
  public Uni<ResultadoPaginado<PlantillaFormulario>> listar(Page page, Sort sort, String buscar) {

    // Construir condiciones y parámetros
    String where = construirCondicionesFiltro(buscar);
    Map<String, Object> parametros = construirParametrosFiltro(buscar);

    // Construir ordenamiento
    Sort sortAlias = construirSort(sort);

    // Query para datos (sin JOIN, más rápido que Formulario)
    String jpql = "SELECT p FROM PlantillaFormularioEntity p WHERE " + where;

    // Query optimizada
    var consulta = panacheRepository.find(jpql, sortAlias, parametros).page(page);

    // Count optimizado
    Uni<Long> totalUni = panacheRepository.find(jpql, parametros).count();

    // Lista de resultados
    Uni<List<PlantillaFormularioEntity>> listaUni = consulta.list();

    // Combinar resultados y calcular metadatos
    return Uni.combine().all()
        .unis(listaUni, totalUni)
        .asTuple()
        .map(tuple -> {
          List<PlantillaFormularioEntity> lista = tuple.getItem1();
          long total = tuple.getItem2();

          // Calcular metadatos de paginación
          int paginaActual = page.index + 1;
          int tamanio = page.size;
          int totalPaginas = (int) Math.ceil((double) total / tamanio);

          // Mapear a dominio
          List<PlantillaFormulario> contenido = mapper.toDomainList(lista);

          return new ResultadoPaginado<>(contenido, total, totalPaginas,
              paginaActual, tamanio);
        });
  }

  @Override
  @WithTransaction
  public Uni<Boolean> eliminar(Long codigoPlantillaFormulario) {
    return panacheRepository.findById(codigoPlantillaFormulario)
        .onItem().ifNotNull().transformToUni(entity -> {
          entity.setEliminado("S");
          entity.setEstado("I");
          entity.setAudUsuarioElimina("SYSTEM");
          entity.setAudFechaElimina(LocalDateTime.now());
          entity.setFechaActualizacion(LocalDateTime.now());
          return panacheRepository.persist(entity)
              .map(e -> true);
        })
        .onItem().ifNull().continueWith(false);
  }

  */
/**
 * Construye Sort con alias 'p' para JPQL
 *//*

  private Sort construirSort(Sort sort) {
    if (sort == null || sort.getColumns().isEmpty()) {
      return Sort.by("p.fechaCreacion", Sort.Direction.Descending);
    }

    Sort.Column col = sort.getColumns().get(0);
    return Sort.by("p." + col.getName(), col.getDirection());
  }

  */
/**
 * Construye las condiciones de filtro para la consulta JPQL El parámetro 'buscar' busca en: codigo,
 * nombre, descripcion, version
 *//*

  private String construirCondicionesFiltro(String buscar) {
    StringBuilder consulta = new StringBuilder("p.eliminado = 'N'");

    // Búsqueda global en múltiples campos
    if (buscar != null && !buscar.isEmpty()) {
      consulta.append(
          " AND (LOWER(p.codigo) LIKE :buscar " +
              "OR LOWER(p.nombre) LIKE :buscar " +
              "OR LOWER(p.descripcion) LIKE :buscar " +
              "OR LOWER(p.version) LIKE :buscar)");
    }

    return consulta.toString();
  }

  */
/**
 * Construye los parámetros para la consulta
 *//*

  private Map<String, Object> construirParametrosFiltro(String buscar) {
    Map<String, Object> parametros = new HashMap<>();

    if (buscar != null && !buscar.isEmpty()) {
      parametros.put("buscar", "%" + buscar.toLowerCase() + "%");
    }

    return parametros;
  }
}
*/
