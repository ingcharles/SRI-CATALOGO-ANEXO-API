// ============================================================================
// 4. EJECUTOR DE CONSULTAS - Template Method Pattern
// ============================================================================

package ec.gob.sri.api.catalogo.infraestructure.persistence.repository.executor;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;

/**
 * Ejecutor de consultas nativas reactivas. Aplica DIP: Depende de abstracciones, no de
 * implementaciones concretas
 */
@ApplicationScoped
public class ConsultasReactivasExecutor {

    /**
     * Ejecuta consulta de listado con paginación.
     * Devuelve resultados crudos como Object[] sin mapeo automático.
     * Útil cuando necesitas mapeo manual a DTO personalizado.
     *
     * @param consulta   sentencia SQL nativa
     * @param parametros parámetros nombrados para la consulta
     * @param pagina     página con índice y tamaño
     * @return Uni con lista de Object[] crudos
     */
    public Uni<List<Object[]>> ejecutarConsultaListadoComoObject(
        String consulta,
        Map<String, Object> parametros,
        Page pagina) {
        return Panache.getSession()
            .chain(sesion -> {
                var consulQuery = sesion.createNativeQuery(consulta, Object[].class);
                parametros.forEach(consulQuery::setParameter);
                consulQuery.setFirstResult(pagina.index * pagina.size);
                consulQuery.setMaxResults(pagina.size);
                return consulQuery.getResultList();
            });
    }

    /**
     * Ejecuta consulta de listado con paginación. Devuelve resultados crudos (Object[]) sin
     * intentar mapeo automático de Hibernate
     *
     * @param consulta       sentencia SQL nativa
     * @param parametros     parámetros nombrados para la consulta
     * @param claseResultado clase que mapea los resultados
     * @param pagina         página con índice y tamaño
     * @param <T>            tipo genérico de resultado
     * @return Uni con lista de resultados
     */
    public <T> Uni<List<T>> ejecutarConsultaListado(
        String consulta,
        Map<String, Object> parametros,
        Class<T> claseResultado,
        Page pagina) {
        return Panache.getSession()
            .chain(sesion -> {
                var consulQuery = sesion.createNativeQuery(consulta, claseResultado);
                parametros.forEach(consulQuery::setParameter);
                consulQuery.setFirstResult(pagina.index * pagina.size);
                consulQuery.setMaxResults(pagina.size);
                return consulQuery.getResultList();
            });
    }

    /**
     * Ejecuta consulta de conteo.
     *
     * @param consulta   sentencia SQL nativa con COUNT(*)
     * @param parametros parámetros nombrados para la consulta
     * @return Uni con el total de registros
     */
    public Uni<Long> ejecutarConsultaConteo(String consulta, Map<String, Object> parametros) {
        return Panache.getSession()
            .chain(sesion -> {
                var consulQuery = sesion.createNativeQuery(consulta, Long.class);
                parametros.forEach(consulQuery::setParameter);
                return consulQuery.getSingleResult();
            });
    }
}
