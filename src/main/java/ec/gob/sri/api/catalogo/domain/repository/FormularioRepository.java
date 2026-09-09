package ec.gob.sri.api.catalogo.domain.repository;

import ec.gob.sri.api.catalogo.application.dto.PaginadoResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.Formulario;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

/**
 * Repositorio del dominio para Formulario
 */
public interface FormularioRepository {

    /**
     * Guarda un nuevo formulario
     */
    Uni<Formulario> guardar(Formulario formulario);

    /**
     * Actualiza un formulario existente
     */
    Uni<Formulario> actualizar(Formulario formulario);

    /**
     * Busca un formulario por ID
     */
    Uni<Formulario> buscarPorId(Long codigoFormulario);

    /**
     * Lista todos los formularios con paginación y filtros usando Panache
     */
    Uni<PaginadoResponse<Formulario>> listar(Page page, Sort sort,
        String identificacionUsuario, String buscar);

    /**
     * Elimina lógicamente un formulario
     */
    Uni<Boolean> eliminar(Long codigoFormulario);
}
