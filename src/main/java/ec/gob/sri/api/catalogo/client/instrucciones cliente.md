🚀 2. Cliente en Quarkus
📦 DTO compartido
java
@Getter
@Setter
public class GrupoPorIntegranteDTO implements Serializable {

    private GrupoPorIntegranteClavePrimariaDTO clavePrimaria;
    private String nombreGrupoTrabajo;
    private String codigoCiudadGrupo;
    private String codigoOficinaGrupo;
    private String codigoUnidadAdministrativaGrupo;
    private String nombreAdministrador;
    private String nombreIntegrante;
    private String esSupervisor;
    private String principalSupervisor;
    private BigDecimal porcentajeAsignacion;
    private String estadoGrupo;

    // Campos transitorios
    private String porcentajeSolicitado;
    private Map<String, String> listaPorcentajesAsignaciones = new HashMap<>();

}
@Getter
@Setter
public class GrupoPorIntegranteClavePrimariaDTO implements Serializable {

    private static final long serialVersionUID = 4059009449114347195L;

    private Long codigoGrupoTrabajo;
    private String codigoUsuarioAdministrador;
    private String codigoUsuario;

}

📦 Interfaz REST Client
java
import jakarta.ws.rs.\*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/grupoPorIntegrante")
@RegisterRestClient(configKey = "grupo-por-integrante-rest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GrupoPorIntegranteService {

    @GET
    @Path("/{id}")
    GrupoPorIntegranteDTO listarGruposPorCodigoUsuario(@QueryParam("codigoUsuario") String codigoUsuario);

    //@POST
    //void crear(GrupoPorIntegranteDTO dto);

}
📦 Servicio consumidor
java
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class GrupoPorIntegranteConsumer {

    @Inject
    @RestClient
    GrupoPorIntegranteService grupoPorIntegranteService;

    public void consumir() {
        GrupoPorIntegranteDTO dto = grupoPorIntegranteService.listarGruposPorCodigoUsuario(codigoUsuario);
        System.out.println("Nombre: " + dto.nombreGrupoTrabajo);
    }

}
📄 application.properties
properties
grupo-por-integrante-rest/mp-rest/url=http://localhost:8080/sri-grupo-trabajo-fachada/rest
grupo-por-integrante-rest/mp-rest/scope=javax.inject.Singleton
