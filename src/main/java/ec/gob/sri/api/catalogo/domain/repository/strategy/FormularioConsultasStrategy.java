package ec.gob.sri.api.catalogo.domain.repository.strategy;

import java.util.Map;

/**
 * Estrategia para construir consultas optimizadas de Formulario.
 * 
 * Responsabilidades:
 * - Construir consulta de listado con filtros opcionales (sin ORDER BY)
 * - Construir consulta de conteo con filtros opcionales
 * - Preparar parámetros nombrados para las consultas
 * 
 * Nota: El ORDER BY lo maneja Panache automáticamente mediante el parámetro Sort
 * 
 * Patrón: Strategy Pattern - Encapsula la lógica de construcción de consultas
 * Principio SOLID: SRP - Una sola responsabilidad: construir consultas
 */
public interface FormularioConsultasStrategy {

    /**
     * Construye la consulta JPQL para listar formularios (sin ORDER BY).
     * El ordenamiento lo maneja Panache con el parámetro Sort.
     *
     * @param identificacionUsuario filtro por identificación de usuario (opcional)
     * @param buscar               búsqueda en múltiples campos (opcional)
     * @return consulta JPQL sin cláusula ORDER BY
     */
    String construirConsultaListado(String identificacionUsuario, String buscar);

    /**
     * Construye la consulta JPQL para contar formularios.
     *
     * @param identificacionUsuario filtro por identificación de usuario (opcional)
     * @param buscar               búsqueda en múltiples campos (opcional)
     * @return consulta JPQL con COUNT(*)
     */
    String construirConsultaConteo(String identificacionUsuario, String buscar);

    /**
     * Construye el mapa de parámetros nombrados para las consultas.
     *
     * @param identificacionUsuario filtro por identificación de usuario (opcional)
     * @param buscar               búsqueda en múltiples campos (opcional)
     * @return mapa de parámetros
     */
    Map<String, Object> construirParametros(String identificacionUsuario, String buscar);
}
