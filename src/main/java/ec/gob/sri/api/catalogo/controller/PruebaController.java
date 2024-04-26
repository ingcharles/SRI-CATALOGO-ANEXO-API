package ec.gob.sri.api.catalogo.controller;

import java.util.ArrayList;
import java.util.List;

import ec.gob.sri.api.catalogo.modelo.ParametroAmbiente;
import ec.gob.sri.api.catalogo.service.IParametroAmbienteService;
import io.quarkus.hibernate.reactive.panache.common.WithSessionOnDemand;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
@Path("/pruebas")
@ApplicationScoped
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
@WithSessionOnDemand
public class PruebaController {
	@Inject
	IParametroAmbienteService parametroAmbienteService;
	@GET
	@Path("/prueba01")
	public String prueba01(
			@QueryParam("nombre") String nombre) {
		Uni<List<ParametroAmbiente>> parametros = parametroAmbienteService.obtenerTodos();
		StringBuilder stringBuilder = new StringBuilder();
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0;i<100; i ++) {
			stringBuilder.append(i+ ",");
			final int lenght = stringBuilder.length();
			Thread newThread = new Thread(() -> {
				try {
					for (int j = 0;j<1000; j ++) {
						System.out.println(lenght);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			newThread.start();
			threads.add(newThread);
			
			//			try {
			//			Thread.sleep((5));
			//		} catch (InterruptedException e) {
			//			// TODO Auto-generated catch block
			//			e.printStackTrace();
			//		}
		}
		
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return parametros.toString()+ stringBuilder.toString();
	}
}
