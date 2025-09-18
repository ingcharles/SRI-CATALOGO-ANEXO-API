package ec.gob.sri.api.catalogo.infraestructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
public class UbicacionGeograficaEntity {
    //public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODIGO_UBICACION_GEOGRAFICA", unique = true, nullable = false, length = 30)
    public String codigoUbicacionGeografica;

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

    @Column(name = "CODIGO_NIVEL_GEOGRAFICO", nullable = false, precision = 2)
    public BigDecimal codigoNivelGeografico;

    @Column(nullable = false, length = 120)
    public String descripcion;

    @Column(nullable = false, length = 1)
    public String eliminado;

    @Column(nullable = false, length = 1)
    public String estado;

    //bi-directional many-to-one association to UbicacionGeograficaEntity
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "CODIGO_PADRE")
    public UbicacionGeograficaEntity ubicacionGeograficaEntity;

    //bi-directional many-to-one association to UbicacionGeograficaEntity
    @OneToMany(mappedBy = "ubicacionGeograficaEntity", cascade = {CascadeType.ALL})
    public List<UbicacionGeograficaEntity> ubicacionGeograficasEntity;
    

}