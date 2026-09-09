package ec.gob.sri.api.catalogo.application.service;

import ec.gob.sri.api.catalogo.application.dto.client.GrupoPorIntegranteDTO;
import ec.gob.sri.api.catalogo.domain.repository.GrupoPorIntegranteClient;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
@DisplayName("Tests para GestionarGrupoTrabajoService")
class GestionarGrupoTrabajoServiceTest {

    @Inject
    GestionarGrupoTrabajoService service;

    @InjectMock
    GrupoPorIntegranteClient grupoPorIntegranteClient;

    private GrupoPorIntegranteDTO grupoPorIntegranteDTO;
    private List<GrupoPorIntegranteDTO> listaGrupos;

    @BeforeEach
    void setUp() {
        grupoPorIntegranteDTO = new GrupoPorIntegranteDTO();
        grupoPorIntegranteDTO.setNombreGrupoTrabajo("Grupo Test");
        grupoPorIntegranteDTO.setNombreAdministrador("Admin Test");
        grupoPorIntegranteDTO.setNombreIntegrante("Integrante Test");
        grupoPorIntegranteDTO.setEstadoGrupo("ACTIVO");
        
        listaGrupos = List.of(grupoPorIntegranteDTO);
    }

    @Test
    @DisplayName("Debe obtener lista de grupos por código de usuario exitosamente")
    void debeObtenerGruposPorCodigoUsuarioExitosamente() {
        // Given
        String codigoUsuario = "USUARIO123";
        when(grupoPorIntegranteClient.obtenerGruposPorCodigoUsuario(codigoUsuario))
            .thenReturn(listaGrupos);

        // When
        List<GrupoPorIntegranteDTO> resultado = service.obtenerGruposPorUsuario(codigoUsuario);

        // Then
        assertThat(resultado).isNotNull().hasSize(1);
        assertThat(resultado.get(0).getNombreGrupoTrabajo()).isEqualTo("Grupo Test");
        assertThat(resultado.get(0).getNombreAdministrador()).isEqualTo("Admin Test");
        verify(grupoPorIntegranteClient).obtenerGruposPorCodigoUsuario(codigoUsuario);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no existen grupos para el usuario")
    void debeRetornarListaVaciaCuandoNoExistenGrupos() {
        // Given
        String codigoUsuario = "USUARIO_SIN_GRUPO";
        when(grupoPorIntegranteClient.obtenerGruposPorCodigoUsuario(codigoUsuario))
            .thenReturn(Collections.emptyList());

        // When
        List<GrupoPorIntegranteDTO> resultado = service.obtenerGruposPorUsuario(codigoUsuario);

        // Then
        assertThat(resultado).isEmpty();
        verify(grupoPorIntegranteClient).obtenerGruposPorCodigoUsuario(codigoUsuario);
    }

    @Test
    @DisplayName("Debe retornar null cuando el cliente retorna null")
    void debeRetornarNullCuandoClienteRetornaNull() {
        // Given
        String codigoUsuario = "USUARIO_SIN_GRUPO";
        when(grupoPorIntegranteClient.obtenerGruposPorCodigoUsuario(codigoUsuario))
            .thenReturn(null);

        // When
        List<GrupoPorIntegranteDTO> resultado = service.obtenerGruposPorUsuario(codigoUsuario);

        // Then
        assertThat(resultado).isNull();
        verify(grupoPorIntegranteClient).obtenerGruposPorCodigoUsuario(codigoUsuario);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el código de usuario es nulo")
    void debeLanzarExcepcionCuandoCodigoUsuarioEsNulo() {
        // When & Then
        assertThatThrownBy(() -> service.obtenerGruposPorUsuario(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("código de usuario es requerido");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el código de usuario está vacío")
    void debeLanzarExcepcionCuandoCodigoUsuarioEstaVacio() {
        // When & Then
        assertThatThrownBy(() -> service.obtenerGruposPorUsuario(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("código de usuario es requerido");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el código de usuario es solo espacios")
    void debeLanzarExcepcionCuandoCodigoUsuarioEsSoloEspacios() {
        // When & Then
        assertThatThrownBy(() -> service.obtenerGruposPorUsuario("   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("código de usuario es requerido");
    }
}
