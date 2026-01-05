package ec.gob.sri.api.catalogo.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The persistent class for the ADM_UBICACION_GEOGRAFICA database table.
 */
@Entity
@Getter
@Setter
@Table(name = "ADM_UBICACION_GEOGRAFICA")
@NamedQuery(name = "UbicacionGeograficaEntity.findAll", query = "SELECT u FROM UbicacionGeograficaEntity u")
public class UbicacionGeograficaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODIGO_UBICACION_GEOGRAFICA", unique = true, nullable = false, length = 30)
    private String codigoUbicacionGeografica;

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

    @Column(name = "CODIGO_NIVEL_GEOGRAFICO", nullable = false, precision = 2)
    private BigDecimal codigoNivelGeografico;

    @Column(nullable = false, length = 120)
    private String descripcion;

    @Column(nullable = false, length = 1)
    private String eliminado;

    @Column(nullable = false, length = 1)
    private String estado;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "CODIGO_PADRE")
    private UbicacionGeograficaEntity ubicacionGeograficaPadreEntity;

    @OneToMany(mappedBy = "ubicacionGeograficaPadreEntity", cascade = {CascadeType.ALL})
    private List<UbicacionGeograficaEntity> ubicacionGeograficasEntity;

}