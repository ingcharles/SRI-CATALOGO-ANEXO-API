package ec.gob.sri.api.catalogo.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.sri.api.catalogo.application.dto.*;
import ec.gob.sri.api.catalogo.domain.model.entity.PlantillaFormulario;
import ec.gob.sri.api.catalogo.domain.repository.PlantillaFormularioRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PlantillaFormularioMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Servicio de aplicación para Plantilla Formulario
 */
@ApplicationScoped
public class PlantillaFormularioService {

    @Inject
    PlantillaFormularioRepository repository;

    @Inject
    PlantillaFormularioMapper mapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
                    // Convertir paginas Object a JSON String
                    plantilla.setPaginas(convertirPaginasAJson(request.paginas));

                    return repository.guardar(plantilla)
                            .map(mapper::toResponse);
                });
    }

    /**
     * Actualiza un formulario existente
     */
    public Uni<GuardarFormularioResponse> actualizar(ActualizarFormularioRequest request) {
        return repository.buscarPorId(request.codigoPlantillaFormulario)
                .onItem().transformToUni(existente -> {
                    if (existente == null) {
                        return Uni.createFrom().failure(
                                new IllegalArgumentException(
                                        "No se encontró el formulario con ID: " + request.codigoPlantillaFormulario));
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
        if (request.paginas != null) {
            existente.setPaginas(convertirPaginasAJson(request.paginas));
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
        if (request.pagina == null || request.pagina < 1) {
            request.pagina = 1;
        }
        if (request.limite == null || request.limite < 1) {
            request.limite = 10;
        }
        if (request.ordenarPor == null) {
            request.ordenarPor = "fechaCreacion";
        }
        if (request.orden == null) {
            request.orden = "desc";
        }

        Uni<List<PlantillaFormulario>> listaUni = repository.listar(
                request.pagina,
                request.limite,
                request.buscar,
                request.ordenarPor,
                request.orden);

        Uni<Long> totalUni = repository.contar(request.buscar);

        return Uni.combine().all()
                .unis(listaUni, totalUni)
                .asTuple()
                .map(tuple -> {
                    ListarFormulariosResponse response = new ListarFormulariosResponse();
                    response.formularios = mapper.toResponseList(tuple.getItem1());
                    response.total = tuple.getItem2();
                    response.pagina = request.pagina;
                    response.limite = request.limite;
                    return response;
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
     * Convierte el objeto paginas a JSON String
     */
    private String convertirPaginasAJson(Object paginas) {
        if (paginas == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(paginas);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error al convertir páginas a JSON: " + e.getMessage(), e);
        }
    }
}
