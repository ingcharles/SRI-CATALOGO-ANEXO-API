package ec.gob.sri.api.catalogo.infraestructure.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.sri.api.catalogo.application.dto.GuardarPlantillaFormularioRequest;
import ec.gob.sri.api.catalogo.application.dto.GuardarPlantillaFormularioResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.PlantillaFormulario;
import ec.gob.sri.api.catalogo.domain.model.enums.EstadoPlantilla;
import ec.gob.sri.api.catalogo.infraestructure.persistence.dto.PlantillaFormularioListaDTO;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.PlantillaFormularioEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

/**
 * Mapper para PlantillaFormulario
 */
@Mapper(componentModel = "jakarta", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)

public interface PlantillaFormularioMapper {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Entity -> Domain
    @Mapping(source = "codigoPlantillaFormulario", target = "codigoPlantillaFormulario")
    @Mapping(source = "codigo", target = "codigo")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "elementosJson", target = "elementosJson")
    @Mapping(source = "elementosXml", target = "elementosXml")
    @Mapping(source = "eliminado", target = "eliminado")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "stringToEstado")
    @Mapping(source = "motivo", target = "motivo")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "fechaActualizacion", target = "fechaActualizacion")
    @Mapping(source = "fechaRevision", target = "fechaRevision")
    @Mapping(source = "fechaAprobacion", target = "fechaAprobacion")
    @Mapping(source = "fechaPublicacion", target = "fechaPublicacion")
    PlantillaFormulario toDomain(PlantillaFormularioEntity entity);

    List<PlantillaFormulario> toDomainList(List<PlantillaFormularioEntity> entities);

    // Domain -> Entity
    @Mapping(source = "codigoPlantillaFormulario", target = "codigoPlantillaFormulario")
    @Mapping(source = "codigo", target = "codigo")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "elementosJson", target = "elementosJson")
    @Mapping(source = "elementosXml", target = "elementosXml")
    @Mapping(source = "eliminado", target = "eliminado")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(source = "motivo", target = "motivo")
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
    @Mapping(source = "motivo", target = "motivo")
    @Mapping(target = "elementosJson", ignore = true)
    @Mapping(source = "elementosXml", target = "elementosXml")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion", qualifiedByName = "dateToString")
    @Mapping(source = "fechaActualizacion", target = "fechaActualizacion", qualifiedByName = "dateToString")
    @Mapping(source = "fechaRevision", target = "fechaRevision", qualifiedByName = "dateToString")
    @Mapping(source = "fechaAprobacion", target = "fechaAprobacion", qualifiedByName = "dateToString")
    @Mapping(source = "fechaPublicacion", target = "fechaPublicacion", qualifiedByName = "dateToString")
    GuardarPlantillaFormularioResponse toResponse(PlantillaFormulario domain);

    List<GuardarPlantillaFormularioResponse> toResponseList(List<PlantillaFormulario> domains);

    // Request -> Domain
    @Mapping(target = "codigoPlantillaFormulario", ignore = true)
    @Mapping(source = "codigo", target = "codigo")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "version", target = "version")
    @Mapping(target = "elementosJson", ignore = true)
    @Mapping(target = "elementosXml", ignore = true)
    @Mapping(target = "eliminado", constant = "N")
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "motivo", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "fechaRevision", ignore = true)
    @Mapping(target = "fechaAprobacion", ignore = true)
    @Mapping(target = "fechaPublicacion", ignore = true)
    PlantillaFormulario toDomainFromRequest(GuardarPlantillaFormularioRequest request);

    // Update Entity from Domain (para actualización completa)
    @Mapping(source = "codigo", target = "codigo")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "elementosJson", target = "elementosJson")
    @Mapping(source = "elementosXml", target = "elementosXml")
    @Mapping(source = "estado", target = "estado", qualifiedByName = "estadoToCodigo")
    @Mapping(source = "motivo", target = "motivo")
    @Mapping(target = "codigoPlantillaFormulario", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "audFechaCrea", ignore = true)
    @Mapping(target = "audFechaElimina", ignore = true)
    @Mapping(target = "audFechaModifica", ignore = true)
    @Mapping(target = "audUsuarioCrea", ignore = true)
    @Mapping(target = "audUsuarioElimina", ignore = true)
    @Mapping(target = "audUsuarioModifica", ignore = true)
    void updateEntityFromDomain(PlantillaFormulario domain,
        @MappingTarget PlantillaFormularioEntity entity);

    // Métodos de conversión personalizados
    @Named("dateToString")
    default String dateToString(LocalDateTime date) {
        return date != null ? date.format(formatter) : null;
    }

    // elementosJson is ignored here; service will parse the JSON string to the DTO
    // field

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

    // @Mapping(target = "elementosJson", ignore = true)
    // @Mapping(target = "elementosXml", ignore = true)
    @Mapping(target = "eliminado", constant = "N")
    @Mapping(target = "fechaRevision", ignore = true)
    @Mapping(target = "fechaAprobacion", ignore = true)
    @Mapping(target = "fechaPublicacion", ignore = true)
    PlantillaFormulario toDomainFromListaDTO(PlantillaFormularioListaDTO dto);

    List<PlantillaFormulario> toDomainFromListaDTOList(List<PlantillaFormularioListaDTO> dtos);


    /**
     * Mapea lista de dominios a DTOs de respuesta, parseando elementos JSON Responsabilidad de
     * infraestructura/mapeo: transformar datos persistidos a transfer objects
     *
     * @param plantillas   Lista de objetos de dominio
     * @param objectMapper Mapper JSON para parsear cadenas de JSON
     * @return Lista de DTOs con elementos parseados
     */
    default List<GuardarPlantillaFormularioResponse> toResponseListConElementosParsados(
        List<PlantillaFormulario> plantillas,
        ObjectMapper objectMapper) {
        if (plantillas == null) {
            return null;
        }

        return plantillas.stream()
            .map(p -> {
                GuardarPlantillaFormularioResponse dto = toResponse(p);

                // Parsear JSON string a List<Map<String, Object>> en la capa de infraestructura
                if (p.getElementosJson() != null && !p.getElementosJson().isEmpty()) {
                    try {
                        List<Map<String, Object>> parsed = objectMapper.readValue(
                            p.getElementosJson(),
                            new TypeReference<List<Map<String, Object>>>() {
                            });
                        dto.elementosJson = parsed;
                    } catch (JsonProcessingException e) {
                        dto.elementosJson = null;
                    }
                } else {
                    dto.elementosJson = null;
                }

                return dto;
            })
            .toList();
    }

}
