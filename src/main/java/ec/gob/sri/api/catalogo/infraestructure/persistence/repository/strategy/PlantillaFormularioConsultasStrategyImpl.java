package ec.gob.sri.api.catalogo.infraestructure.persistence.repository.strategy;

import ec.gob.sri.api.catalogo.domain.repository.strategy.PlantillaFormularioConsultasStrategy;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

/**
 * Estrategia que selecciona solo campos necesarios Aplica SRP: Una sola responsabilidad - construir
 * consultas optimizadas
 */
@ApplicationScoped
public class PlantillaFormularioConsultasStrategyImpl implements
    PlantillaFormularioConsultasStrategy {

    // Campos a seleccionar (mantenibles en un solo lugar)
    private static final String CAMPOS_SELECCION = """
        CODIGO_PLANTILLA_FORMULARIO,
        CODIGO,
        NOMBRE,
        DESCRIPCION,
        VERSION,
        ELEMENTOS_JSON,
        ELEMENTOS_XML,
        ESTADO,
        MOTIVO,
        FECHA_CREACION,
        FECHA_ACTUALIZACION
        """;

    @Override
    public String construirConsultaListado(String codigo, String buscar, Sort ordenamiento) {
        StringBuilder consulta = new StringBuilder("SELECT ").append(CAMPOS_SELECCION)
            .append(" FROM PLANTILLA_FORMULARIO WHERE ELIMINADO = 'N'");

        if (codigo != null && !codigo.isEmpty()) {
            consulta.append(construirFiltroCodigo());
        }

        if (buscar != null && !buscar.isEmpty()) {
            consulta.append(construirFiltrosJson());
        }

        consulta.append(construirOrdenamiento(ordenamiento));

        return consulta.toString();
    }

    @Override
    public String construirConsultaConteo(String codigo, String buscar) {
        StringBuilder consulta = new StringBuilder(
            "SELECT COUNT(*) FROM PLANTILLA_FORMULARIO WHERE ELIMINADO = 'N'");

        if (codigo != null && !codigo.isEmpty()) {
            consulta.append(construirFiltroCodigo());
        }

        if (buscar != null && !buscar.isEmpty()) {
            consulta.append(construirFiltrosJson());
        }

        return consulta.toString();
    }

    @Override
    public Map<String, Object> construirParametros(String codigo, String buscar) {
        Map<String, Object> parametros = new HashMap<>();

        if (codigo != null && !codigo.isEmpty()) {
            parametros.put("buscarCodigo", codigo);
        }

        if (buscar != null && !buscar.isEmpty()) {
            String buscarLike = "%" + buscar.toLowerCase() + "%";
            parametros.put("buscar", buscarLike);
            parametros.put("buscarJson", buscar);
        }

        return parametros;
    }

    private String construirFiltroCodigo() {
        return """
             AND (
               LOWER(CODIGO) LIKE :buscarCodigo
             )
            """;
    }

    private String construirFiltrosJson() {
        return """
             AND (
               LOWER(CODIGO) LIKE :buscar
               OR LOWER(NOMBRE) LIKE :buscar
               OR LOWER(DESCRIPCION) LIKE :buscar
               OR LOWER(VERSION) LIKE :buscar
               OR JSON_EXISTS(ELEMENTOS_JSON, '$?(@.id == $buscarJson)' PASSING :buscarJson AS "buscarJson")
               OR JSON_EXISTS(ELEMENTOS_JSON, '$?(@.nombre == $buscarJson)' PASSING :buscarJson AS "buscarJson")
             )
            """;
    }

    private String construirOrdenamiento(Sort ordenamiento) {
        if (ordenamiento == null || ordenamiento.getColumns().isEmpty()) {
            return " ORDER BY FECHA_CREACION DESC";
        }

        Sort.Column columna = ordenamiento.getColumns().get(0);
        String nombreColumna = mapearCampoAColumna(columna.getName());
        String direccion = columna.getDirection() == Sort.Direction.Ascending ? "ASC" : "DESC";
        return " ORDER BY " + nombreColumna + " " + direccion;
    }

    /**
     * Mapea nombres de campos Java a nombres de columnas SQL
     */
    private String mapearCampoAColumna(String campo) {
        return switch (campo) {
            case "fechaCreacion" -> "FECHA_CREACION";
            case "fechaActualizacion" -> "FECHA_ACTUALIZACION";
            case "codigo" -> "CODIGO";
            case "nombre" -> "NOMBRE";
            case "version" -> "VERSION";
            default -> "FECHA_CREACION";
        };
    }
}
