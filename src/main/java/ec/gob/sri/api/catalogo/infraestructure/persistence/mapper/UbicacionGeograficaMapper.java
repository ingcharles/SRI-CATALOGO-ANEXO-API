package ec.gob.sri.api.catalogo.infraestructure.persistence.mapper;

import ec.gob.sri.api.catalogo.application.dto.UbicacionGeograficaResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.UbicacionGeografica;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.UbicacionGeograficaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "jakarta", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UbicacionGeograficaMapper {

    @Mapping(source = "codigoUbicacionGeografica", target = "codigoUbicacionGeografica")
    @Mapping(source = "codigoNivelGeografico", target = "codigoNivelGeografico")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "eliminado", target = "eliminado")
    @Mapping(source = "estado", target = "estado")
    UbicacionGeografica toDomain(UbicacionGeograficaEntity e);

    List<UbicacionGeografica> toDomainList(List<UbicacionGeograficaEntity> list);

    @Mapping(source = "codigoUbicacionGeografica", target = "valor")
    @Mapping(source = "descripcion", target = "etiqueta")
    UbicacionGeograficaResponse toResponse(UbicacionGeografica d);

    List<UbicacionGeograficaResponse> toResponseList(List<UbicacionGeografica> list);

}
