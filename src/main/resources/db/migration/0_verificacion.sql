-- ============================================
-- Script de verificación y limpieza
-- Ejecuta SOLO si necesitas recrear la tabla
-- ============================================

-- Verificar si la secuencia existe
select sequence_name
  from user_sequences
 where sequence_name = 'SEQ_PLANTILLA_FORMULARIO';

-- Verificar si la tabla existe
select table_name
  from user_tables
 where table_name = 'PLANTILLA_FORMULARIO';

-- Si necesitas eliminar y recrear (ESTO BORRA TODOS LOS DATOS):
-- DROP TABLE PLANTILLA_FORMULARIO CASCADE CONSTRAINTS;
-- DROP SEQUENCE SEQ_PLANTILLA_FORMULARIO;

-- ============================================
-- Continúa con el script principal
-- ============================================