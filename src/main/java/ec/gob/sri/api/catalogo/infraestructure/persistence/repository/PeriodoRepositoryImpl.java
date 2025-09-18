package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.domain.model.entity.Periodo;
import ec.gob.sri.api.catalogo.domain.repository.PeriodoRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PeriodoMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class PeriodoRepositoryImpl implements PeriodoRepository {

    private static final Logger LOG = Logger.getLogger(PeriodoRepositoryImpl.class);

    @Inject
    PeriodoPanacheRepository periodoPanacheRepository;
    @Inject
    PeriodoMapper periodoMapper;


    @Override
    @WithSession
    public Uni<List<Periodo>> consultarPorCodigoPeriodicidad(BigDecimal codPeriodicidad) {

        LOG.infof("🔍 INICIANDO consultarPorCodigoPeriodicidad con codPeriodicidad: %s", codPeriodicidad);
        
        // Print para debug
        System.out.println("🟡 DEBUG: Parámetro recibido codPeriodicidad = " + codPeriodicidad);
        
       /* List<PeriodoEntity> lista = periodoPanacheRepository
                .find("eliminado = 'N' and estado = 'A' and periodicidadEntity.codigoPeriodicidad = ?1 ORDER BY descripcion ASC", codPeriodicidad)
                .list()
                .await().indefinitely();*/

        String query = "SELECT p FROM PeriodoEntity p JOIN FETCH p.periodicidadEntity WHERE p.eliminado = 'N' and p.estado = 'A' and p.periodicidadEntity.codigoPeriodicidad = ?1 ORDER BY p.descripcion ASC";
        LOG.infof("🔍 Ejecutando query: %s", query);
        System.out.println("🟡 DEBUG: Query ejecutada = " + query);

        return periodoPanacheRepository.find(query, codPeriodicidad)
                .list()
                .onItem().invoke(entities -> {
                    LOG.infof("📊 Entidades encontradas: %d", entities.size());
                    System.out.println("🟡 DEBUG: Número de entidades encontradas = " + entities.size());
                    
                    entities.forEach(entity -> {
                        LOG.infof("🏷️ Entidad: codigoPeriodo=%s, descripcion=%s, periodicidad=%s", 
                            entity.codigoPeriodo, 
                            entity.descripcion,
                            entity.periodicidadEntity != null ? entity.periodicidadEntity.codigoPeriodicidad : "NULL");
                        System.out.println("🟡 DEBUG: Entidad - codigoPeriodo=" + entity.codigoPeriodo + 
                                         ", descripcion=" + entity.descripcion + 
                                         ", periodicidad=" + (entity.periodicidadEntity != null ? entity.periodicidadEntity.codigoPeriodicidad : "NULL"));
                    });
                })
                .map(entities -> {
                    LOG.info("🔄 Iniciando mapping a dominio");
                    System.out.println("🟡 DEBUG: Iniciando mapping a dominio");
                    return periodoMapper.toDomainList(entities);
                })
                .onItem().invoke(periodos -> {
                    LOG.infof("✅ Mapping completado. Periodos de dominio: %d", periodos.size());
                    System.out.println("🟡 DEBUG: Mapping completado. Periodos de dominio = " + periodos.size());
                    
                    periodos.forEach(periodo -> {
                        LOG.infof("🎯 Periodo dominio: codigoPeriodo=%s, descripcion=%s, codigoPeriodicidad=%s", 
                            periodo.getCodigoPeriodo(), 
                            periodo.getDescripcion(),
                            periodo.getCodigoPeriodicidad());
                        System.out.println("🟡 DEBUG: Periodo dominio - codigoPeriodo=" + periodo.getCodigoPeriodo() + 
                                         ", descripcion=" + periodo.getDescripcion() + 
                                         ", codigoPeriodicidad=" + periodo.getCodigoPeriodicidad());
                    });
                })
                .onFailure().invoke(throwable -> {
                    LOG.errorf("❌ Error en consultarPorCodigoPeriodicidad: %s", throwable.getMessage());
                    System.out.println("🔴 ERROR: " + throwable.getMessage());
                    throwable.printStackTrace();
                });
    }
}