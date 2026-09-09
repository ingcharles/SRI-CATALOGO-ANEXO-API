package ec.gob.sri.api.catalogo.infraestructure.client.impl;

import ec.gob.sri.api.catalogo.application.dto.client.GrupoPorIntegranteDTO;
import ec.gob.sri.api.catalogo.domain.repository.GrupoPorIntegranteClient;
import ec.gob.sri.api.catalogo.infraestructure.client.GrupoPorIntegranteRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementación del cliente para consumir servicios de Grupo por Integrante
 */
@ApplicationScoped
public class GrupoPorIntegranteClientImpl implements GrupoPorIntegranteClient {

    private static final Logger logger = LoggerFactory.getLogger(GrupoPorIntegranteClientImpl.class);

    @Inject
    @RestClient
    GrupoPorIntegranteRestClient grupoPorIntegranteRestClient;

    @Override
    public List<GrupoPorIntegranteDTO> obtenerGruposPorCodigoUsuario(String codigoUsuario) {
        logger.info("Consultando grupos para el usuario: {}", codigoUsuario);
        
        try {
            List<GrupoPorIntegranteDTO> resultado = grupoPorIntegranteRestClient
                .listarGruposPorCodigoUsuario(codigoUsuario);
            
            logger.info("Grupos obtenidos exitosamente. Total: {}", 
                resultado != null ? resultado.size() : 0);
            
            return resultado;
        } catch (Exception e) {
            logger.error("Error al consultar grupos para el usuario: {}", codigoUsuario, e);
            throw e;
        }
    }
}
