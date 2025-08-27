package ec.gob.sri.api.catalogo.domain.repository;


import ec.gob.sri.api.catalogo.domain.model.entity.ParametroAmbiente;
import io.smallrye.mutiny.Uni;
import java.util.List;
import java.util.Optional;

public interface ParametroAmbienteRepository {

    Uni<List<ParametroAmbiente>> consultarPorAmbienteYCodigoAplicacion(String ambiente, String codigoAplicacion);
}
