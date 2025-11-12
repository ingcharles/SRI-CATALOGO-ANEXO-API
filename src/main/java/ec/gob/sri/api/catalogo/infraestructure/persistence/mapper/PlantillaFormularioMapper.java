package ec.gob.sri.api.catalogo.infraestructure.persistence.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.sri.api.catalogo.application.dto.*;
import ec.gob.sri.api.catalogo.domain.model.entity.PlantillaFormulario;
import ec.gob.sri.api.catalogo.domain.model.enums.EstadoPlantilla;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.PlantillaFormularioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Mapper para PlantillaFormulario
 */
@Mapper(componentModel = "jakarta", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PlantillaFormularioMapper {

    ObjectMapper objectMapper = new ObjectMapper();
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Entity -> Domain
    @Mapping(source = "codigoPlantillaFormulario", target = "codigoPlantillaFormulario")
    @Mapping(source = "codigo", target = "codigo")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "paginas", target = "paginas")
    @Mapping(source = "eliminado", target = "eliminado")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "stringToEstado")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "fechaActualizacion", target = "fechaActualizacion")
    PlantillaFormulario toDomain(PlantillaFormularioEntity entity);

    List<PlantillaFormulario> toDomainList(List<PlantillaFormularioEntity> entities);

    // Domain -> Entity
    @Mapping(source = "codigoPlantillaFormulario", target = "codigoPlantillaFormulario")
    @Mapping(source = "codigo", target = "codigo")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "paginas", target = "paginas")
    @Mapping(source = "eliminado", target = "eliminado")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "fechaActualizacion", target = "fechaActualizacion")
    @Mapping(target = "audFechaCrea", ignore = true)
    @Mapping(target = "audFechaElimina", ignore = true)
    @Mapping(target = "audFechaModifica", ignore = true)
    @Mapping(target = "audUsuarioCrea", ignore = true)
    @Mapping(target = "audUsuarioElimina", ignore = true)
    @Mapping(target = "audUsuarioModifica", ignore = true)
    PlantillaFormularioEntity toEntity(PlantillaFormulario domain);

    // Domain -> Response
    @Mapping(source = "codigoPlantillaFormulario", target = "codigoPlantillaFormulario")
    @Mapping(source = "codigo", target = "codigo")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToString")
    @Mapping(source = "paginas", target = "paginas")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion", qualifiedByName = "dateToString")
    @Mapping(source = "fechaActualizacion", target = "fechaActualizacion", qualifiedByName = "dateToString")
    GuardarFormularioResponse toResponse(PlantillaFormulario domain);

    List<GuardarFormularioResponse> toResponseList(List<PlantillaFormulario> domains);

    // Request -> Domain
    @Mapping(target = "codigoPlantillaFormulario", ignore = true)
    @Mapping(source = "codigo", target = "codigo")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "version", target = "version")
    @Mapping(target = "paginas", ignore = true) // Se maneja manualmente en el servicio
    @Mapping(target = "eliminado", constant = "N")
    @Mapping(target = "estado", ignore = true) // Se maneja manualmente en el servicio
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    PlantillaFormulario toDomainFromRequest(GuardarFormularioRequest request);

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
