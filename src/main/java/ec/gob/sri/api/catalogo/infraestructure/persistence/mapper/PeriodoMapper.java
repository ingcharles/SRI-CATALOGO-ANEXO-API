package ec.gob.sri.api.catalogo.infraestructure.persistence.mapper;


import ec.gob.sri.api.catalogo.application.dto.PeriodoResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.Periodo;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.PeriodoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "jakarta", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PeriodoMapper {

    // Entity -> Domain
    @Mappings({
            @Mapping(source = "codigoPeriodo", target = "codigoPeriodo"),
            @Mapping(source = "anioFiscal", target = "anioFiscal"),
            @Mapping(source = "periodicidadEntity.codigoPeriodicidad", target = "codigoPeriodicidad"),
            @Mapping(source = "descripcion", target = "descripcion"),
            @Mapping(source = "eliminado", target = "eliminado"),
            @Mapping(source = "estado", target = "estado"),
            @Mapping(source = "fechaFinal", target = "fechaFinal"),
            @Mapping(source = "fechaInicial", target = "fechaInicial"),
            @Mapping(source = "numeroPeriodo", target = "numeroPeriodo"),
            @Mapping(source = "procesado", target = "procesado")


    })
    Periodo toDomain(PeriodoEntity e);

    @Mappings({
            @Mapping(source = "codigoPeriodo", target = "codigoPeriodo"),
            @Mapping(source = "anioFiscal", target = "anioFiscal"),
            @Mapping(source = "periodicidadEntity.codigoPeriodicidad", target = "codigoPeriodicidad"),
            @Mapping(source = "descripcion", target = "descripcion"),
            @Mapping(source = "eliminado", target = "eliminado"),
            @Mapping(source = "estado", target = "estado"),
            @Mapping(source = "fechaFinal", target = "fechaFinal"),
            @Mapping(source = "fechaInicial", target = "fechaInicial"),
            @Mapping(source = "numeroPeriodo", target = "numeroPeriodo"),
            @Mapping(source = "procesado", target = "procesado")
    })
    List<Periodo> toDomainList(List<PeriodoEntity> list);

    // Domain -> Response DTO
    @Mappings({
            @Mapping(source = "codigoPeriodo", target = "valor"),
            @Mapping(source = "descripcion", target = "etiqueta")
    })
    PeriodoResponse toResponse(Periodo d);

    List<PeriodoResponse> toResponseList(List<Periodo> list);

}
