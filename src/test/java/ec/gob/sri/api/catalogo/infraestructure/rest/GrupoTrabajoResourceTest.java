package ec.gob.sri.api.catalogo.infraestructure.rest;

import ec.gob.sri.api.catalogo.application.dto.client.GrupoPorIntegranteDTO;
import ec.gob.sri.api.catalogo.application.service.GestionarGrupoTrabajoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
@DisplayName("Tests de integración para GrupoTrabajoResource")
class GrupoTrabajoResourceTest {

        @InjectMock
        GestionarGrupoTrabajoService gestionarGrupoTrabajoService;

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
        @DisplayName("GET /grupoTrabajo/codigoUsuario/{codigoUsuario} debe retornar 200 con lista de grupos encontrados")
        void debeRetornar200ConGruposEncontrados() {
                // Given
                String codigoUsuario = "USUARIO123";
                when(gestionarGrupoTrabajoService.obtenerGruposPorUsuario(codigoUsuario))
                                .thenReturn(listaGrupos);

                // When & Then
                given()
                                .pathParam("codigoUsuario", codigoUsuario)
                                .contentType(ContentType.JSON)
                                .when()
                                .get("/grupoTrabajo/codigoUsuario/{codigoUsuario}")
                                .then()
                                .statusCode(200)
                                .body("size()", equalTo(1))
                                .body("[0].nombreGrupoTrabajo", equalTo("Grupo Test"))
                                .body("[0].nombreAdministrador", equalTo("Admin Test"))
                                .body("[0].nombreIntegrante", equalTo("Integrante Test"))
                                .body("[0].estadoGrupo", equalTo("ACTIVO"));
        }

        @Test
        @DisplayName("GET /grupoTrabajo/codigoUsuario/{codigoUsuario} debe retornar 204 cuando no existen grupos")
        void debeRetornar204CuandoNoExistenGrupos() {
                // Given
                String codigoUsuario = "USUARIO_SIN_GRUPO";
                when(gestionarGrupoTrabajoService.obtenerGruposPorUsuario(codigoUsuario))
                                .thenReturn(Collections.emptyList());

                // When & Then
                given()
                                .pathParam("codigoUsuario", codigoUsuario)
                                .contentType(ContentType.JSON)
                                .when()
                                .get("/grupoTrabajo/codigoUsuario/{codigoUsuario}")
                                .then()
                                .statusCode(204);
        }

        @Test
        @DisplayName("GET /grupoTrabajo/codigoUsuario/{codigoUsuario} debe retornar 204 cuando la lista es null")
        void debeRetornar204CuandoListaEsNull() {
                // Given
                String codigoUsuario = "USUARIO_SIN_GRUPO";
                when(gestionarGrupoTrabajoService.obtenerGruposPorUsuario(codigoUsuario))
                                .thenReturn(null);

                // When & Then
                given()
                                .pathParam("codigoUsuario", codigoUsuario)
                                .contentType(ContentType.JSON)
                                .when()
                                .get("/grupoTrabajo/codigoUsuario/{codigoUsuario}")
                                .then()
                                .statusCode(204);
        }

        @Test
        @DisplayName("GET /grupoTrabajo/codigoUsuario/{codigoUsuario} debe retornar 400 cuando código de usuario es inválido")
        void debeRetornar400CuandoCodigoUsuarioEsInvalido() {
                // Given - El servicio lanza excepción cuando el código está vacío (solo espacios)
                String codigoUsuario = " ";
                when(gestionarGrupoTrabajoService.obtenerGruposPorUsuario(codigoUsuario))
                                .thenThrow(new IllegalArgumentException("El código de usuario es requerido"));

                // When & Then
                given()
                                .pathParam("codigoUsuario", codigoUsuario)
                                .contentType(ContentType.JSON)
                                .when()
                                .get("/grupoTrabajo/codigoUsuario/{codigoUsuario}")
                                .then()
                                .statusCode(400)
                                .body("mensaje", containsString("código de usuario es requerido"));
        }

        @Test
        @DisplayName("GET /grupoTrabajo/codigoUsuario/{codigoUsuario} debe retornar 500 cuando ocurre error interno")
        void debeRetornar500CuandoOcurreErrorInterno() {
                // Given
                String codigoUsuario = "USUARIO123";
                when(gestionarGrupoTrabajoService.obtenerGruposPorUsuario(codigoUsuario))
                                .thenThrow(new RuntimeException("Error de conexión"));

                // When & Then
                given()
                                .pathParam("codigoUsuario", codigoUsuario)
                                .contentType(ContentType.JSON)
                                .when()
                                .get("/grupoTrabajo/codigoUsuario/{codigoUsuario}")
                                .then()
                                .statusCode(500)
                                .body("mensaje", equalTo("Error al procesar la solicitud"));
        }
}
