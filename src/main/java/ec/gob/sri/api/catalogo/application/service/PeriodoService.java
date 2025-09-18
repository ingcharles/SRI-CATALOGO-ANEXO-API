package ec.gob.sri.api.catalogo.application.service;

import ec.gob.sri.api.catalogo.application.dto.PeriodoResponse;
import ec.gob.sri.api.catalogo.domain.repository.PeriodoRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PeriodoMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class PeriodoService {

    private static final Logger LOG = Logger.getLogger(PeriodoService.class);

    @Inject
    PeriodoRepository periodoRepository;

    @Inject
    PeriodoMapper periodoMapper;

    public Uni<List<PeriodoResponse>> consultarPorCodigoPeriodicidad(BigDecimal codPeriodicidad) {
        LOG.infof("🔧 [SERVICE] Iniciando consultarPorCodigoPeriodicidad con codPeriodicidad: %s", codPeriodicidad);
        System.out.println("🟡 DEBUG: [SERVICE] Parámetro codPeriodicidad = " + codPeriodicidad);
        
        return periodoRepository.consultarPorCodigoPeriodicidad(codPeriodicidad)
                .onItem().invoke(periodos -> {
                    LOG.infof("📊 [SERVICE] Repository retornó %d periodos de dominio", periodos.size());
                    System.out.println("🟡 DEBUG: [SERVICE] Periodos de dominio desde repository = " + periodos.size());
                })
                .map(periodos -> {
                    LOG.info("🔄 [SERVICE] Iniciando mapping a Response DTOs");
                    System.out.println("🟡 DEBUG: [SERVICE] Iniciando mapping a Response DTOs");
                    return periodoMapper.toResponseList(periodos);
                })
                .onItem().invoke(responses -> {
                    LOG.infof("✅ [SERVICE] Mapping completado. Response DTOs: %d", responses.size());
                    System.out.println("🟡 DEBUG: [SERVICE] Response DTOs generados = " + responses.size());
                    
                    responses.forEach(response -> {
                        LOG.infof("🎯 [SERVICE] Response DTO: valor=%s, etiqueta=%s", 
                            response.valor, response.etiqueta);
                        System.out.println("🟡 DEBUG: [SERVICE] Response DTO - valor=" + response.valor + 
                                         ", etiqueta=" + response.etiqueta);
                    });
                })
                .onFailure().invoke(throwable -> {
                    LOG.errorf("❌ [SERVICE] Error en consultarPorCodigoPeriodicidad: %s", throwable.getMessage());
                    System.out.println("🔴 ERROR: [SERVICE] " + throwable.getMessage());
                    throwable.printStackTrace();
                });
    }

}

