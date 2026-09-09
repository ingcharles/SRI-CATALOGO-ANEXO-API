package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.application.dto.PaginadoResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.PlantillaFormulario;
import ec.gob.sri.api.catalogo.domain.repository.PlantillaFormularioRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.dto.PlantillaFormularioListaDTO;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.PlantillaFormularioEntity;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.PlantillaFormularioMapper;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.ResultadosObjetoMapper;
import ec.gob.sri.api.catalogo.infraestructure.persistence.repository.executor.ConsultasReactivasExecutor;
import ec.gob.sri.api.catalogo.infraestructure.persistence.repository.strategy.PlantillaFormularioConsultasStrategyImpl;
import ec.gob.sri.api.catalogo.infraestructure.persistence.util.PaginacionUtil;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Implementación del repositorio de PlantillaFormulario
 */
@ApplicationScoped
public class PlantillaFormularioRepositoryImpl implements PlantillaFormularioRepository {

    private final PlantillaFormularioPanacheRepository panacheRepository;
    private final PlantillaFormularioMapper mapper;
    private final PlantillaFormularioConsultasStrategyImpl strategy;
    private final ConsultasReactivasExecutor executor;

    public PlantillaFormularioRepositoryImpl(
        PlantillaFormularioPanacheRepository panacheRepository,
        PlantillaFormularioMapper mapper, PlantillaFormularioConsultasStrategyImpl strategy,
        ConsultasReactivasExecutor executor) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
        this.strategy = strategy;
        this.executor = executor;
    }


    @Override
    @WithTransaction
    public Uni<PlantillaFormulario> guardar(PlantillaFormulario plantilla) {
        PlantillaFormularioEntity entity = mapper.toEntity(plantilla);
        entity.setFechaCreacion(LocalDateTime.now());
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setAudUsuarioCrea("SYSTEM");
        entity.setAudFechaCrea(LocalDateTime.now());

        return panacheRepository.persist(entity)
            .map(mapper::toDomain);
    }

    @Override
    @WithTransaction
    public Uni<PlantillaFormulario> actualizar(PlantillaFormulario plantilla) {
        return panacheRepository.findById(plantilla.getCodigoPlantillaFormulario())
            .onItem().ifNotNull().transformToUni(entity -> {
                mapper.updateEntityFromDomain(plantilla, entity);
                entity.setFechaActualizacion(LocalDateTime.now());
                entity.setAudFechaModifica(LocalDateTime.now());

                return panacheRepository.persist(entity)
                    .map(mapper::toDomain);
            });
    }

    @Override
    @WithTransaction
    public Uni<PlantillaFormulario> actualizarEstado(Long codigoPlantillaFormulario,
        PlantillaFormulario plantilla) {
        return panacheRepository.findById(codigoPlantillaFormulario)
            .onItem().ifNotNull().transformToUni(entity -> {
                if (plantilla.getEstado() != null) {
                    entity.setEstado(plantilla.getEstado().name());
                }
                if (plantilla.getMotivo() != null) {
                    entity.setMotivo(plantilla.getMotivo());
                }
                if (plantilla.getFechaRevision() != null) {
                    entity.setFechaRevision(plantilla.getFechaRevision());
                }
                if (plantilla.getFechaAprobacion() != null) {
                    entity.setFechaAprobacion(plantilla.getFechaAprobacion());
                }
                if (plantilla.getFechaPublicacion() != null) {
                    entity.setFechaPublicacion(plantilla.getFechaPublicacion());
                }
                entity.setFechaActualizacion(LocalDateTime.now());
                entity.setAudUsuarioModifica("SYSTEM");
                entity.setAudFechaModifica(LocalDateTime.now());

                return panacheRepository.persist(entity)
                    .map(mapper::toDomain);
            });
    }

    @Override
    @WithTransaction
    public Uni<PlantillaFormulario> buscarPorId(Long codigoPlantillaFormulario) {
        return panacheRepository.find("#PlantillaFormularioEntity.buscarPorId",
                Parameters.with("codigoPlantillaFormulario", codigoPlantillaFormulario))
            .firstResult()
            .map(entity -> entity != null ? mapper.toDomain(entity) : null);
    }

    @Override
    @WithTransaction
    public Uni<PlantillaFormulario> buscarPorCodigoYVersion(String codigo, String version) {
        return panacheRepository.find("codigo = ?1 AND version = ?2 AND eliminado = 'N'", codigo,
                version)
            .firstResult()
            .map(entity -> entity != null ? mapper.toDomain(entity) : null);
    }

    /**
     * Listado optimizado usando proyección DTO Solo consulta campos necesarios, mejorando
     * performance
     */
    @Override
    @WithTransaction
    public Uni<PaginadoResponse<PlantillaFormulario>> listar(
        Page pagina,
        Sort ordenamiento,
        String codigo,
        String buscar) {

        String consultaListado = strategy.construirConsultaListado(codigo, buscar, ordenamiento);
        String consultaConteo = strategy.construirConsultaConteo(codigo, buscar);
        Map<String, Object> parametros = strategy.construirParametros(codigo, buscar);

        Uni<List<PlantillaFormularioListaDTO>> listaUni = executor.ejecutarConsultaListadoComoObject(
                consultaListado, parametros, pagina)
            .map(resultadosCrudos -> resultadosCrudos.stream()
                .map(fila -> new PlantillaFormularioListaDTO(
                    ResultadosObjetoMapper.aLong(fila[0]),
                    // CODIGO_PLANTILLA_FORMULARIO
                    ResultadosObjetoMapper.aString(fila[1]),        // CODIGO
                    ResultadosObjetoMapper.aString(fila[2]),        // NOMBRE
                    ResultadosObjetoMapper.aString(fila[3]),        // DESCRIPCION
                    ResultadosObjetoMapper.aString(fila[4]),        // VERSION
                    ResultadosObjetoMapper.aClob(fila[5]),          // ELEMENTOS_JSON (CLOB)
                    ResultadosObjetoMapper.aClob(fila[6]),          // ELEMENTOS_XML (CLOB)
                    ResultadosObjetoMapper.aString(fila[7]),        // ESTADO
                    ResultadosObjetoMapper.aString(fila[8]),        // MOTIVO
                    ResultadosObjetoMapper.aLocalDateTime(fila[9]), // FECHA_CREACION
                    ResultadosObjetoMapper.aLocalDateTime(fila[10]) // FECHA_ACTUALIZACION
                ))
                .toList());

        Uni<Long> totalUni = executor.ejecutarConsultaConteo(consultaConteo, parametros);

        return Uni.combine().all().unis(listaUni, totalUni)
            .asTuple()
            .map(tupla -> {
                List<PlantillaFormularioListaDTO> listaDTO = tupla.getItem1();
                long total = tupla.getItem2();

                int paginaActual = PaginacionUtil.calcularPaginaActual(pagina.index);
                int tamanio = pagina.size;
                int totalPaginas = PaginacionUtil.calcularTotalPaginas(total, tamanio);

                List<PlantillaFormulario> contenido = mapper.toDomainFromListaDTOList(listaDTO);

                return new PaginadoResponse<>(
                    contenido, total, totalPaginas, paginaActual, tamanio);
            });
    }

    @Override
    @WithTransaction
    public Uni<Boolean> eliminar(Long codigoPlantillaFormulario) {
        return panacheRepository.findById(codigoPlantillaFormulario)
            .onItem().ifNotNull().transformToUni(entity -> {
                entity.setEliminado("S");
                entity.setEstado("I");
                entity.setAudUsuarioElimina("SYSTEM");
                entity.setAudFechaElimina(LocalDateTime.now());
                entity.setFechaActualizacion(LocalDateTime.now());
                return panacheRepository.persist(entity)
                    .map(e -> true);
            })
            .onItem().ifNull().continueWith(false);
    }

}
