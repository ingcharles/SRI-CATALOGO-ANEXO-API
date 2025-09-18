package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.domain.model.entity.ParametroAmbiente;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.ParametroAmbienteEntity;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.ParametroAmbienteMapper;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Disabled
class ParametroAmbienteRepositoryImplTest {

    @Mock
    ParametroAmbientePanacheRepository repo;

    @Mock
    ParametroAmbienteMapper mapper;

    @InjectMocks
    ParametroAmbienteRepositoryImpl impl;

    private static ParametroAmbienteEntity entity() {
        return new ParametroAmbienteEntity();
    }

    private static ParametroAmbiente domain() {
        return new ParametroAmbiente();
    }

    @Test

    void consultarPorAmbienteYCodigoAplicacion_retornaListaMapeada() {
        String nombreParametro = "URL_SERVICIO";
        String codigoApp = "ADM";

        PanacheQuery<ParametroAmbienteEntity> query = mock(PanacheQuery.class);

        when(repo.find(
                argThat(q ->
                        q.contains("eliminado = 'N'")
                                && q.contains("estado = 'A'")
                                && q.contains("nombreParametro = ?1")
                                && q.contains("codigoAplicacion = ?2")),
                eq(nombreParametro),
                eq(codigoApp)
        )).thenReturn(query);

        List<ParametroAmbienteEntity> entidades = List.of(entity(), entity());
        when(query.list()).thenReturn(Uni.createFrom().item(entidades));

        List<ParametroAmbiente> dominio = List.of(domain(), domain());
        when(mapper.toDomainList(entidades)).thenReturn(dominio);

        List<ParametroAmbiente> out = impl
                .consultarPorAmbienteYCodigoAplicacion(nombreParametro, codigoApp)
                .await().indefinitely();

        assertThat(out).containsExactlyElementsOf(dominio);
        verify(repo).find(anyString(), eq(nombreParametro), eq(codigoApp));
        verify(query).list();
        verify(mapper).toDomainList(entidades);
        verifyNoMoreInteractions(repo, query, mapper);
    }

    @Test
    void consultarPorAmbienteYCodigoAplicacion_listaVacia() {
        String nombreParametro = "TIMEOUT";
        String codigoApp = "ADM";

        PanacheQuery<ParametroAmbienteEntity> query = mock(PanacheQuery.class);
        when(repo.find(anyString(), eq(nombreParametro), eq(codigoApp))).thenReturn(query);
        when(query.list()).thenReturn(Uni.createFrom().item(List.of()));
        when(mapper.toDomainList(anyList())).thenReturn(List.of());

        List<ParametroAmbiente> out = impl
                .consultarPorAmbienteYCodigoAplicacion(nombreParametro, codigoApp)
                .await().indefinitely();

        assertThat(out).isEmpty();
        verify(repo).find(anyString(), eq(nombreParametro), eq(codigoApp));
        verify(query).list();
        verify(mapper).toDomainList(anyList());
        verifyNoMoreInteractions(repo, query, mapper);
    }

    @Test
    void consultarPorAmbienteYCodigoAplicacion_errorPropagado() {
        String nombreParametro = "PROBLEMA";
        String codigoApp = "ADM";

        PanacheQuery<ParametroAmbienteEntity> query = mock(PanacheQuery.class);
        when(repo.find(anyString(), eq(nombreParametro), eq(codigoApp))).thenReturn(query);
        when(query.list()).thenReturn(Uni.createFrom().failure(new RuntimeException("db down")));

        assertThatThrownBy(() ->
                impl.consultarPorAmbienteYCodigoAplicacion(nombreParametro, codigoApp)
                        .await().indefinitely()
        ).isInstanceOf(RuntimeException.class)
         .hasMessageContaining("db down");

        verify(repo).find(anyString(), eq(nombreParametro), eq(codigoApp));
        verify(query).list();
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(repo, query);
    }

    @Test
    void consultarPorAmbienteYCodigoAplicacion_verificaParametrosDeBusqueda() {
        String nombreParametro = "QA_ONLY";
        String codigoApp = "APP1";

        PanacheQuery<ParametroAmbienteEntity> query = mock(PanacheQuery.class);
        when(repo.find(anyString(), anyString(), anyString())).thenReturn(query);
        when(query.list()).thenReturn(Uni.createFrom().item(List.of()));
        when(mapper.toDomainList(anyList())).thenReturn(List.of());

        impl.consultarPorAmbienteYCodigoAplicacion(nombreParametro, codigoApp)
                .await().indefinitely();

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(repo).find(queryCaptor.capture(), eq(nombreParametro), eq(codigoApp));

        String q = queryCaptor.getValue();
        assertThat(q).contains("eliminado = 'N'")
                     .contains("estado = 'A'")
                     .contains("nombreParametro = ?1")
                     .contains("codigoAplicacion = ?2");

        verify(query).list();
        verify(mapper).toDomainList(anyList());
        verifyNoMoreInteractions(repo, query, mapper);
    }
}
