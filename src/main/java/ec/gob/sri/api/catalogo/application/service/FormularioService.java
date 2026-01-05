package ec.gob.sri.api.catalogo.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.sri.api.catalogo.application.dto.ConsultarFormulariosRequest;
import ec.gob.sri.api.catalogo.application.dto.CrearFormularioRequest;
import ec.gob.sri.api.catalogo.application.dto.FormularioResponse;
import ec.gob.sri.api.catalogo.application.dto.ModificarFormularioRequest;
import ec.gob.sri.api.catalogo.application.dto.PaginadoResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.Formulario;
import ec.gob.sri.api.catalogo.domain.model.enums.EstadoPlantilla;
import ec.gob.sri.api.catalogo.domain.repository.FormularioRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.FormularioMapper;
import ec.gob.sri.api.catalogo.infraestructure.persistence.util.PaginacionUtil;
import ec.gob.sri.api.catalogo.infraestructure.persistence.util.ResultadoPaginado;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Servicio de aplicación para Formulario
 */
@ApplicationScoped
public class FormularioService {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final FormularioRepository repository;
  private final FormularioMapper mapper;

  public FormularioService(FormularioRepository repository, FormularioMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  /**
   * Guarda un nuevo formulario
   */
  public Uni<FormularioResponse> guardar(CrearFormularioRequest solicitud) {
    Formulario formulario = mapper.toDomainFromRequest(solicitud);

    // Validar y establecer el estado
    formulario.setEstado(validarYConvertirEstado(solicitud.estado));

    // Convertir elementos Object a JSON String
    formulario.setElementos(convertirElementosAJson(solicitud.elementos));

    return repository.guardar(formulario)
        .map(mapper::toResponse);
  }

  /**
   * Actualiza un formulario existente
   */
  public Uni<FormularioResponse> actualizar(ModificarFormularioRequest solicitud,
      Long codigoFormulario) {
    return repository.buscarPorId(codigoFormulario)
        .onItem().transformToUni(existente -> {
          if (existente == null) {
            return Uni.createFrom().failure(
                new IllegalArgumentException(
                    "No se encontró el formulario con ID: " + codigoFormulario));
          }

          return actualizarFormulario(solicitud, existente);
        });
  }

  private Uni<FormularioResponse> actualizarFormulario(ModificarFormularioRequest solicitud,
      Formulario existente) {
    if (solicitud.codigoUsuario != null) {
      existente.setCodigoUsuario(solicitud.codigoUsuario);
    }
    if (solicitud.identificacionUsuario != null) {
      existente.setIdentificacionUsuario(solicitud.identificacionUsuario);
    }
    if (solicitud.estado != null) {
      existente.setEstado(validarYConvertirEstado(solicitud.estado));
    }
    if (solicitud.elementos != null) {
      existente.setElementos(convertirElementosAJson(solicitud.elementos));
    }

    return repository.actualizar(existente)
        .map(mapper::toResponse);
  }

  /**
   * Busca un formulario por ID
   */
  public Uni<FormularioResponse> buscarPorId(Long codigoFormulario) {
    return repository.buscarPorId(codigoFormulario)
        .onItem().transform(formulario -> {
          if (formulario == null) {
            throw new IllegalArgumentException(
                "No se encontró el formulario con ID: " + codigoFormulario);
          }
          return mapper.toResponse(formulario);
        });
  }

  /**
   * Lista formularios con paginación y filtros
   * <p>
   * Estructura clara de paginación: 1. Normalizar parámetros usando PaginacionUtil 2. Crear Page y
   * Sort 3. Ejecutar consulta paginada (obtiene lista + metadatos) 4. Mapear resultados a DTO con
   * metadata completa
   */
  public Uni<PaginadoResponse<FormularioResponse>> listar(ConsultarFormulariosRequest solicitud) {
    // 1. Normalizar parámetros de paginación usando utilidad
    Page pagina = PaginacionUtil.crearPage(solicitud.pagina, solicitud.limite);
    Sort ordenamiento = PaginacionUtil.crearOrdenamiento(solicitud.ordenarPor, solicitud.orden);

    // 2. Ejecutar consulta paginada con metadatos
    Uni<ResultadoPaginado<Formulario>> resultadoUni = repository.listar(pagina, ordenamiento,
        solicitud.identificacionUsuario, solicitud.buscar);

    // 3. Mapear resultados a DTO con respuesta completa
    return resultadoUni.map(resultado -> {
      List<FormularioResponse> contenidoFormularios = mapper.toResponseList(resultado.contenido);

      return new PaginadoResponse<>(
          contenidoFormularios,
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
  public Uni<Boolean> eliminar(Long codigoFormulario) {
    return repository.buscarPorId(codigoFormulario)
        .onItem().transformToUni(existente -> {
          if (existente == null) {
            return Uni.createFrom().failure(
                new IllegalArgumentException(
                    "No se encontró el formulario con ID: " + codigoFormulario));
          }
          return repository.eliminar(codigoFormulario);
        });
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
      throw new IllegalArgumentException(
          "Error al convertir elementos a JSON: " + e.getMessage(), e);
    }
  }

  /**
   * Valida y convierte el código de estado a EstadoPlantilla
   */
  private EstadoPlantilla validarYConvertirEstado(String estadoCodigo) {
    if (estadoCodigo == null) {
      return EstadoPlantilla.A; // Por defecto Activo
    }
    try {
      return EstadoPlantilla.fromCodigo(estadoCodigo);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Estado inválido: " + estadoCodigo
          + ". Los valores permitidos son: A (Activo), I (Inactivo).");
    }
  }
}
