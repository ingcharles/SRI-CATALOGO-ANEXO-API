package ec.gob.sri.api.catalogo.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA para la tabla FORMULARIO
 */
@Entity
@Getter
@Setter
@Table(name = "FORMULARIO")

// Consulta básica sin filtros
@NamedQuery(
    name = "FormularioEntity.buscarTodos",
    query = "SELECT f FROM FormularioEntity f"
)
// Consulta con JOIN FETCH optimizado (sin DISTINCT para mejor performance)
@NamedQuery(
    name = "FormularioEntity.buscarTodosConPlantilla",
    query = """
        SELECT f FROM FormularioEntity f 
        LEFT JOIN FETCH f.plantillaFormulario p 
        WHERE f.eliminado = 'N'
        """
)
// Consulta por ID con plantilla
@NamedQuery(
    name = "FormularioEntity.buscarPorIdConPlantilla",
    query = """
        SELECT f FROM FormularioEntity f 
        LEFT JOIN FETCH f.plantillaFormulario p 
        WHERE f.codigoFormulario = :codigoFormulario 
        AND f.eliminado = 'N'
        """
)
// Count optimizado sin JOIN
@NamedQuery(
    name = "FormularioEntity.contarActivos",
    query = "SELECT COUNT(f) FROM FormularioEntity f WHERE f.eliminado = 'N'"
)

public class FormularioEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_formulario")
  @SequenceGenerator(name = "seq_formulario", sequenceName = "SEQ_FORMULARIO", allocationSize = 1)
  @Column(name = "CODIGO_FORMULARIO", unique = true, nullable = false)
  private Long codigoFormulario;

  @Column(name = "CODIGO_PLANTILLA_FORMULARIO", nullable = false, insertable = false, updatable = false)
  private Long codigoPlantillaFormulario;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CODIGO_PLANTILLA_FORMULARIO", referencedColumnName = "CODIGO_PLANTILLA_FORMULARIO")
  private PlantillaFormularioEntity plantillaFormulario;

  @Column(name = "CODIGO_USUARIO", length = 30)
  private String codigoUsuario;

  @Column(name = "IDENTIFICACION_USUARIO", length = 30)
  private String identificacionUsuario;

  @Lob
  @Column(name = "ELEMENTOS", nullable = false)
  private String elementos;

  @Column(name = "ELIMINADO", nullable = false, length = 1)
  private String eliminado;

  @Column(name = "ESTADO", nullable = false, length = 1)
  private String estado;

  @Column(name = "FECHA_CREACION", nullable = false)
  private LocalDateTime fechaCreacion;

  @Column(name = "FECHA_ACTUALIZACION", nullable = false)
  private LocalDateTime fechaActualizacion;

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
