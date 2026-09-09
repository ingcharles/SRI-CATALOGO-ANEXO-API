package ec.gob.sri.api.catalogo.application.dto.client;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Schema(name = "GrupoPorIntegranteDTO")
@Getter
@Setter
public class GrupoPorIntegranteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Clave primaria del grupo por integrante")
    private GrupoPorIntegranteClavePrimariaDTO clavePrimaria;

    @Schema(description = "Nombre del grupo de trabajo")
    private String nombreGrupoTrabajo;

    @Schema(description = "Código de la ciudad del grupo")
    private String codigoCiudadGrupo;

    @Schema(description = "Código de la oficina del grupo")
    private String codigoOficinaGrupo;

    @Schema(description = "Código de la unidad administrativa del grupo")
    private String codigoUnidadAdministrativaGrupo;

    @Schema(description = "Nombre del administrador")
    private String nombreAdministrador;

    @Schema(description = "Nombre del integrante")
    private String nombreIntegrante;

    @Schema(description = "Indica si es supervisor")
    private String esSupervisor;

    @Schema(description = "Indica si es el supervisor principal")
    private String principalSupervisor;

    @Schema(description = "Porcentaje de asignación")
    private BigDecimal porcentajeAsignacion;

    @Schema(description = "Estado del grupo")
    private String estadoGrupo;

    // Campos transitorios
    @Schema(description = "Porcentaje solicitado")
    private String porcentajeSolicitado;

    @Schema(description = "Lista de porcentajes de asignaciones")
    private Map<String, String> listaPorcentajesAsignaciones = new HashMap<>();
}
