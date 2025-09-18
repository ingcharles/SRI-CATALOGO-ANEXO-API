package ec.gob.sri.api.catalogo.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * The persistent class for the ADM_PERIODO database table.
 */
@Entity
@Getter
@Setter
@Table(name = "ADM_PERIODO")
@NamedQuery(name = "PeriodoEntity.findAll", query = "SELECT p FROM PeriodoEntity p")
public class PeriodoEntity implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODIGO_PERIODO", unique = true, nullable = false, precision = 5)
    public Long codigoPeriodo;

    @Column(name = "ANIO_FISCAL", nullable = false, precision = 4)
    public BigDecimal anioFiscal;

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

    @Column(name = "FECHA_FINAL", nullable = false)
    public LocalDate fechaFinal;

    @Column(name = "FECHA_INICIAL", nullable = false)
    public LocalDate fechaInicial;

    @Column(name = "NUMERO_PERIODO", nullable = false, precision = 3)
    public BigDecimal numeroPeriodo;

    @Column(nullable = false, length = 1)
    public String procesado;

    //bi-directional many-to-one association to PeriodicidadEntity
    @ManyToOne
    @JoinColumn(name = "CODIGO_PERIODICIDAD", nullable = false)
    public PeriodicidadEntity periodicidadEntity;


}