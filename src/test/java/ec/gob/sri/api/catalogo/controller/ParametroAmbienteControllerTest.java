/**
 * Clase ParametroAmbienteControllerTest.java 24 ago. 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

/**
 * @author cfcg070314
 *
 */
@QuarkusTest
@TestSecurity(authorizationEnabled = false)
public class ParametroAmbienteControllerTest {

	@Test
	public void deberiaResponderConsultar() {
		given().when().get("/parametros/MAIL_SERVER?codigoApp=ADM").then().statusCode(200)
				.body(containsString("[{\"ambiente\":\"PRO\""));
	}

	@Test
	public void deberiaResponderConsultarNoEncontrado() {
		given().when().get("/parametrosxyz/XYZ?codigoApp=ADM").then().statusCode(404);
	}

}
