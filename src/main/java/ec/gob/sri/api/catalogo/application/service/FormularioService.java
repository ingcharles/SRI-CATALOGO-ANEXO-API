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
     * 
     * Responsabilidades:
     * - Orquestar el flujo de negocio
     * - Normalizar parámetros de entrada
     * - Transformar respuesta a DTOs de salida
     * - NO contiene lógica de infraestructura
     */
    public Uni<PaginadoResponse<FormularioResponse>> listar(ConsultarFormulariosRequest solicitud) {
        System.out.println("[FormularioService.listar] 🔍 INICIO - Solicitud recibida:");
        System.out.println("  - pagina: " + solicitud.pagina);
        System.out.println("  - limite: " + solicitud.limite);
        System.out.println("  - codigoPlantillaFormulario: " + solicitud.codigoPlantillaFormulario);
        System.out.println("  - codigoUsuario: " + solicitud.codigoUsuario);
        System.out.println("  - identificacionUsuario: " + solicitud.identificacionUsuario);
        System.out.println("  - buscar: " + solicitud.buscar);
        System.out.println("  - ordenarPor: " + solicitud.ordenarPor);
        System.out.println("  - orden: " + solicitud.orden);
        
        // Normalizar parámetros de entrada (responsabilidad de aplicación)
        var parametrosPaginacion = normalizarParametrosPaginacion(solicitud);
        System.out.println("[FormularioService.listar] ✅ Parámetros normalizados:");
        System.out.println("  - Page index: " + parametrosPaginacion.page().index);
        System.out.println("  - Page size: " + parametrosPaginacion.page().size);
        System.out.println("  - Sort: " + parametrosPaginacion.sort());

        // Delegación al repositorio (responsabilidad: acceso a datos)
        System.out.println("[FormularioService.listar] 🚀 Llamando al repositorio...");
        Uni<PaginadoResponse<Formulario>> resultadoUni = repository.listar(
            parametrosPaginacion.page(),
            parametrosPaginacion.sort(),
            solicitud.identificacionUsuario, 
            solicitud.buscar);

        // Mapeo a DTOs de salida (responsabilidad: transformación a transfer objects)
        return resultadoUni.map(resultado -> {
            System.out.println("[FormularioService.listar] 📊 Resultado del repositorio:");
            System.out.println("  - Total elementos: " + resultado.totalElementos);
            System.out.println("  - Total páginas: " + resultado.totalPaginas);
            System.out.println("  - Página actual: " + resultado.paginaActual);
            System.out.println("  - Tamaño: " + resultado.tamanio);
            System.out.println("  - Registros en contenido: " + (resultado.contenido != null ? resultado.contenido.size() : "null"));
            
            List<FormularioResponse> contenidoFormularios = mapper.toResponseList(resultado.contenido);
            System.out.println("  - Registros mapeados a Response: " + (contenidoFormularios != null ? contenidoFormularios.size() : "null"));

            var respuesta = new PaginadoResponse<>(
                contenidoFormularios,
                resultado.totalElementos,
                resultado.totalPaginas,
                resultado.paginaActual,
                resultado.tamanio);
            
            System.out.println("[FormularioService.listar] ✅ FIN - Respuesta enviada");
            return respuesta;
        });
    }

    /**
     * Normaliza parámetros de paginación desde la solicitud del cliente
     * Responsabilidad de capa de aplicación: validar y transformar inputs
     * 
     * @param solicitud Solicitud del cliente con parámetros potencialmente inválidos o nulos
     * @return Record con Page y Sort normalizados
     */
    private ParametrosPaginacion normalizarParametrosPaginacion(ConsultarFormulariosRequest solicitud) {
        Page page = PaginacionUtil.crearPage(solicitud.pagina, solicitud.limite);
        Sort sort = PaginacionUtil.crearOrdenamiento(solicitud.ordenarPor, solicitud.orden);
        return new ParametrosPaginacion(page, sort);
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

    /**
     * Record para encapsular parámetros normalizados de paginación
     * Responsabilidad: transportar parámetros validados a través de las capas
     */
    private record ParametrosPaginacion(Page page, Sort sort) {
    }
}
