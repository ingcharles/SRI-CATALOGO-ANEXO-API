package ec.gob.sri.api.catalogo.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.sri.api.catalogo.application.dto.ActualizarPlantillaFormularioRequest;
import ec.gob.sri.api.catalogo.application.dto.CambiarEstadoPlantillaRequest;
import ec.gob.sri.api.catalogo.application.dto.GuardarPlantillaFormularioRequest;
import ec.gob.sri.api.catalogo.application.dto.GuardarPlantillaFormularioResponse;
import ec.gob.sri.api.catalogo.application.dto.ListarPlantillaFormulariosRequest;
import ec.gob.sri.api.catalogo.application.dto.PaginadoResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.PlantillaFormulario;
import ec.gob.sri.api.catalogo.domain.model.enums.EstadoPlantilla;
import ec.gob.sri.api.catalogo.domain.repository.PlantillaFormularioRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PlantillaFormularioMapper;
import ec.gob.sri.api.catalogo.infraestructure.persistence.util.PaginacionUtil;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Servicio de aplicación para Plantilla Formulario
 */
@ApplicationScoped
public class PlantillaFormularioService {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final PlantillaFormularioRepository repository;
  private final PlantillaFormularioMapper mapper;

  public PlantillaFormularioService(PlantillaFormularioRepository repository,
      PlantillaFormularioMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  /**
   * Guarda un nuevo formulario
   */
  public Uni<GuardarPlantillaFormularioResponse> guardar(
      GuardarPlantillaFormularioRequest request) {
    // Validar que el código y versión no existan
    return repository.buscarPorCodigoYVersion(request.codigo, request.version)
        .onItem().transformToUni(existente -> {
          if (existente != null) {
            return Uni.createFrom().failure(
                new IllegalArgumentException(
                    "Ya existe un formulario con el código: " + request.codigo
                        + " y versión: " + request.version));
          }

          PlantillaFormulario plantilla = mapper.toDomainFromRequest(request);

          // Validar y establecer el estado
          plantilla.setEstado(validarYConvertirEstado(request.estado));

          // Convertir paginas Object a JSON String
          plantilla.setElementosJson(convertirElementosAJson(request.elementosJson));

          plantilla.setElementosXml(request.elementosXml);

          return repository.guardar(plantilla)
              .map(mapper::toResponse);
        });
  }

  /**
   * Actualiza un formulario existente
   */
  public Uni<GuardarPlantillaFormularioResponse> actualizar(
      ActualizarPlantillaFormularioRequest request,
      Long codigoPlantillaFormulario) {
    return repository.buscarPorId(codigoPlantillaFormulario)
        .onItem().transformToUni(existente ->
            procesarActualizacion(request, codigoPlantillaFormulario, existente)
        );
  }

  private Uni<GuardarPlantillaFormularioResponse> procesarActualizacion(
      ActualizarPlantillaFormularioRequest request,
      Long codigoPlantillaFormulario,
      PlantillaFormulario existente) {

    if (existente == null) {
      return Uni.createFrom().failure(
          new IllegalArgumentException(
              "No se encontró el formulario con ID: " + codigoPlantillaFormulario));
    }

    if (seModificoCodigoOVersion(request, existente)) {
      return validarYActualizar(request, existente);
    }

    return actualizarPlantilla(request, existente);
  }

  private boolean seModificoCodigoOVersion(ActualizarPlantillaFormularioRequest request,
      PlantillaFormulario existente) {
    boolean codigoCambiado =
        request.codigo != null && !request.codigo.equals(existente.getCodigo());
    boolean versionCambiada =
        request.version != null && !request.version.equals(existente.getVersion());
    return codigoCambiado || versionCambiada;
  }

  private Uni<GuardarPlantillaFormularioResponse> validarYActualizar(
      ActualizarPlantillaFormularioRequest request,
      PlantillaFormulario existente) {

    String codigoABuscar = request.codigo != null ? request.codigo : existente.getCodigo();
    String versionABuscar = request.version != null ? request.version : existente.getVersion();

    return repository.buscarPorCodigoYVersion(codigoABuscar, versionABuscar)
        .onItem().transformToUni(otro ->
            validarDuplicadoYActualizar(request, existente, otro, codigoABuscar, versionABuscar)
        );
  }

  private Uni<GuardarPlantillaFormularioResponse> validarDuplicadoYActualizar(
      ActualizarPlantillaFormularioRequest request,
      PlantillaFormulario existente,
      PlantillaFormulario otro,
      String codigoABuscar,
      String versionABuscar) {

    if (esDuplicado(existente, otro)) {
      return Uni.createFrom().failure(
          new IllegalArgumentException(
              "Ya existe otro formulario con el código: " + codigoABuscar
                  + " y versión: " + versionABuscar));
    }

    return actualizarPlantilla(request, existente);
  }

  private boolean esDuplicado(PlantillaFormulario existente, PlantillaFormulario otro) {
    return otro != null &&
        !otro.getCodigoPlantillaFormulario().equals(existente.getCodigoPlantillaFormulario());
  }

  private Uni<GuardarPlantillaFormularioResponse> actualizarPlantilla(
      ActualizarPlantillaFormularioRequest request,
      PlantillaFormulario existente) {
    if (request.codigo != null) {
      existente.setCodigo(request.codigo);
    }
    if (request.nombre != null) {
      existente.setNombre(request.nombre);
    }
    if (request.descripcion != null) {
      existente.setDescripcion(request.descripcion);
    }
    if (request.version != null) {
      existente.setVersion(request.version);
    }
    if (request.estado != null) {
      existente.setEstado(validarYConvertirEstado(request.estado));
    }
    if (request.elementosJson != null) {
      existente.setElementosJson(convertirElementosAJson(request.elementosJson));
    }
    if (request.elementosXml != null) {
      existente.setElementosXml(request.elementosXml);
    }
    return repository.actualizar(existente)
        .map(mapper::toResponse);
  }

  /**
   * Busca un formulario por ID
   */
  public Uni<GuardarPlantillaFormularioResponse> buscarPorId(Long codigoPlantillaFormulario) {
    return repository.buscarPorId(codigoPlantillaFormulario)
        .onItem().transform(plantilla -> {
          if (plantilla == null) {
            throw new IllegalArgumentException(
                "No se encontró el formulario con ID: " + codigoPlantillaFormulario);
          }
          return mapper.toResponse(plantilla);
        });
  }

  /**
   * Lista formularios con paginación y filtros
   */
  public Uni<PaginadoResponse<GuardarPlantillaFormularioResponse>> listar(
      ListarPlantillaFormulariosRequest request) {
    // Normalizar parámetros de paginación usando utilidad
    Page page = PaginacionUtil.crearPage(request.pagina, request.limite);
    Sort sort = PaginacionUtil.crearOrdenamiento(request.ordenarPor, request.orden);

    // Ejecutar consulta paginada con metadatos
    return repository.listar(page, sort, request.buscar)
        .map(resultado -> {
          List<GuardarPlantillaFormularioResponse> contenido = mapper.toResponseList(
              resultado.contenido);

          // Parse elementosJson (stored as String in domain) into List<Map<String,Object>> in each response
          for (int i = 0; i < contenido.size(); i++) {
            GuardarPlantillaFormularioResponse dto = contenido.get(i);
            PlantillaFormulario dominio = resultado.contenido.get(i);
            String elementosStr = dominio.getElementosJson();
            if (elementosStr != null && !elementosStr.isEmpty()) {
              try {
                List<Map<String, Object>> parsed = objectMapper.readValue(
                    elementosStr,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
                dto.elementosJson = parsed;
              } catch (JsonProcessingException e) {
                dto.elementosJson = null;
              }
            } else {
              dto.elementosJson = null;
            }
          }

          return new PaginadoResponse<>(
              contenido,
              resultado.totalElementos,
              resultado.totalPaginas,
              resultado.paginaActual,
              resultado.tamanio
          );
        });
  }

  /**
   * Elimina lógicamente un formulario
   */
  public Uni<Boolean> eliminar(Long codigoPlantillaFormulario) {
    return repository.buscarPorId(codigoPlantillaFormulario)
        .onItem().transformToUni(existente -> {
          if (existente == null) {
            return Uni.createFrom().failure(
                new IllegalArgumentException(
                    "No se encontró el formulario con ID: " + codigoPlantillaFormulario));
          }
          return repository.eliminar(codigoPlantillaFormulario);
        });
  }

  /**
   * Cambia el estado de una plantilla de formulario
   */

  public Uni<GuardarPlantillaFormularioResponse> cambiarEstado(Long codigoPlantillaFormulario,
      CambiarEstadoPlantillaRequest request) {
    return repository.buscarPorId(codigoPlantillaFormulario)
        .onItem().transformToUni(existente ->
            procesarCambioEstado(codigoPlantillaFormulario, request, existente)
        );
  }

  private Uni<GuardarPlantillaFormularioResponse> procesarCambioEstado(
      Long codigoPlantillaFormulario,
      CambiarEstadoPlantillaRequest request,
      PlantillaFormulario existente) {

    if (existente == null) {
      return Uni.createFrom().failure(
          new IllegalArgumentException(
              "No se encontró la plantilla con ID: " + codigoPlantillaFormulario));
    }

    EstadoPlantilla nuevoEstado = validarYConvertirEstado(request.estado);

    Uni<Void> validacion = validarTransicionEstado(existente, nuevoEstado, request);

    return validacion.onItem().transformToUni(unused -> {
      actualizarEstadoYCampos(existente, nuevoEstado, request);
      return repository.actualizarEstado(codigoPlantillaFormulario, existente)
          .map(mapper::toResponse);
    });
  }

  private Uni<Void> validarTransicionEstado(
      PlantillaFormulario existente,
      EstadoPlantilla nuevoEstado,
      CambiarEstadoPlantillaRequest request) {

    if (!EstadoPlantilla.esTransicionValida(existente.getEstado(), nuevoEstado)) {
      return Uni.createFrom().failure(
          new IllegalArgumentException(
              EstadoPlantilla.getMensajeTransicionInvalida(existente.getEstado(), nuevoEstado)
                  + ". Estado actual: " + existente.getEstado().getDescripcion()));
    }

    if (existente.getEstado() == EstadoPlantilla.ER
        && nuevoEstado == EstadoPlantilla.EC
        && (request.motivo == null || request.motivo.trim().isEmpty())) {

      return Uni.createFrom().failure(
          new IllegalArgumentException(
              "El motivo es obligatorio cuando se rechaza una plantilla (EN REVISIÓN -> EN CONSTRUCCIÓN)"));
    }

    return Uni.createFrom().voidItem();
  }

  private void actualizarEstadoYCampos(
      PlantillaFormulario existente,
      EstadoPlantilla nuevoEstado,
      CambiarEstadoPlantillaRequest request) {

    existente.setEstado(nuevoEstado);
    actualizarMotivo(existente, nuevoEstado, request);
    actualizarFechas(existente, nuevoEstado);
  }

  private void actualizarMotivo(
      PlantillaFormulario existente,
      EstadoPlantilla nuevoEstado,
      CambiarEstadoPlantillaRequest request) {

    if (nuevoEstado == EstadoPlantilla.AP) {
      existente.setMotivo("");
    } else {
      existente.setMotivo(request.motivo);
    }
  }

  private void actualizarFechas(PlantillaFormulario existente, EstadoPlantilla nuevoEstado) {
    LocalDateTime ahora = LocalDateTime.now();

    if (nuevoEstado == EstadoPlantilla.ER) {
      existente.setFechaRevision(ahora);
    }

    if (nuevoEstado == EstadoPlantilla.AP) {
      existente.setFechaAprobacion(ahora);
    }

    if (nuevoEstado == EstadoPlantilla.PU) {
      existente.setFechaPublicacion(ahora);
    }
  }

  /**
   * Convierte el objeto elementos a JSON String
   */
  private String convertirElementosAJson(Object elementos) {
    if (elementos == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(elementos);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error al convertir páginas a JSON: " + e.getMessage(), e);
    }
  }

  /**
   * Valida y convierte el código de estado a EstadoPlantilla
   */
  private EstadoPlantilla validarYConvertirEstado(String estadoCodigo) {
    if (estadoCodigo == null) {
      return EstadoPlantilla.EC; // Por defecto EN CONSTRUCCIÓN
    }
    try {
      return EstadoPlantilla.fromCodigo(estadoCodigo);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Estado inválido: " + estadoCodigo
          + ". Los valores permitidos son: EC (EN CONSTRUCCIÓN), ER (EN REVISIÓN), AP (APROBADO), PU (PUBLICADO)");
    }
  }
}
