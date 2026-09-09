package ec.gob.sri.api.catalogo.application.dto.client;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;

@Schema(name = "GrupoPorIntegranteClavePrimariaDTO")
@Getter
@Setter
public class GrupoPorIntegranteClavePrimariaDTO implements Serializable {

    private static final long serialVersionUID = 4059009449114347195L;

    @Schema(description = "Código del grupo de trabajo")
    private Long codigoGrupoTrabajo;

    @Schema(description = "Código del usuario administrador")
    private String codigoUsuarioAdministrador;

    @Schema(description = "Código del usuario")
    private String codigoUsuario;
}
