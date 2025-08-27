package ec.gob.sri.api.catalogo.infraestructure.persistence.mapper;


import org.mapstruct.*;
import java.util.List;
import ec.gob.sri.api.catalogo.domain.model.entity.ParametroAmbiente;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.ParametroAmbienteEntity;
import ec.gob.sri.api.catalogo.application.dto.ParametroAmbienteResponse;
import ec.gob.sri.api.catalogo.domain.model.enums.Ambiente;

@Mapper(componentModel = "jakarta", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ParametroAmbienteMapper {

    // Entity -> Domain
    @Mappings({
        @Mapping(source="codigoParametro", target="codigoParametro"),
        @Mapping(source="nombreParametro", target="nombreParametro"),
        @Mapping(source="codigoAplicacion", target="codigoAplicacion"),
        @Mapping(source="ambiente", target="ambiente", qualifiedByName = "toAmbiente"),
        @Mapping(source="valor", target="valor"),
        @Mapping(source="estado", target="estado"),
        @Mapping(source="eliminado", target="eliminado")
        
    })
    ParametroAmbiente toDomain(ParametroAmbienteEntity e);

    List<ParametroAmbiente> toDomainList(List<ParametroAmbienteEntity> list);

    // Domain -> Response DTO
    @Mappings({
        @Mapping(source="codigoParametro", target="codigoParametro"),
        @Mapping(source="nombreParametro", target="nombreParametro"),
        @Mapping(source="codigoAplicacion", target="codigoAplicacion"),
        @Mapping(source="ambiente", target="ambiente", qualifiedByName = "ambienteToString"),
        @Mapping(source="valor", target="valor"),
        @Mapping(source="estado", target="estado")
    })
    ParametroAmbienteResponse toResponse(ParametroAmbiente d);

    List<ParametroAmbienteResponse> toResponseList(List<ParametroAmbiente> list);

    @Named("toAmbiente")
    public default Ambiente toAmbiente(String name) {
        if (name == null) return null;
        try { return Ambiente.valueOf(name); }
        catch (IllegalArgumentException ex) { return null; }
    }

    @Named("ambienteToString")
    public default String ambienteToString(Ambiente a) {
        return a == null ? null : a.name();
    }
}
