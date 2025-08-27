package ec.gob.sri.api.catalogo.infraestructure.persistence.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ADM_PARAMETRO_AMBIENTE")
public class ParametroAmbienteEntity {

    @Id
    @Column(name = "CODIGO_PARAMETRO_AMBIENTE")
    public Long codigoParametro;

    @Column(name = "NOMBRE_PARAMETRO", nullable = false)
    public String nombreParametro;

    @Column(name = "CODIGO_APLICACION", nullable = false)
    public String codigoAplicacion;

    @Column(name = "AMBIENTE", nullable = false)
    public String ambiente; 

    @Column(name = "VALOR_TEXTO", nullable = false)
    public String valor;

    @Column(name = "ESTADO")
    public String estado;

    @Column(name = "ELIMINADO")
    public String eliminado;

}
