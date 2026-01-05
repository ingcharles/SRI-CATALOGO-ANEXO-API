-- ============================================================================
-- Script de Optimización de Performance - Índices para FORMULARIO
-- Autor: Arquitectura SRI
-- Fecha: 2026-01-05
-- Descripción: Índices optimizados para consultas frecuentes en listar()
-- ============================================================================

-- Índice principal para consultas con filtro de eliminado
CREATE INDEX idx_formulario_eliminado 
ON FORMULARIO(ELIMINADO);

-- Índice para búsqueda por código de usuario
CREATE INDEX idx_formulario_codigo_usuario 
ON FORMULARIO(CODIGO_USUARIO);

-- Índice para búsqueda por identificación de usuario
CREATE INDEX idx_formulario_identificacion 
ON FORMULARIO(IDENTIFICACION_USUARIO);

-- Índice para JOIN con plantilla
CREATE INDEX idx_formulario_plantilla 
ON FORMULARIO(CODIGO_PLANTILLA_FORMULARIO);

-- Índice para ordenamiento por fecha de creación (descendente)
CREATE INDEX idx_formulario_fecha_creacion 
ON FORMULARIO(FECHA_CREACION DESC);

-- Índice compuesto para consulta más frecuente (eliminado + plantilla)
-- Mejora las queries con WHERE eliminado = 'N' AND codigo_plantilla_formulario = X
CREATE INDEX idx_formulario_eliminado_plantilla 
ON FORMULARIO(ELIMINADO, CODIGO_PLANTILLA_FORMULARIO);

-- ============================================================================
-- Índices en PLANTILLA_FORMULARIO (para búsqueda global)
-- ============================================================================

-- Índice para búsqueda por nombre
CREATE INDEX idx_plantilla_nombre 
ON PLANTILLA_FORMULARIO(NOMBRE);

-- Índice para búsqueda por versión
CREATE INDEX idx_plantilla_version 
ON PLANTILLA_FORMULARIO(VERSION);

-- Índice para filtro de eliminado
CREATE INDEX idx_plantilla_eliminado 
ON PLANTILLA_FORMULARIO(ELIMINADO);

-- Índice compuesto para búsquedas frecuentes
CREATE INDEX idx_plantilla_eliminado_estado 
ON PLANTILLA_FORMULARIO(ELIMINADO, ESTADO);

-- ============================================================================
-- Estadísticas para el optimizador de Oracle
-- ============================================================================

-- Actualizar estadísticas para que Oracle use correctamente los índices
BEGIN
    DBMS_STATS.GATHER_TABLE_STATS(
        ownname => USER,
        tabname => 'FORMULARIO',
        cascade => TRUE
    );
    
    DBMS_STATS.GATHER_TABLE_STATS(
        ownname => USER,
        tabname => 'PLANTILLA_FORMULARIO',
        cascade => TRUE
    );
END;
/

-- ============================================================================
-- Comentarios para documentación
-- ============================================================================

COMMENT ON INDEX idx_formulario_eliminado IS 'Índice para filtrado de registros activos/eliminados';
COMMENT ON INDEX idx_formulario_codigo_usuario IS 'Índice para búsqueda por código de usuario';
COMMENT ON INDEX idx_formulario_identificacion IS 'Índice para búsqueda por identificación de usuario';
COMMENT ON INDEX idx_formulario_plantilla IS 'Índice para JOIN con tabla PLANTILLA_FORMULARIO';
COMMENT ON INDEX idx_formulario_fecha_creacion IS 'Índice para ordenamiento por fecha de creación';
COMMENT ON INDEX idx_formulario_eliminado_plantilla IS 'Índice compuesto para consultas frecuentes';
