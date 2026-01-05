-- Script para crear tabla PLANTILLA_FORMULARIO
-- Base de datos: Oracle

-- Crear secuencia para generar IDs automáticamente
create sequence seq_plantilla_formulario start with 1 increment by 1 nocache nocycle;

create table plantilla_formulario (
   codigo_plantilla_formulario number(19) not null,
   codigo                      varchar2(50) not null,
   nombre                      varchar2(255) not null,
   descripcion                 varchar2(1000),
   version                     varchar2(20) not null,
   elementos_json              clob not null,
   elementos_xml               clob not null,
   eliminado                   char(1) default 'N' not null,
   estado                      varchar2(2) default 'A' not null,
   motivo                      varchar2(500) not null,
   fecha_creacion              timestamp default current_timestamp not null,
   fecha_actualizacion         timestamp default current_timestamp not null,
   fecha_revision              timestamp default current_timestamp not null,
   fecha_aprobacion            timestamp default current_timestamp not null,
   fecha_publicacion           timestamp default current_timestamp not null,
   aud_usuario_crea            varchar2(30) not null,
   aud_usuario_modifica        varchar2(30),
   aud_usuario_elimina         varchar2(30),
   aud_fecha_crea              timestamp default current_timestamp,
   aud_fecha_modifica          timestamp,
   aud_fecha_elimina           timestamp,
   constraint pk_plantilla_formulario primary key ( codigo_plantilla_formulario ),
   constraint uk_plantilla_codigo unique ( codigo ),
   constraint chk_eliminado check ( eliminado in ( 'S',
                                                   'N' ) ),
   constraint chk_estado
      check ( estado in ( 'A',
                          'I',
                          'EC' ) )
);

-- Comentarios en las columnas
comment on table plantilla_formulario is
   'Tabla para almacenar plantillas de formularios';
comment on column plantilla_formulario.codigo_plantilla_formulario is
   'Identificador único autogenerado de la plantilla';
comment on column plantilla_formulario.codigo is
   'Código único de la plantilla';
comment on column plantilla_formulario.nombre is
   'Nombre de la plantilla';
comment on column plantilla_formulario.descripcion is
   'Descripción de la plantilla';
comment on column plantilla_formulario.version is
   'Versión de la plantilla';
comment on column plantilla_formulario.paginas is
   'Estructura JSON de las páginas del formulario';
comment on column plantilla_formulario.eliminado is
   'Indica si el registro está eliminado (S/N)';
comment on column plantilla_formulario.estado is
   'Estado del registro (A=Activo, I=Inactivo)';
comment on column plantilla_formulario.fecha_creacion is
   'Fecha de creación del registro';
comment on column plantilla_formulario.fecha_actualizacion is
   'Fecha de última actualización';
comment on column plantilla_formulario.aud_usuario_crea is
   'Usuario que creó el registro';
comment on column plantilla_formulario.aud_usuario_modifica is
   'Usuario que modificó el registro';
comment on column plantilla_formulario.aud_usuario_elimina is
   'Usuario que eliminó el registro';

-- Índices para mejorar el rendimiento
create index idx_plantilla_codigo on
   plantilla_formulario (
      codigo
   );
create index idx_plantilla_estado on
   plantilla_formulario (
      estado,
      eliminado
   );
create index idx_plantilla_fecha_creacion on
   plantilla_formulario (
      fecha_creacion
   );

-- Trigger para actualizar FECHA_ACTUALIZACION automáticamente
create or replace trigger trg_plantilla_formulario_update before
   update on plantilla_formulario
   for each row
begin
   :new.fecha_actualizacion := current_timestamp;
end;
/

-- ============================================
-- INSERTAR REGISTROS DE EJEMPLO
-- ============================================

-- Registro 1: Formulario de Declaración de Impuestos
insert into plantilla_formulario (
   codigo_plantilla_formulario,
   codigo,
   nombre,
   descripcion,
   version,
   elementos,
   eliminado,
   estado,
   fecha_creacion,
   fecha_actualizacion,
   aud_usuario_crea,
   aud_fecha_crea
) values ( seq_plantilla_formulario.nextval,
           'FORM-IMP-001',
           'Formulario de Declaración de Impuestos',
           'Formulario para la declaración mensual de impuestos',
           '1.0.0',
           '[{"id":"page-001","tipo":"page","nombre":"Datos Generales","dimension":12,"atributos":{"contenidos":[],"secciones":[{"id":"section-001","tipo":"section","nombre":"Información del Contribuyente","dimension":12,"atributos":{"contenidos":[],"secciones":[]}}]}}]'
           ,
           'N',
           'A',
           current_timestamp,
           current_timestamp,
           'ADMIN',
           current_timestamp );

-- Registro 2: Formulario de Registro de Contribuyente
insert into plantilla_formulario (
   codigo_plantilla_formulario,
   codigo,
   nombre,
   descripcion,
   version,
   elementos,
   eliminado,
   estado,
   fecha_creacion,
   fecha_actualizacion,
   aud_usuario_crea,
   aud_fecha_crea
) values ( seq_plantilla_formulario.nextval,
           'FORM-REG-001',
           'Formulario de Registro de Contribuyente',
           'Formulario para el registro inicial de contribuyentes',
           '2.1.0',
           '[{"id":"page-002","tipo":"page","nombre":"Datos Personales","dimension":12,"atributos":{"contenidos":[],"secciones":[{"id":"section-002","tipo":"section","nombre":"Identificación","dimension":6,"atributos":{"contenidos":[],"secciones":[]}},{"id":"section-003","tipo":"section","nombre":"Dirección","dimension":6,"atributos":{"contenidos":[],"secciones":[]}}]}}]'
           ,
           'N',
           'A',
           current_timestamp,
           current_timestamp,
           'ADMIN',
           current_timestamp );

-- Registro 3: Formulario de Solicitud de Devolución
insert into plantilla_formulario (
   codigo_plantilla_formulario,
   codigo,
   nombre,
   descripcion,
   version,
   elementos,
   eliminado,
   estado,
   fecha_creacion,
   fecha_actualizacion,
   aud_usuario_crea,
   aud_fecha_crea
) values ( seq_plantilla_formulario.nextval,
           'FORM-DEV-001',
           'Formulario de Solicitud de Devolución',
           'Formulario para solicitar devolución de impuestos pagados en exceso',
           '1.5.0',
           '[{"id":"page-003","tipo":"page","nombre":"Solicitud de Devolución","dimension":12,"atributos":{"contenidos":[],"secciones":[{"id":"section-004","tipo":"section","nombre":"Datos del Solicitante","dimension":12,"atributos":{"contenidos":[],"secciones":[]}},{"id":"section-005","tipo":"section","nombre":"Detalle de Pagos","dimension":12,"atributos":{"contenidos":[],"secciones":[]}}]}}]'
           ,
           'N',
           'A',
           current_timestamp,
           current_timestamp,
           'ADMIN',
           current_timestamp );

-- Registro 4: Formulario de Anexo Transaccional
insert into plantilla_formulario (
   codigo_plantilla_formulario,
   codigo,
   nombre,
   descripcion,
   version,
   elementos,
   eliminado,
   estado,
   fecha_creacion,
   fecha_actualizacion,
   aud_usuario_crea,
   aud_fecha_crea
) values ( seq_plantilla_formulario.nextval,
           'FORM-ATS-001',
           'Formulario de Anexo Transaccional Simplificado',
           'Formulario para reportar transacciones y retenciones',
           '3.0.0',
           '[{"id":"page-004","tipo":"page","nombre":"Compras y Retenciones","dimension":12,"atributos":{"contenidos":[],"secciones":[{"id":"section-006","tipo":"section","nombre":"Compras","dimension":6,"atributos":{"contenidos":[],"secciones":[]}},{"id":"section-007","tipo":"section","nombre":"Retenciones","dimension":6,"atributos":{"contenidos":[],"secciones":[]}}]}},{"id":"page-005","tipo":"page","nombre":"Ventas","dimension":12,"atributos":{"contenidos":[],"secciones":[{"id":"section-008","tipo":"section","nombre":"Detalle de Ventas","dimension":12,"atributos":{"contenidos":[],"secciones":[]}}]}}]'
           ,
           'N',
           'A',
           current_timestamp,
           current_timestamp,
           'ADMIN',
           current_timestamp );

-- Registro 5: Formulario de Actualización de Datos (Inactivo como ejemplo)
insert into plantilla_formulario (
   codigo_plantilla_formulario,
   codigo,
   nombre,
   descripcion,
   version,
   elementos,
   eliminado,
   estado,
   fecha_creacion,
   fecha_actualizacion,
   aud_usuario_crea,
   aud_fecha_crea
) values ( seq_plantilla_formulario.nextval,
           'FORM-ACT-001',
           'Formulario de Actualización de Datos',
           'Formulario para actualizar información del contribuyente (Versión antigua - deprecada)',
           '1.0.0',
           '[{"id":"page-006","tipo":"page","nombre":"Actualización","dimension":12,"atributos":{"contenidos":[],"secciones":[{"id":"section-009","tipo":"section","nombre":"Datos a Actualizar","dimension":12,"atributos":{"contenidos":[],"secciones":[]}}]}}]'
           ,
           'N',
           'I',
           current_timestamp,
           current_timestamp,
           'ADMIN',
           current_timestamp );

commit;