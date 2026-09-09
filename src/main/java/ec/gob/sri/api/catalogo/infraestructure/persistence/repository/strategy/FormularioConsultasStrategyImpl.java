package ec.gob.sri.api.catalogo.infraestructure.persistence.repository.strategy;

import ec.gob.sri.api.catalogo.domain.repository.strategy.FormularioConsultasStrategy;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

/**
 * Estrategia que construye consultas JPQL optimizadas para Formulario.
 * Aplica SRP: Una sola responsabilidad - construir consultas optimizadas
 * 
 * Características:
 * - Usa JPQL para trabajar con entidades
 * - JOIN FETCH para cargar relaciones eficientemente
 * - Filtros opcionales: identificacionUsuario, búsqueda global
 * - NO incluye ORDER BY en consulta listado (Panache lo agrega automáticamente)
 */
@ApplicationScoped
public class FormularioConsultasStrategyImpl implements FormularioConsultasStrategy {

    @Override
    public String construirConsultaListado(String identificacionUsuario, String buscar) {
        System.out.println("[FormularioConsultasStrategy.construirConsultaListado] 🔍 Construyendo consulta listado:");
        System.out.println("  - identificacionUsuario: " + identificacionUsuario);
        System.out.println("  - buscar: " + buscar);
        
        StringBuilder consulta = new StringBuilder("SELECT f FROM FormularioEntity f ")
            .append("LEFT JOIN FETCH f.plantillaFormulario p ")
            .append("WHERE f.eliminado = 'N'");

        // Filtro por identificación de usuario
        if (identificacionUsuario != null && !identificacionUsuario.isEmpty()) {
            consulta.append(" AND LOWER(f.identificacionUsuario) LIKE :identificacionUsuario");
            System.out.println("  - Agregado filtro identificacionUsuario");
        }

        // Búsqueda global en múltiples campos
        if (buscar != null && !buscar.isEmpty()) {
            consulta.append(construirFiltrosBusquedaGlobal());
            System.out.println("  - Agregado filtro búsqueda global");
        }

        // ⚠️ NO agregar ORDER BY aquí - Panache lo maneja automáticamente con el parámetro Sort
        // consulta.append(construirOrdenamiento(ordenamiento));
        
        String consultaFinal = consulta.toString();
        System.out.println("[FormularioConsultasStrategy] ✅ Consulta listado (sin ORDER BY - lo agrega Panache): " + consultaFinal);
        return consultaFinal;
    }

    @Override
    public String construirConsultaConteo(String identificacionUsuario, String buscar) {
        System.out.println("[FormularioConsultasStrategy.construirConsultaConteo] 🔍 Construyendo consulta conteo:");
        
        StringBuilder consulta = new StringBuilder("SELECT COUNT(f) FROM FormularioEntity f");

        // Solo hacer JOIN si hay búsqueda en campos de plantilla
        boolean necesitaJoin = buscar != null && !buscar.isEmpty();
        if (necesitaJoin) {
            consulta.append(" LEFT JOIN f.plantillaFormulario p");
            System.out.println("  - Agregado LEFT JOIN con plantilla");
        }

        consulta.append(" WHERE f.eliminado = 'N'");

        // Filtro por identificación de usuario
        if (identificacionUsuario != null && !identificacionUsuario.isEmpty()) {
            consulta.append(" AND LOWER(f.identificacionUsuario) LIKE :identificacionUsuario");
            System.out.println("  - Agregado filtro identificacionUsuario");
        }

        // Búsqueda global en múltiples campos
        if (buscar != null && !buscar.isEmpty()) {
            consulta.append(construirFiltrosBusquedaGlobal());
            System.out.println("  - Agregado filtro búsqueda global");
        }

        String consultaFinal = consulta.toString();
        System.out.println("[FormularioConsultasStrategy] ✅ Consulta conteo: " + consultaFinal);
        return consultaFinal;
    }

    @Override
    public Map<String, Object> construirParametros(String identificacionUsuario, String buscar) {
        System.out.println("[FormularioConsultasStrategy.construirParametros] 🔍 Construyendo parámetros:");
        Map<String, Object> parametros = new HashMap<>();

        if (identificacionUsuario != null && !identificacionUsuario.isEmpty()) {
            String valor = "%" + identificacionUsuario.toLowerCase() + "%";
            parametros.put("identificacionUsuario", valor);
            System.out.println("  - identificacionUsuario: " + valor);
        }

        if (buscar != null && !buscar.isEmpty()) {
            String buscarLike = "%" + buscar.toLowerCase() + "%";
            parametros.put("buscar", buscarLike);
            System.out.println("  - buscar: " + buscarLike);
        }

        System.out.println("[FormularioConsultasStrategy] ✅ Parámetros: " + parametros);
        return parametros;
    }

    /**
     * Construye filtros de búsqueda global en JPQL.
     * Busca en: codigoFormulario, codigoUsuario, identificacionUsuario (formulario)
     * y nombre, versión, descripción (plantilla)
     */
    private String construirFiltrosBusquedaGlobal() {
        return """
             AND (
               LOWER(CAST(f.codigoFormulario AS string)) LIKE :buscar
               OR LOWER(f.codigoUsuario) LIKE :buscar
               OR LOWER(f.identificacionUsuario) LIKE :buscar
               OR LOWER(p.nombre) LIKE :buscar
               OR LOWER(p.version) LIKE :buscar
               OR LOWER(p.descripcion) LIKE :buscar
             )
            """;
    }
}
