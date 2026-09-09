package ec.gob.sri.api.catalogo.domain.repository;

import ec.gob.sri.api.catalogo.application.dto.client.GrupoPorIntegranteDTO;

import java.util.List;

/**
 * Puerto para el cliente de Grupo por Integrante
 * Define el contrato para consumir servicios externos de gestión de grupos
 */
public interface GrupoPorIntegranteClient {

    /**
     * Obtiene la información de grupos por código de usuario
     * 
     * @param codigoUsuario Código del usuario a consultar
     * @return Lista de DTOs con la información de los grupos por integrante
     */
    List<GrupoPorIntegranteDTO> obtenerGruposPorCodigoUsuario(String codigoUsuario);
}
