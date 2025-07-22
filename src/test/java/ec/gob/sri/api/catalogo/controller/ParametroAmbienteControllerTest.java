package ec.gob.sri.api.catalogo.controller;

import ec.gob.sri.api.catalogo.service.IParametroAmbienteService;
import ec.gob.sri.api.catalogo.service.to.ParametroAmbienteTo;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@QuarkusTest
class ParametroAmbienteControllerTest {

    @InjectMock
    IParametroAmbienteService parametroAmbienteService;

    @Test
    void testConsultarParametrosPorNombreYCodigoApp() {
        // Datos simulados
        ParametroAmbienteTo dto1 = new ParametroAmbienteTo("1e", "producción");
        ParametroAmbienteTo dto2 = new ParametroAmbienteTo("1a", "pruebas");
        List<ParametroAmbienteTo> simulados = Arrays.asList(dto1, dto2);

        String nombreParametro = "ambiente";
        String codigoApp = "APP1";

        // Mock del servicio
        when(parametroAmbienteService.consultarParametrosPorNombreCodigoAplicacion(nombreParametro, codigoApp))
            .thenReturn(Uni.createFrom().item(simulados));

        // Prueba del endpoint real
        given()
            .accept(ContentType.JSON)
            .queryParam("codigoApp", codigoApp)
        .when()
            .get("/parametros/" + nombreParametro)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("$", hasSize(2));
    }
}
