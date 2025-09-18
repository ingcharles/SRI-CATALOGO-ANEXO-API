package ec.gob.sri.api.catalogo.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * The persistent class for the ADM_PERIODICIDAD database table.
 */
@Entity
@Getter
@Setter
@Table(name = "ADM_PERIODICIDAD")
@NamedQuery(name = "PeriodicidadEntity.findAll", query = "SELECT p FROM PeriodicidadEntity p")

public class PeriodicidadEntity {
    //public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODIGO_PERIODICIDAD", unique = true, nullable = false, precision = 2)
    public Long codigoPeriodicidad;

    @Column(nullable = false, length = 15)
    public String abreviacion;

    @Column(name = "AUD_FECHA_CREA")
    public LocalDateTime audFechaCrea;

    @Column(name = "AUD_FECHA_ELIMINA")
    public LocalDateTime audFechaElimina;

    @Column(name = "AUD_FECHA_MODIFICA")
    public LocalDateTime audFechaModifica;

    @Column(name = "AUD_USUARIO_CREA", nullable = false, length = 30)
    public String audUsuarioCrea;

    @Column(name = "AUD_USUARIO_ELIMINA", length = 30)
    public String audUsuarioElimina;

    @Column(name = "AUD_USUARIO_MODIFICA", length = 30)
    public String audUsuarioModifica;

    @Column(nullable = false, length = 30)
    public String descripcion;

    @Column(nullable = false, length = 1)
    public String eliminado;

    @Column(nullable = false, length = 1)
    public String estado;

    @Column(name = "TIPO_PERIODICIDAD", nullable = false, precision = 4)
    public BigDecimal tipoPeriodicidad;

    //bi-directional many-to-one association to PeriodoEntity
    @OneToMany(mappedBy = "periodicidadEntity")
    public List<PeriodoEntity> periodosEntity;

}