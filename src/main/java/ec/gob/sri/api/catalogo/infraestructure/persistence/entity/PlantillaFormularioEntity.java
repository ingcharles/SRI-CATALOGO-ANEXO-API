package ec.gob.sri.api.catalogo.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA para la tabla PLANTILLA_ANEXO
 */
@Entity
@Getter
@Setter
@Table(name = "PLANTILLA_FORMULARIO")
// Consulta básica de todas las plantillas
@NamedQuery(
    name = "PlantillaFormularioEntity.buscarTodas",
    query = """
        SELECT p FROM PlantillaFormularioEntity p 
        WHERE p.eliminado = 'N'
        """
)
// Consulta por ID
@NamedQuery(
    name = "PlantillaFormularioEntity.buscarPorId",
    query = """
        SELECT p FROM PlantillaFormularioEntity p 
        WHERE p.codigoPlantillaFormulario = :codigoPlantillaFormulario 
        AND p.eliminado = 'N'
        """
)
// Count optimizado
@NamedQuery(
    name = "PlantillaFormularioEntity.contarActivos",
    query = "SELECT COUNT(p) FROM PlantillaFormularioEntity p WHERE p.eliminado = 'N'"
)
public class PlantillaFormularioEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_plantilla_formulario")
  @SequenceGenerator(name = "seq_plantilla_formulario", sequenceName = "SEQ_PLANTILLA_FORMULARIO", allocationSize = 1)
  @Column(name = "CODIGO_PLANTILLA_FORMULARIO", unique = true, nullable = false)
  private Long codigoPlantillaFormulario;

  @Column(name = "CODIGO", nullable = false, length = 50)
  private String codigo;

  @Column(name = "NOMBRE", nullable = false, length = 255)
  private String nombre;

  @Column(name = "DESCRIPCION", length = 1000)
  private String descripcion;

  @Column(name = "VERSION", nullable = false, length = 20)
  private String version;

  @Lob
  @Column(name = "ELEMENTOS_JSON", nullable = false)
  private String elementosJson;

  @Lob
  @Column(name = "ELEMENTOS_XML", nullable = false)
  private String elementosXml;

  @Column(name = "ELIMINADO", nullable = false, length = 1)
  private String eliminado;

  @Column(name = "ESTADO", nullable = false, length = 2)
  private String estado;

  @Column(name = "MOTIVO", length = 1000)
  private String motivo;

  @Column(name = "FECHA_CREACION", nullable = false)
  private LocalDateTime fechaCreacion;

  @Column(name = "FECHA_ACTUALIZACION", nullable = false)
  private LocalDateTime fechaActualizacion;

  @Column(name = "FECHA_REVISION")
  private LocalDateTime fechaRevision;

  @Column(name = "FECHA_APROBACION")
  private LocalDateTime fechaAprobacion;

  @Column(name = "FECHA_PUBLICACION")
  private LocalDateTime fechaPublicacion;

  @Column(name = "AUD_USUARIO_CREA", nullable = false, length = 30)
  private String audUsuarioCrea;

  @Column(name = "AUD_USUARIO_MODIFICA", length = 30)
  private String audUsuarioModifica;

  @Column(name = "AUD_USUARIO_ELIMINA", length = 30)
  private String audUsuarioElimina;

  @Column(name = "AUD_FECHA_CREA")
  private LocalDateTime audFechaCrea;

  @Column(name = "AUD_FECHA_MODIFICA")
  private LocalDateTime audFechaModifica;

  @Column(name = "AUD_FECHA_ELIMINA")
  private LocalDateTime audFechaElimina;

  @PrePersist
  public void prePersist() {
    if (this.eliminado == null) {
      this.eliminado = "N";
    }
    if (this.estado == null) {
      this.estado = "A";
    }
    if (this.fechaCreacion == null) {
      this.fechaCreacion = LocalDateTime.now();
    }
    if (this.fechaActualizacion == null) {
      this.fechaActualizacion = LocalDateTime.now();
    }
  }

  @PreUpdate
  public void preUpdate() {
    this.fechaActualizacion = LocalDateTime.now();
  }
}
