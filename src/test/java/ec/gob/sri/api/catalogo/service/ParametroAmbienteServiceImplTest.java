package ec.gob.sri.api.catalogo.service;

import ec.gob.sri.api.catalogo.modelo.ParametroAmbiente;
import ec.gob.sri.api.catalogo.repository.ParametroAmbienteRepository;
import ec.gob.sri.api.catalogo.service.to.ParametroAmbienteTo;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ParametroAmbienteServiceImplTest {

    @Mock
    ParametroAmbienteRepository parametroAmbienteRepository;

    ParametroAmbienteServiceImpl parametroAmbienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        parametroAmbienteService = new ParametroAmbienteServiceImpl();
        parametroAmbienteService.parametroAmbienteRepository = parametroAmbienteRepository;
    }

    @Test
    void testConsultarParametrosPorNombreCodigoAplicacion() {
        ParametroAmbiente entidad1 = new ParametroAmbiente();
        entidad1.setValor("v1");
        entidad1.setAmbiente("a1");

        ParametroAmbiente entidad2 = new ParametroAmbiente();
        entidad2.setValor("v2");
        entidad2.setAmbiente("a2");

        when(parametroAmbienteRepository.consultarPorNombreYCodigoAplicacion("ambiente", "APP1"))
            .thenReturn(Uni.createFrom().item(List.of(entidad1, entidad2)));

        List<ParametroAmbienteTo> result = parametroAmbienteService
            .consultarParametrosPorNombreCodigoAplicacion("ambiente", "APP1")
            .await().indefinitely();

        assertEquals(2, result.size());
        assertEquals("v1", result.get(0).getValor());
        assertEquals("a1", result.get(0).getAmbiente());
    }

    @Test
    void testObtenerTodos() {
        ParametroAmbiente entidad = new ParametroAmbiente();
        entidad.setValor("v3");
        entidad.setAmbiente("a3");

        when(parametroAmbienteRepository.obtenerTodos())
            .thenReturn(Uni.createFrom().item(List.of(entidad)));

        List<ParametroAmbiente> result = parametroAmbienteService.obtenerTodos().await().indefinitely();

        assertEquals(1, result.size());
        assertEquals("v3", result.get(0).getValor());
        assertEquals("a3", result.get(0).getAmbiente());
    }
}
