package ec.gob.sri.api.catalogo.infraestructure.rest;

import ec.gob.sri.api.catalogo.application.dto.ParametroAmbienteResponse;
import ec.gob.sri.api.catalogo.application.service.GestionarParametroAmbiente;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParametroAmbienteResourceTest {

    @Mock
    GestionarParametroAmbiente gestionarParametro;

    @InjectMocks
    ParametroAmbienteResource resource;

    private static ParametroAmbienteResponse resp(
            long codigoParametro, String nombreParametro, String codigoApp, String ambiente, String valor, String estado) {
        ParametroAmbienteResponse r = new ParametroAmbienteResponse();
        r.codigoParametro = codigoParametro;
        r.nombreParametro = nombreParametro;
        r.codigoAplicacion = codigoApp;
        r.ambiente = ambiente;
        r.valor = valor;
        r.estado = estado;
        return r;
    }

    @Test
    void retorna200_conLista() {
        var lista = List.of(
                resp(101L, "URL_SERVICIO", "ADM", "PRO", "https://api.sri.gob.ec/servicio", "A"),
                resp(102L, "TIMEOUT", "ADM", "PRO", "5000", "A")
        );

        when(gestionarParametro.consultarPorAmbienteYCodigoAplicacion("PRO", "ADM"))
                .thenReturn(Uni.createFrom().item(lista));

        Response response = resource
                .consultarPorAmbienteYCodigoAplicacion("PRO", "ADM")
                .await().indefinitely();

        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        List<ParametroAmbienteResponse> body = (List<ParametroAmbienteResponse>) response.getEntity();
        assertNotNull(body);
        assertEquals(2, body.size());
        assertEquals(101L, body.get(0).codigoParametro);

        verify(gestionarParametro).consultarPorAmbienteYCodigoAplicacion("PRO", "ADM");
        verifyNoMoreInteractions(gestionarParametro);
    }

    @Test
    void retorna404_cuandoListaVacia() {
        when(gestionarParametro.consultarPorAmbienteYCodigoAplicacion("CER", "ADM"))
                .thenReturn(Uni.createFrom().item(List.of()));

        Response response = resource
                .consultarPorAmbienteYCodigoAplicacion("CER", "ADM")
                .await().indefinitely();

        assertEquals(404, response.getStatus());
        assertNull(response.getEntity());

        verify(gestionarParametro).consultarPorAmbienteYCodigoAplicacion("CER", "ADM");
        verifyNoMoreInteractions(gestionarParametro);
    }

    @Test
    void retorna400_cuandoIllegalArgument() {
        when(gestionarParametro.consultarPorAmbienteYCodigoAplicacion("???", "ADM"))
                .thenReturn(Uni.createFrom().failure(new IllegalArgumentException("Ambiente inválido")));

        Response response = resource
                .consultarPorAmbienteYCodigoAplicacion("???", "ADM")
                .await().indefinitely();

        assertEquals(400, response.getStatus());
        verify(gestionarParametro).consultarPorAmbienteYCodigoAplicacion("???", "ADM");
        verifyNoMoreInteractions(gestionarParametro);
    }

    @Test
void retorna500_cuandoErrorNoControlado() {
    when(gestionarParametro.consultarPorAmbienteYCodigoAplicacion("PRO", "ADM"))
            .thenReturn(Uni.createFrom().failure(new RuntimeException("boom")));

    RuntimeException ex = assertThrows(RuntimeException.class, () ->
            resource.consultarPorAmbienteYCodigoAplicacion("PRO", "ADM").await().indefinitely());

    assertEquals("boom", ex.getMessage());

    verify(gestionarParametro).consultarPorAmbienteYCodigoAplicacion("PRO", "ADM");
    verifyNoMoreInteractions(gestionarParametro);
}

}
