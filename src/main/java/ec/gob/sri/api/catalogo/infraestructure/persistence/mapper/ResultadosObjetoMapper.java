package ec.gob.sri.api.catalogo.infraestructure.persistence.mapper;

import java.time.LocalDateTime;

/**
 * Mapper para convertir resultados JDBC/SQL a tipos Java.
 * <p>
 * Responsabilidades: - Convertir tipos genéricos: Object → Long, String, LocalDateTime - Manejar
 * CLOBs (oracle.sql.CLOB y java.sql.Clob) - Gestionar null-safety en conversiones
 * <p>
 * Patrón: Utility class con métodos estáticos reutilizables Similar a: PaginacionUtil.java
 * <p>
 * Principio SOLID: SRP - Responsabilidad única en conversión de tipos
 *
 * @author SRI - Refactoring SOLID
 */
public class ResultadosObjetoMapper {

    /**
     * Constructor privado para evitar instanciación
     */
    private ResultadosObjetoMapper() {
        // Utility class, do not instantiate
    }

    /**
     * Convierte Object a Long manteniendo null y BigDecimal.
     * <p>
     * Casos soportados: - null → null - Long → Long (directo) - BigDecimal → longValue() - Integer
     * → longValue()
     * <p>
     * Usado en: Conversión de columnas numéricas desde Object[] de queries nativas
     *
     * @param valor objeto a convertir
     * @return Long o null si valor es null
     * @throws IllegalArgumentException si el tipo no es convertible
     */
    public static Long aLong(Object valor) {
        if (valor == null) {
            return null;
        }
        if (valor instanceof Long l) {
            return l;
        }
        if (valor instanceof java.math.BigDecimal bd) {
            return bd.longValue();
        }
        if (valor instanceof Integer i) {
            return i.longValue();
        }
        throw new IllegalArgumentException(
            "No se puede convertir a Long: " + valor.getClass().getSimpleName());
    }

    /**
     * Convierte Object a String manteniendo null.
     * <p>
     * Casos soportados: - null → null - String → String (directo) - Otros → toString()
     * <p>
     * Usado en: Conversión de columnas VARCHAR, CHAR, etc.
     *
     * @param valor objeto a convertir
     * @return String o null si valor es null
     */
    public static String aString(Object valor) {
        if (valor == null) {
            return null;
        }
        return valor.toString();
    }

    /**
     * Convierte Object a String, manejando especialmente CLOB.
     * <p>
     * Los CLOBs pueden llegar de múltiples formas según el driver JDBC y versión Oracle:
     * <p>
     * 1. java.sql.Clob (estándar JDBC) - Usar: clob.length() y clob.getSubString()
     * <p>
     * 2. oracle.sql.CLOB (Oracle 12c+) - Requiere reflexión para acceder a getSubString() - Driver
     * JDBC de Oracle proporciona esta clase
     * <p>
     * 3. String (ya convertido) - Algunos drivers convierten CLOB a String automáticamente -
     * Retornar directamente
     * <p>
     * 4. null (columna vacía o NULL en BD) - Retornar null
     * <p>
     * Usado en: Conversión de columnas CLOB (ELEMENTOS_JSON, ELEMENTOS_XML)
     *
     * @param valor objeto CLOB o String
     * @return String con contenido del CLOB, null si valor es null o vacío
     */
    public static String aClob(Object valor) {
        if (valor == null) {
            return null;
        }

        // CASO 1: Ya es String (el driver JDBC lo convirtió automáticamente)
        if (valor instanceof String s) {
            return s;
        }

        // CASO 2: CLOB específico de Oracle (oracle.sql.CLOB)
        if (valor.getClass().getName().equals("oracle.sql.CLOB")) {
            try {
                Object clobObj = valor;

                // Obtener longitud usando reflexión
                java.lang.reflect.Method obtenerLongitudMetodo =
                    clobObj.getClass().getMethod("length");
                long longitud = (Long) obtenerLongitudMetodo.invoke(clobObj);

                if (longitud == 0) {
                    return null;
                }

                // Obtener substring usando reflexión
                java.lang.reflect.Method obtenerSubstringMetodo =
                    clobObj.getClass().getMethod("getSubString", long.class, int.class);
                return (String) obtenerSubstringMetodo.invoke(clobObj, 1, (int) longitud);

            } catch (Exception e) {
                // Si hay error al leer el CLOB Oracle, retornar null (fallback seguro)
                return null;
            }
        }

        // CASO 3: CLOB estándar de JDBC (java.sql.Clob)
        if (valor instanceof java.sql.Clob clob) {
            try {
                long longitud = clob.length();
                if (longitud == 0) {
                    return null;
                }
                return clob.getSubString(1, (int) longitud);

            } catch (java.sql.SQLException e) {
                // Si hay error al leer el CLOB JDBC, retornar null (fallback seguro)
                return null;
            }
        }

        // CASO 4: Fallback - Intentar conversión genérica a String
        try {
            return valor.toString();
        } catch (Exception e) {
            // Si falla todo, retornar null
            return null;
        }
    }

    /**
     * Convierte Object a LocalDateTime.
     * <p>
     * Casos soportados: - null → null - LocalDateTime → LocalDateTime (directo) - java.util.Date →
     * LocalDateTime (conversión con zona horaria del sistema)
     * <p>
     * Usado en: Conversión de columnas DATE/TIMESTAMP
     *
     * @param valor objeto a convertir
     * @return LocalDateTime o null si valor es null
     * @throws IllegalArgumentException si el tipo no es convertible
     */
    public static LocalDateTime aLocalDateTime(Object valor) {
        if (valor == null) {
            return null;
        }
        if (valor instanceof LocalDateTime ldt) {
            return ldt;
        }
        if (valor instanceof java.util.Date fecha) {
            return fecha.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
        }
        throw new IllegalArgumentException(
            "No se puede convertir a LocalDateTime: " + valor.getClass().getSimpleName());
    }
}
