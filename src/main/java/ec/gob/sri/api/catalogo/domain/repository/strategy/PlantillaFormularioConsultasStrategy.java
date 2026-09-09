// ============================================================================
// 2. ESTRATEGIA DE CONSTRUCTOR CONSULTAS - Strategy Pattern
// ============================================================================

package ec.gob.sri.api.catalogo.domain.repository.strategy;

import io.quarkus.panache.common.Sort;
import java.util.Map;

/**
 * Estrategia para construcción de consultas SQL nativas
 */
public interface PlantillaFormularioConsultasStrategy {

    String construirConsultaListado(String codigo, String buscar, Sort ordenamiento);

    String construirConsultaConteo(String codigo, String buscar);

    Map<String, Object> construirParametros(String codigo, String buscar);
}
