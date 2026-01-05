-- Script para crear tabla FORMULARIO
-- Base de datos: Oracle

-- Crear secuencia para generar IDs automáticamente
create sequence seq_formulario start with 1 increment by 1 nocache nocycle;

create table formulario (
   codigo_formulario           number(19) not null,
   codigo_plantilla_formulario number(19) not null,
   codigo_usuario              varchar2(30),
   identificacion_usuario      varchar2(30),
   elementos                   clob not null,
   eliminado                   char(1) default 'N' not null,
   estado                      char(1) default 'A' not null,
   fecha_creacion              timestamp default current_timestamp not null,
   fecha_actualizacion         timestamp default current_timestamp not null,
   aud_usuario_crea            varchar2(30) not null,
   aud_usuario_modifica        varchar2(30),
   aud_usuario_elimina         varchar2(30),
   aud_fecha_crea              timestamp default current_timestamp,
   aud_fecha_modifica          timestamp,
   aud_fecha_elimina           timestamp,
   constraint pk_formulario primary key ( codigo_formulario ),
   constraint fk_formulario_plantilla foreign key ( codigo_plantilla_formulario )
      references plantilla_formulario ( codigo_plantilla_formulario ),
   constraint chk_formulario_eliminado check ( eliminado in ( 'S',
                                                              'N' ) ),
   constraint chk_formulario_estado check ( estado in ( 'A',
                                                        'I' ) )
);

-- Comentarios en las columnas
comment on table formulario is
   'Tabla para almacenar instancias de formularios basados en plantillas';
comment on column formulario.codigo_formulario is
   'Identificador único autogenerado del formulario';
comment on column formulario.codigo_plantilla_formulario is
   'Referencia a la plantilla utilizada para crear el formulario';
comment on column formulario.codigo_usuario is
   'Código del usuario asociado al formulario';
comment on column formulario.identificacion_usuario is
   'Identificación del usuario asociado al formulario';
comment on column formulario.elementos is
   'Estructura JSON/CLob con los elementos específicos del formulario';
comment on column formulario.eliminado is
   'Indica si el registro está eliminado (S/N)';
comment on column formulario.estado is
   'Estado del registro (A=Activo, I=Inactivo)';
comment on column formulario.fecha_creacion is
   'Fecha de creación del registro';
comment on column formulario.fecha_actualizacion is
   'Fecha de última actualización';
comment on column formulario.aud_usuario_crea is
   'Usuario que creó el registro';
comment on column formulario.aud_usuario_modifica is
   'Usuario que modificó el registro';
comment on column formulario.aud_usuario_elimina is
   'Usuario que eliminó el registro';

-- Índices para mejorar el rendimiento
create index idx_formulario_plantilla on
   formulario (
      codigo_plantilla_formulario
   );
create index idx_formulario_estado on
   formulario (
      estado,
      eliminado
   );
create index idx_formulario_fecha_creacion on
   formulario (
      fecha_creacion
   );

-- Trigger para actualizar FECHA_ACTUALIZACION automáticamente
create or replace trigger trg_formulario_update before
   update on formulario
   for each row
begin
   :new.fecha_actualizacion := current_timestamp;
end;
/

-- ============================================
-- INSERTAR REGISTROS DE EJEMPLO (opcional)
-- ============================================
-- Estos inserts usan la secuencia seq_formulario para el id y suponen
-- que existen registros en plantilla_formulario a los que referenciar.
-- Uncomment y ajuste los valores según sea necesario para pruebas.

-- insert into formulario (
--    codigo_formulario,
--    codigo_plantilla_formulario,
--    elementos,
--    eliminado,
--    estado,
--    fecha_creacion,
--    fecha_actualizacion,
--    aud_usuario_crea,
--    aud_fecha_crea
-- ) values ( seq_formulario.nextval,
--             1,
--             '[{"id":"page-001","tipo":"page","nombre":"Datos",...}]',
--             'N',
--             'A',
--             current_timestamp,
--             current_timestamp,
--             'ADMIN',
--             current_timestamp );

commit;