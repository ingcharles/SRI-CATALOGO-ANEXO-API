package ec.gob.sri.api.catalogo.infraestructure.persistence.dto;

import java.time.LocalDateTime;

/**
 * DTO tipo Record para proyección de listados de PlantillaFormulario.
 * 
 * Características:
 * - Inmutable: Todos los campos son final
 * - Auto-generado: Constructor, getters, equals, hashCode, toString
 * - Optimizado: Mejor que class con boilerplate
 * 
 * Campos incluyen:
 * - Datos básicos: código, nombre, descripción, versión
 * - Datos complejos: elementosJson (CLOB), elementosXml (CLOB)
 * - Metadatos: estado, motivo, fechas de auditoría
 * 
 * Patrón: DTO inmutable para mapeo Object[] → DTO en queries nativas
 */
public record PlantillaFormularioListaDTO(
    Long codigoPlantillaFormulario,
    String codigo,
    String nombre,
    String descripcion,
    String version,
    String elementosJson,
    String elementosXml,
    String estado,
    String motivo,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion
) { }

