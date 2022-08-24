/**
 * Clase ParametroAmbienteControllerTest.java 24 ago. 2022
 * Copyright 2022 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * @author cfcg070314
 *
 */
@QuarkusTest
public class ParametroAmbienteControllerTest {

	@Test
	public void deberiaResponderConsultar() {
		given().when().get("/parametros/MAIL_SERVER?codigoApp=ADM").then().statusCode(200).body(is("[{\"valor\":\"10.1.0.76\"}]"));
	}

	
}
