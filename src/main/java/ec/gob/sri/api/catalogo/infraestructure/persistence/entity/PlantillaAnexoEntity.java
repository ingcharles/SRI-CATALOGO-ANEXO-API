package ec.gob.sri.api.catalogo.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad JPA para la tabla PLANTILLA_ANEXO
 */
@Entity
@Getter
@Setter
@Table(name = "PLANTILLA_ANEXO")
@NamedQuery(name = "PlantillaAnexoEntity.findAll", query = "SELECT p FROM PlantillaAnexoEntity p")
public class PlantillaAnexoEntity {

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 36)
    private String id;

    @Column(name = "CODIGO", nullable = false, length = 50)
    private String codigo;

    @Column(name = "NOMBRE", nullable = false, length = 255)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 1000)
    private String descripcion;

    @Column(name = "VERSION", nullable = false, length = 20)
    private String version;

    @Lob
    @Column(name = "PAGINAS", nullable = false)
    private String paginas;

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
