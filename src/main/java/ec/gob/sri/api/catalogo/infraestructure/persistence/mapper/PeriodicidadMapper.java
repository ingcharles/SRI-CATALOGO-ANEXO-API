package ec.gob.sri.api.catalogo.infraestructure.persistence.mapper;


import ec.gob.sri.api.catalogo.application.dto.PeriodicidadResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.Periodicidad;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.PeriodicidadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "jakarta", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PeriodicidadMapper {

    // Entity -> Domain
    @Mappings({
            @Mapping(source = "codigoPeriodicidad", target = "codigoPeriodicidad"),
            @Mapping(source = "abreviacion", target = "abreviacion"),
            @Mapping(source = "descripcion", target = "descripcion"),
            @Mapping(source = "eliminado", target = "eliminado"),
            @Mapping(source = "estado", target = "estado")


    })
    Periodicidad toDomain(PeriodicidadEntity e);

    List<Periodicidad> toDomainList(List<PeriodicidadEntity> list);

    // Domain -> Response DTO
    @Mappings({
            @Mapping(source = "codigoPeriodicidad", target = "valor"),
            //@Mapping(source = "abreviacion", target = "abreviacion"),
            @Mapping(source = "descripcion", target = "etiqueta"),
            //@Mapping(source = "eliminado", target = "eliminado"),
            //@Mapping(source = "estado", target = "estado")
    })
    PeriodicidadResponse toResponse(Periodicidad d);

    List<PeriodicidadResponse> toResponseList(List<Periodicidad> list);

}
