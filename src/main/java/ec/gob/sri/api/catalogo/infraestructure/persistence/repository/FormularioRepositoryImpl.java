package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.domain.model.entity.Formulario;
import ec.gob.sri.api.catalogo.domain.repository.FormularioRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.FormularioEntity;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.FormularioMapper;
import ec.gob.sri.api.catalogo.infraestructure.persistence.util.ResultadoPaginado;
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

/**
 * Implementación del repositorio de Formulario
 */
@ApplicationScoped
public class FormularioRepositoryImpl implements FormularioRepository {

  private final FormularioPanacheRepository panacheRepository;
  private final PlantillaFormularioPanacheRepository plantillaRepository;
  private final FormularioMapper mapper;

  public FormularioRepositoryImpl(FormularioPanacheRepository panacheRepository,
      PlantillaFormularioPanacheRepository plantillaRepository,
      FormularioMapper mapper) {
    this.panacheRepository = panacheRepository;
    this.plantillaRepository = plantillaRepository;
    this.mapper = mapper;
  }

  @Override
  @WithTransaction
  public Uni<Formulario> guardar(Formulario formulario) {
    // Primero buscar la plantilla para establecer la relación
    return plantillaRepository.findById(formulario.getCodigoPlantillaFormulario())
        .onItem().ifNotNull().transformToUni(plantilla -> {
          FormularioEntity entidad = mapper.toEntity(formulario);
          // Establecer la relación con la plantilla
          entidad.setPlantillaFormulario(plantilla);
          // El ID se genera automáticamente por la secuencia de Oracle
          entidad.setFechaCreacion(LocalDateTime.now());
          entidad.setFechaActualizacion(LocalDateTime.now());
          entidad.setAudUsuarioCrea("SYSTEM");
          entidad.setAudFechaCrea(LocalDateTime.now());

          return panacheRepository.persist(entidad)
              .map(mapper::toDomain);
        })
        .onItem().ifNull().failWith(
            () -> new IllegalArgumentException("Plantilla no encontrada con código: "
                + formulario.getCodigoPlantillaFormulario()));
  }

  @Override
  @WithTransaction
  public Uni<Formulario> actualizar(Formulario formulario) {
    return panacheRepository.findById(formulario.getCodigoFormulario())
        .onItem().ifNotNull().transformToUni(entidad -> {
          // Actualizar campos simples
          if (formulario.getCodigoUsuario() != null) {
            entidad.setCodigoUsuario(formulario.getCodigoUsuario());
          }
          if (formulario.getIdentificacionUsuario() != null) {
            entidad.setIdentificacionUsuario(formulario.getIdentificacionUsuario());
          }
          if (formulario.getElementos() != null) {
            entidad.setElementos(formulario.getElementos());
          }
          if (formulario.getEstado() != null) {
            entidad.setEstado(formulario.getEstado().getCodigo());
          }

          // Si se cambió la plantilla, actualizar la relación
          if (formulario.getCodigoPlantillaFormulario() != null
              && entidad.getPlantillaFormulario() != null
              && !formulario.getCodigoPlantillaFormulario()
              .equals(entidad.getPlantillaFormulario().getCodigoPlantillaFormulario())) {
            return plantillaRepository.findById(formulario.getCodigoPlantillaFormulario())
                .onItem().ifNotNull().transformToUni(plantilla -> {
                  entidad.setPlantillaFormulario(plantilla);
                  entidad.setFechaActualizacion(LocalDateTime.now());
                  entidad.setAudUsuarioModifica("SYSTEM");
                  entidad.setAudFechaModifica(LocalDateTime.now());

                  return panacheRepository.persist(entidad)
                      .map(mapper::toDomain);
                })
                .onItem().ifNull().failWith(
                    () -> new IllegalArgumentException(
                        "Plantilla no encontrada con código: "
                            + formulario.getCodigoPlantillaFormulario()));
          }

          // Si no se cambió la plantilla, solo actualizar campos
          entidad.setFechaActualizacion(LocalDateTime.now());
          entidad.setAudUsuarioModifica("SYSTEM");
          entidad.setAudFechaModifica(LocalDateTime.now());

          return panacheRepository.persist(entidad)
              .map(mapper::toDomain);
        });
  }

  @Override
  @WithTransaction
  public Uni<Formulario> buscarPorId(Long codigoFormulario) {
    // ✅ Usando NamedQuery (pre-compilada, mejor performance)
    return panacheRepository.find("#FormularioEntity.buscarPorIdConPlantilla",
            Parameters.with("codigoFormulario", codigoFormulario))
        .firstResult()
        .map(entidad -> entidad != null ? mapper.toDomain(entidad) : null);
  }

  @Override
  @WithTransaction
  public Uni<ResultadoPaginado<Formulario>> listar(Page page, Sort sort,
      String identificacionUsuario, String buscar) {

    // Construir condiciones y parámetros
    String where = construirCondicionesFiltro(identificacionUsuario, buscar);
    Map<String, Object> parametros = construirParametrosFiltro(identificacionUsuario, buscar);

    // Construir ordenamiento
    Sort sortAlias = construirSort(sort);

    // ✅ SIN DISTINCT - Más rápido, eliminamos duplicados en memoria si es necesario
    String jpql = "SELECT f FROM FormularioEntity f " +
        "LEFT JOIN FETCH f.plantillaFormulario p " +
        "WHERE " + where;

    // Query optimizada
    var consulta = panacheRepository.find(jpql, sortAlias, parametros).page(page);

    // Count optimizado - Sin JOIN cuando no hay filtros de plantilla
    Uni<Long> totalUni = obtenerTotalFormularios(where, parametros,
        buscar);

    Uni<List<FormularioEntity>> listaUni = consulta.list(); // Eliminar duplicados por JOIN si existen

    // Combinar resultados y calcular metadatos
    return Uni.combine().all()
        .unis(listaUni, totalUni)
        .asTuple()
        .map(tuple -> {
          List<FormularioEntity> lista = tuple.getItem1();
          long total = tuple.getItem2();

          // Calcular metadatos de paginación
          int paginaActual = page.index + 1;
          int tamanio = page.size;
          int totalPaginas = (int) Math.ceil((double) total / tamanio);

          // Mapear a dominio - las relaciones lazy ya están cargadas por JOIN FETCH
          List<Formulario> contenido = mapper.toDomainList(lista);

          return new ResultadoPaginado<>(contenido, total, totalPaginas,
              paginaActual, tamanio);
        });
  }

  /**
   * Count optimizado - sin JOIN cuando es posible
   */
  private Uni<Long> obtenerTotalFormularios(String where, Map<String, Object> parametros,
      String buscar) {

    // Si hay búsqueda en campos de plantilla, necesitamos JOIN
    boolean necesitaJoin = buscar != null && !buscar.isEmpty();

    String countJpql;
    if (necesitaJoin) {
      // Con JOIN (búsqueda en plantilla)
      countJpql = "SELECT COUNT(f) FROM FormularioEntity f " +
          "LEFT JOIN f.plantillaFormulario p " +
          "WHERE " + where;
    } else {
      // Sin JOIN (más rápido)
      countJpql = "SELECT COUNT(f) FROM FormularioEntity f WHERE " + where;
    }

    return panacheRepository.find(countJpql, parametros).count();
  }

  /**
   * Construye Sort con alias 'f' para JPQL
   */
  private Sort construirSort(Sort sort) {
    if (sort == null || sort.getColumns().isEmpty()) {
      return Sort.by("f.codigoFormulario", Sort.Direction.Ascending);
    }

    Sort.Column col = sort.getColumns().get(0);
    return Sort.by("f." + col.getName(), col.getDirection());
  }

  /**
   * Construye las condiciones de filtro para la consulta JPQL El parámetro 'buscar' busca en:
   * nombre, version, descripcion (plantilla), identificacionUsuario y codigoUsuario (formulario)
   */
  private String construirCondicionesFiltro(String identificacionUsuario, String buscar) {
    StringBuilder consulta = new StringBuilder("f.eliminado = 'N'");

    if (identificacionUsuario != null && !identificacionUsuario.isEmpty()) {
      consulta.append(" AND LOWER(f.identificacionUsuario) LIKE :identificacionUsuario");
    }

    // Búsqueda global en múltiples campos
    if (buscar != null && !buscar.isEmpty()) {
      consulta.append(
          " AND (LOWER(CAST(f.codigoFormulario AS string)) LIKE :buscar " +
              "OR LOWER(f.codigoUsuario) LIKE :buscar " +
              "OR LOWER(f.identificacionUsuario) LIKE :buscar " +
              "OR LOWER(p.nombre) LIKE :buscar " +
              "OR LOWER(p.version) LIKE :buscar " +
              "OR LOWER(p.descripcion) LIKE :buscar)");

    }

    return consulta.toString();
  }

  /**
   * Construye los parámetros para la consulta
   */
  private Map<String, Object> construirParametrosFiltro(String identificacionUsuario,
      String buscar) {
    Map<String, Object> parametros = new HashMap<>();

    if (identificacionUsuario != null && !identificacionUsuario.isEmpty()) {
      parametros.put("identificacionUsuario", "%" + identificacionUsuario.toLowerCase() + "%");
    }

    if (buscar != null && !buscar.isEmpty()) {
      parametros.put("buscar", "%" + buscar.toLowerCase() + "%");
    }

    return parametros;
  }

  @Override
  @WithTransaction
  public Uni<Boolean> eliminar(Long codigoFormulario) {
    return panacheRepository.findById(codigoFormulario)
        .onItem().ifNotNull().transformToUni(entidad -> {
          entidad.setEliminado("S");
          entidad.setEstado("I");
          entidad.setAudUsuarioElimina("SYSTEM");
          entidad.setAudFechaElimina(LocalDateTime.now());
          entidad.setFechaActualizacion(LocalDateTime.now());

          return panacheRepository.persist(entidad)
              .map(e -> true);
        })
        .onItem().ifNull().continueWith(false);
  }
}
