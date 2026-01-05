package ec.gob.sri.api.catalogo.infraestructure.persistence.mapper;

import ec.gob.sri.api.catalogo.application.dto.ParametroAmbienteResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.ParametroAmbiente;
import ec.gob.sri.api.catalogo.domain.model.enums.Ambiente;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.ParametroAmbienteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "jakarta", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ParametroAmbienteMapper {

    @Mapping(source = "codigoParametro", target = "codigoParametro")
    @Mapping(source = "nombreParametro", target = "nombreParametro")
    @Mapping(source = "codigoAplicacion", target = "codigoAplicacion")
    @Mapping(source = "ambiente", target = "ambiente", qualifiedByName = "toAmbiente")
    @Mapping(source = "valor", target = "valor")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "eliminado", target = "eliminado")
    ParametroAmbiente toDomain(ParametroAmbienteEntity e);

    List<ParametroAmbiente> toDomainList(List<ParametroAmbienteEntity> list);

    @Mapping(source = "codigoParametro", target = "codigoParametro")
    @Mapping(source = "nombreParametro", target = "nombreParametro")
    @Mapping(source = "codigoAplicacion", target = "codigoAplicacion")
    @Mapping(source = "ambiente", target = "ambiente", qualifiedByName = "ambienteToString")
    @Mapping(source = "valor", target = "valor")
    @Mapping(source = "estado", target = "estado")
    ParametroAmbienteResponse toResponse(ParametroAmbiente d);

    List<ParametroAmbienteResponse> toResponseList(List<ParametroAmbiente> list);

    @Named("toAmbiente")
    default Ambiente toAmbiente(String name) {
        if (name == null)
            return null;
        try {
            return Ambiente.valueOf(name);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    @Named("ambienteToString")
    default String ambienteToString(Ambiente a) {
        return a == null ? null : a.name();
    }
}
