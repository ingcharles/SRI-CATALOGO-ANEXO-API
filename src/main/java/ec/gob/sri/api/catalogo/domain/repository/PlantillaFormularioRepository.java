package ec.gob.sri.api.catalogo.domain.repository;

import ec.gob.sri.api.catalogo.domain.model.entity.PlantillaFormulario;
import io.smallrye.mutiny.Uni;

import java.util.List;

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
     * Busca una plantilla por ID
     */
    Uni<PlantillaFormulario> buscarPorId(Long codigoPlantillaFormulario);

    /**
     * Busca una plantilla por código y versión
     */
    Uni<PlantillaFormulario> buscarPorCodigoYVersion(String codigo, String version);

    /**
     * Lista todas las plantillas con paginación y filtros
     */
    Uni<List<PlantillaFormulario>> listar(Integer pagina, Integer limite, String buscar, String ordenarPor,
            String orden);

    /**
     * Cuenta el total de plantillas según filtros
     */
    Uni<Long> contar(String buscar);

    /**
     * Elimina lógicamente una plantilla
     */
    Uni<Boolean> eliminar(Long codigoPlantillaFormulario);
}
