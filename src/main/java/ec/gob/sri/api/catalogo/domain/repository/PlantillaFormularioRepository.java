package ec.gob.sri.api.catalogo.domain.repository;

import ec.gob.sri.api.catalogo.application.dto.PaginadoResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.PlantillaFormulario;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

/**
 * Repositorio del dominio para Plantilla Formulario
 */
public interface PlantillaFormularioRepository {

    /**
     * Guarda una nueva plantilla
     */
    Uni<PlantillaFormulario> guardar(PlantillaFormulario plantilla);

    /**
     * Actualiza una plantilla existente
     */
    Uni<PlantillaFormulario> actualizar(PlantillaFormulario plantilla);

    /**
     * Actualiza solo el estado de una plantilla
     */
    Uni<PlantillaFormulario> actualizarEstado(Long codigoPlantillaFormulario,
        PlantillaFormulario plantilla);

    /**
     * Busca una plantilla por ID
     */
    Uni<PlantillaFormulario> buscarPorId(Long codigoPlantillaFormulario);

    /**
     * Busca una plantilla por código y versión
     */
    Uni<PlantillaFormulario> buscarPorCodigoYVersion(String codigo, String version);

    /**
     * Lista todas las plantillas con paginación y filtros Retorna ResultadoPaginado con metadatos
     * completos
     */
    Uni<PaginadoResponse<PlantillaFormulario>> listar(Page page, Sort sort, String codigo,
        String buscar);

    /**
     * Elimina lógicamente una plantilla
     */
    Uni<Boolean> eliminar(Long codigoPlantillaFormulario);
}
