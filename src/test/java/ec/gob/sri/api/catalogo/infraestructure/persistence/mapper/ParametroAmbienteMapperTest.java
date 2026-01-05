package ec.gob.sri.api.catalogo.infraestructure.persistence.mapper;

import ec.gob.sri.api.catalogo.application.dto.ParametroAmbienteResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.ParametroAmbiente;
import ec.gob.sri.api.catalogo.domain.model.enums.Ambiente;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class ParametroAmbienteMapperTest {

    private final ParametroAmbienteMapper mapper = Mappers.getMapper(ParametroAmbienteMapper.class);

    private static ParametroAmbiente dom(Long id, String nombre, String codigoApp,
                                         String ambiente, String valor, String estado) {
        ParametroAmbiente d = new ParametroAmbiente();
        d.setCodigoParametro(id);
        d.setNombreParametro(nombre);
        d.setCodigoAplicacion(codigoApp);
        d.setAmbiente(ambiente);
        d.setValor(valor);
        d.setEstado(estado);
        return d;
    }

    @Test
    void toResponse_mapeaTodosLosCampos() {
        ParametroAmbiente domain = dom(10L, "URL_SERVICIO", "ADM", "PRO", "https://sri/api", "A");

        ParametroAmbienteResponse dto = mapper.toResponse(domain);

        assertNotNull(dto);
        assertEquals(10L, dto.getCodigoParametro());
        assertEquals("URL_SERVICIO", dto.getNombreParametro());
        assertEquals("ADM", dto.getCodigoAplicacion());
        assertEquals("PRO", dto.getAmbiente());
        assertEquals("https://sri/api", dto.getValor());
        assertEquals("A", dto.getEstado());
    }

    @Test
    void toResponseList_mapeaColeccion() {
        List<ParametroAmbiente> list = List.of(
                dom(1L, "URL_SERVICIO", "ADM", "PRO", "https://x", "A"),
                dom(2L, "TIMEOUT", "ADM", "CER", "5000", "A"));

        List<ParametroAmbienteResponse> out = mapper.toResponseList(list);

        assertNotNull(out);
        assertEquals(2, out.size());
        assertEquals(1L, out.get(0).getCodigoParametro());
        assertEquals("TIMEOUT", out.get(1).getNombreParametro());
    }

    @Test
    void toAmbiente_convierteStringAEnum_valido() {
        // tu enum es: CON, CER, PRO
        Ambiente a = mapper.toAmbiente("PRO");
        assertEquals(Ambiente.PRO, a);
    }

    @Test
    void conversionesSoportanNullYODesconocidos() {
        assertNull(mapper.toAmbiente(null));
        assertNull(mapper.toAmbiente("DESCONOCIDO"));
        assertNull(mapper.ambienteToString(null));
    }

    @Test
    void ambienteToString_convierteEnumAString() {
        assertEquals("CER", mapper.ambienteToString(Ambiente.CER));
        assertEquals("CON", mapper.ambienteToString(Ambiente.CON));
        assertEquals("PRO", mapper.ambienteToString(Ambiente.PRO));
    }
}
