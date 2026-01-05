package ec.gob.sri.api.catalogo.application.service;

import ec.gob.sri.api.catalogo.application.dto.ParametroAmbienteResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.ParametroAmbiente;
import ec.gob.sri.api.catalogo.domain.repository.ParametroAmbienteRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.ParametroAmbienteMapper;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
class GestionarParametroAmbienteTest {

    @Mock
    ParametroAmbienteRepository repositorio;

    @Mock
    ParametroAmbienteMapper mapper;

    @InjectMocks
    GestionarParametroAmbiente service;

    @Test
    void consultar_retornaListaMapeada() {
        String ambiente = "PROD";
        String codigoApp = "SRI";

        List<ParametroAmbiente> entidades = List.of(mock(ParametroAmbiente.class),
                mock(ParametroAmbiente.class));
        List<ParametroAmbienteResponse> respuestas = List.of(mock(ParametroAmbienteResponse.class),
                mock(ParametroAmbienteResponse.class));

        when(repositorio.consultarPorAmbienteYCodigoAplicacion(ambiente, codigoApp))
                .thenReturn(Uni.createFrom().item(entidades));
        when(mapper.toResponseList(entidades)).thenReturn(respuestas);

        List<ParametroAmbienteResponse> result = service
                .consultarPorAmbienteYCodigoAplicacion(ambiente, codigoApp).await().indefinitely();

        assertSame(respuestas, result);
        verify(repositorio).consultarPorAmbienteYCodigoAplicacion(ambiente, codigoApp);
        verify(mapper).toResponseList(entidades);
        verifyNoMoreInteractions(repositorio, mapper);
    }

    @Test
    void consultar_listaVacia() {
        String ambiente = "QA";
        String codigoApp = "SRI";

        when(repositorio.consultarPorAmbienteYCodigoAplicacion(ambiente, codigoApp))
                .thenReturn(Uni.createFrom().item(Collections.emptyList()));
        when(mapper.toResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ParametroAmbienteResponse> result = service
                .consultarPorAmbienteYCodigoAplicacion(ambiente, codigoApp).await().indefinitely();

        assertTrue(result.isEmpty());
        verify(repositorio).consultarPorAmbienteYCodigoAplicacion(ambiente, codigoApp);
        verify(mapper).toResponseList(Collections.emptyList());
        verifyNoMoreInteractions(repositorio, mapper);
    }

    @Test
    void consultar_propagaErrorRepositorio() {
        String ambiente = "DEV";
        String codigoApp = "SRI";
        RuntimeException boom = new RuntimeException("db down");

        when(repositorio.consultarPorAmbienteYCodigoAplicacion(ambiente, codigoApp))
                .thenReturn(Uni.createFrom().failure(boom));

        Uni<?> uni = service.consultarPorAmbienteYCodigoAplicacion(ambiente, codigoApp);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> esperarResultado(uni));

        assertEquals("db down", runtimeException.getMessage());
        verify(repositorio).consultarPorAmbienteYCodigoAplicacion(ambiente, codigoApp);
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(repositorio);
    }

    private void esperarResultado(Uni<?> uni) {
        uni.await().indefinitely();
    }
}
