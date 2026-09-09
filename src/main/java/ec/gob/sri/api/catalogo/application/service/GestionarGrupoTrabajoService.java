package ec.gob.sri.api.catalogo.application.service;

import ec.gob.sri.api.catalogo.application.dto.client.GrupoPorIntegranteDTO;
import ec.gob.sri.api.catalogo.domain.repository.GrupoPorIntegranteClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Servicio de aplicación para gestionar operaciones relacionadas con grupos de trabajo
 */
@ApplicationScoped
public class GestionarGrupoTrabajoService {

    private static final Logger logger = LoggerFactory.getLogger(GestionarGrupoTrabajoService.class);

    @Inject
    GrupoPorIntegranteClient grupoPorIntegranteClient;

    /**
     * Obtiene la información de grupos por código de usuario
     * 
     * @param codigoUsuario Código del usuario a consultar
     * @return Lista de DTOs con la información de los grupos
     */
    public List<GrupoPorIntegranteDTO> obtenerGruposPorUsuario(String codigoUsuario) {
        logger.info("Iniciando consulta de grupos para el usuario: {}", codigoUsuario);
        
        if (codigoUsuario == null || codigoUsuario.trim().isEmpty()) {
            logger.warn("El código de usuario no puede ser nulo o vacío");
            throw new IllegalArgumentException("El código de usuario es requerido");
        }

        List<GrupoPorIntegranteDTO> grupos = grupoPorIntegranteClient.obtenerGruposPorCodigoUsuario(codigoUsuario);
        
        if (grupos != null && !grupos.isEmpty()) {
            logger.info("Se encontraron {} grupos para el usuario: {}", 
                grupos.size(), codigoUsuario);
        } else {
            logger.warn("No se encontraron grupos para el usuario: {}", codigoUsuario);
        }

        return grupos;
    }
}
