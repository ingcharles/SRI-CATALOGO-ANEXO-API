package ec.gob.sri.api.catalogo.infraestructure.persistence.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import ec.gob.sri.api.catalogo.application.dto.CrearFormularioRequest;
import ec.gob.sri.api.catalogo.application.dto.FormularioResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.Formulario;
import ec.gob.sri.api.catalogo.domain.model.enums.EstadoPlantilla;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.FormularioEntity;

/**
 * Mapper para Formulario
 */
@Mapper(componentModel = "jakarta", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FormularioMapper {

    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Entity -> Domain
    @Mapping(source = "codigoFormulario", target = "codigoFormulario")
    @Mapping(source = "plantillaFormulario.codigoPlantillaFormulario", target = "codigoPlantillaFormulario")
    @Mapping(source = "codigoUsuario", target = "codigoUsuario")
    @Mapping(source = "identificacionUsuario", target = "identificacionUsuario")
    @Mapping(source = "elementos", target = "elementos")
    @Mapping(source = "plantillaFormulario.elementosJson", target = "elementosJsonPlantilla")
    @Mapping(source = "plantillaFormulario.elementosXml", target = "elementosXmlPlantilla")
    @Mapping(source = "plantillaFormulario.nombre", target = "nombrePlantilla")
    @Mapping(source = "plantillaFormulario.descripcion", target = "descripcionPlantilla")
    @Mapping(source = "plantillaFormulario.version", target = "versionPlantilla")
    @Mapping(source = "eliminado", target = "eliminado")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "stringToEstado")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "fechaActualizacion", target = "fechaActualizacion")
    Formulario toDomain(FormularioEntity entity);

    List<Formulario> toDomainList(List<FormularioEntity> entities);

    // Domain -> Entity
    @Mapping(source = "codigoFormulario", target = "codigoFormulario")
    @Mapping(source = "codigoPlantillaFormulario", target = "codigoPlantillaFormulario")
    @Mapping(source = "codigoUsuario", target = "codigoUsuario")
    @Mapping(source = "identificacionUsuario", target = "identificacionUsuario")
    @Mapping(source = "elementos", target = "elementos")
    @Mapping(source = "eliminado", target = "eliminado")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "fechaActualizacion", target = "fechaActualizacion")
    @Mapping(target = "plantillaFormulario", ignore = true)
    @Mapping(target = "audFechaCrea", ignore = true)
    @Mapping(target = "audFechaElimina", ignore = true)
    @Mapping(target = "audFechaModifica", ignore = true)
    @Mapping(target = "audUsuarioCrea", ignore = true)
    @Mapping(target = "audUsuarioElimina", ignore = true)
    @Mapping(target = "audUsuarioModifica", ignore = true)
    FormularioEntity toEntity(Formulario domain);

    // Domain -> Response
    @Mapping(source = "codigoFormulario", target = "codigoFormulario")
    @Mapping(source = "codigoPlantillaFormulario", target = "codigoPlantillaFormulario")
    @Mapping(source = "codigoUsuario", target = "codigoUsuario")
    @Mapping(source = "identificacionUsuario", target = "identificacionUsuario")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToString")
    @Mapping(source = "elementos", target = "elementos")
    @Mapping(source = "elementosJsonPlantilla", target = "elementosJsonPlantilla")
    @Mapping(source = "nombrePlantilla", target = "nombrePlantilla")
    @Mapping(source = "descripcionPlantilla", target = "descripcionPlantilla")
    @Mapping(source = "versionPlantilla", target = "versionPlantilla")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion", qualifiedByName = "dateToString")
    @Mapping(source = "fechaActualizacion", target = "fechaActualizacion", qualifiedByName = "dateToString")
    FormularioResponse toResponse(Formulario domain);

    List<FormularioResponse> toResponseList(List<Formulario> domains);

    // Request -> Domain
    @Mapping(target = "codigoFormulario", ignore = true)
    @Mapping(source = "codigoPlantillaFormulario", target = "codigoPlantillaFormulario")
    @Mapping(source = "codigoUsuario", target = "codigoUsuario")
    @Mapping(source = "identificacionUsuario", target = "identificacionUsuario")
    @Mapping(target = "elementos", ignore = true)
    @Mapping(target = "elementosJsonPlantilla", ignore = true)
    @Mapping(target = "nombrePlantilla", ignore = true)
    @Mapping(target = "descripcionPlantilla", ignore = true)
    @Mapping(target = "versionPlantilla", ignore = true)
    @Mapping(target = "eliminado", constant = "N")
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Formulario toDomainFromRequest(CrearFormularioRequest request);

    // Métodos de conversión personalizados
    @Named("dateToString")
    default String dateToString(LocalDateTime date) {
        return date != null ? date.format(formatter) : null;
    }

    @Named("estadoToCodigo")
    default String estadoToCodigo(EstadoPlantilla estado) {
        return estado != null ? estado.getCodigo() : null;
    }

    @Named("estadoToString")
    default String estadoToString(EstadoPlantilla estado) {
        return estado != null ? estado.getDescripcion() : null;
    }

    @Named("stringToEstado")
    default EstadoPlantilla stringToEstado(String estado) {
        return estado != null ? EstadoPlantilla.fromCodigo(estado) : null;
    }

}
