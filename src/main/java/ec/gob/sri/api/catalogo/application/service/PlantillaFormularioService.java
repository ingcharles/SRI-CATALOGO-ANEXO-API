package ec.gob.sri.api.catalogo.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.sri.api.catalogo.application.dto.*;
import ec.gob.sri.api.catalogo.domain.model.entity.PlantillaFormulario;
import ec.gob.sri.api.catalogo.domain.model.enums.EstadoPlantilla;
import ec.gob.sri.api.catalogo.domain.repository.PlantillaFormularioRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PlantillaFormularioMapper;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Servicio de aplicación para Plantilla Formulario
 */
@ApplicationScoped
public class PlantillaFormularioService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Inject
    PlantillaFormularioRepository repository;
    @Inject
    PlantillaFormularioMapper mapper;

    /**
     * Guarda un nuevo formulario
     */
    public Uni<GuardarFormularioResponse> guardar(GuardarFormularioRequest request) {
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
                    plantilla.setElementos(convertirElementosAJson(request.elementos));

                    return repository.guardar(plantilla)
                            .map(mapper::toResponse);
                });
    }

    /**
     * Actualiza un formulario existente
     */
    public Uni<GuardarFormularioResponse> actualizar(ActualizarFormularioRequest request, Long codigoPlantillaFormulario) {
        return repository.buscarPorId(codigoPlantillaFormulario)
                .onItem().transformToUni(existente -> {
                    if (existente == null) {
                        return Uni.createFrom().failure(
                                new IllegalArgumentException(
                                        "No se encontró el formulario con ID: " + codigoPlantillaFormulario));
                    }

                    // Validar que el código y versión no existan en otro registro
                    boolean codigoCambiado = request.codigo != null && !request.codigo.equals(existente.getCodigo());
                    boolean versionCambiada = request.version != null
                            && !request.version.equals(existente.getVersion());

                    if (codigoCambiado || versionCambiada) {
                        String codigoABuscar = request.codigo != null ? request.codigo : existente.getCodigo();
                        String versionABuscar = request.version != null ? request.version : existente.getVersion();

                        return repository.buscarPorCodigoYVersion(codigoABuscar, versionABuscar)
                                .onItem().transformToUni(otro -> {
                                    if (otro != null && !otro.getCodigoPlantillaFormulario()
                                            .equals(existente.getCodigoPlantillaFormulario())) {
                                        return Uni.createFrom().failure(
                                                new IllegalArgumentException(
                                                        "Ya existe otro formulario con el código: " + codigoABuscar
                                                                + " y versión: " + versionABuscar));
                                    }
                                    return actualizarPlantilla(request, existente);
                                });
                    }

                    return actualizarPlantilla(request, existente);
                });
    }

    private Uni<GuardarFormularioResponse> actualizarPlantilla(ActualizarFormularioRequest request,
                                                               PlantillaFormulario existente) {
        if (request.codigo != null)
            existente.setCodigo(request.codigo);
        if (request.nombre != null)
            existente.setNombre(request.nombre);
        if (request.descripcion != null)
            existente.setDescripcion(request.descripcion);
        if (request.version != null)
            existente.setVersion(request.version);
        if (request.estado != null) {
            existente.setEstado(validarYConvertirEstado(request.estado));
        }
        if (request.elementos != null) {
            existente.setElementos(convertirElementosAJson(request.elementos));
        }

        return repository.actualizar(existente)
                .map(mapper::toResponse);
    }

    /**
     * Busca un formulario por ID
     */
    public Uni<GuardarFormularioResponse> buscarPorId(Long codigoPlantillaFormulario) {
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
    public Uni<ListarFormulariosResponse> listar(ListarFormulariosRequest request) {
        // Validar y establecer valores por defecto (Panache usa índice 0 para la
        // primera página)
        int paginaIndex = (request.pagina == null || request.pagina < 1) ? 0 : request.pagina - 1;
        int tamanio = (request.limite == null || request.limite < 1) ? 10 : request.limite;
        String ordenarPor = (request.ordenarPor == null || request.ordenarPor.isEmpty()) ? "fechaCreacion"
                : request.ordenarPor;
        String orden = (request.orden == null || request.orden.isEmpty()) ? "desc" : request.orden;

        // Crear objetos Page y Sort de Panache
        Page page = Page.of(paginaIndex, tamanio);
        Sort sort = crearOrdenamiento(ordenarPor, orden);

        Uni<List<PlantillaFormulario>> listaUni = repository.listar(page, sort, request.buscar);
        Uni<Long> totalUni = repository.contar(request.buscar);

        return Uni.combine().all()
                .unis(listaUni, totalUni)
                .asTuple()
                .map(tuple -> {
                    Long total = tuple.getItem2();
                    int totalPaginas = (int) Math.ceil((double) total / tamanio);

                    ListarFormulariosResponse response = new ListarFormulariosResponse();
                    response.formularios = mapper.toResponseList(tuple.getItem1());
                    response.total = total;
                    response.totalPaginas = totalPaginas;
                    response.pagina = paginaIndex + 1; // Devolver página en base 1 para el usuario
                    response.tamanio = tamanio;
                    response.esPrimera = (paginaIndex == 0);
                    response.esUltima = (paginaIndex >= totalPaginas - 1);
                    return response;
                });
    }

    /**
     * Crea el ordenamiento para las consultas
     */
    private Sort crearOrdenamiento(String ordenarPor, String orden) {
        String campo = switch (ordenarPor) {
            case "codigo" -> "codigo";
            case "nombre" -> "nombre";
            case "fechaActualizacion" -> "fechaActualizacion";
            default -> "fechaCreacion";
        };

        return "asc".equalsIgnoreCase(orden) ? Sort.by(campo).ascending() : Sort.by(campo).descending();
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
            return EstadoPlantilla.A; // Por defecto Activo
        }
        try {
            return EstadoPlantilla.fromCodigo(estadoCodigo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + estadoCodigo
                    + ". Los valores permitidos son: A (Activo), I (Inactivo)");
        }
    }
}
