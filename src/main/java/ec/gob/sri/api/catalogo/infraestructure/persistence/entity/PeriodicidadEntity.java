package ec.gob.sri.api.catalogo.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
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

public class PeriodicidadEntity implements Serializable {

    @Column(nullable = false, length = 15)
    private String abreviacion;
    @Column(name = "AUD_FECHA_CREA")
    private LocalDateTime audFechaCrea;
    @Column(name = "AUD_FECHA_ELIMINA")
    private LocalDateTime audFechaElimina;
    @Column(name = "AUD_FECHA_MODIFICA")
    private LocalDateTime audFechaModifica;
    @Column(name = "AUD_USUARIO_CREA", nullable = false, length = 30)
    private String audUsuarioCrea;
    @Column(name = "AUD_USUARIO_ELIMINA", length = 30)
    private String audUsuarioElimina;
    @Column(name = "AUD_USUARIO_MODIFICA", length = 30)
    private String audUsuarioModifica;
    @Column(nullable = false, length = 30)
    private String descripcion;
    @Column(nullable = false, length = 1)
    private String eliminado;
    @Column(nullable = false, length = 1)
    private String estado;
    @Column(name = "TIPO_PERIODICIDAD", nullable = false, precision = 4)
    private BigDecimal tipoPeriodicidad;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODIGO_PERIODICIDAD", unique = true, nullable = false, precision = 2)
    private Long codigoPeriodicidad;
    @OneToMany(mappedBy = "periodicidadEntity")
    private List<PeriodoEntity> periodosEntity;

}