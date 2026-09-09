package ec.gob.sri.api.catalogo.infraestructure.persistence.repository;

import ec.gob.sri.api.catalogo.application.dto.PaginadoResponse;
import ec.gob.sri.api.catalogo.domain.model.entity.Formulario;
import ec.gob.sri.api.catalogo.domain.repository.FormularioRepository;
import ec.gob.sri.api.catalogo.infraestructure.persistence.entity.FormularioEntity;
import ec.gob.sri.api.catalogo.infraestructure.persistence.mapper.FormularioMapper;
import ec.gob.sri.api.catalogo.infraestructure.persistence.repository.strategy.FormularioConsultasStrategyImpl;
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
 * Implementación del repositorio de Formulario
 */
@ApplicationScoped
public class FormularioRepositoryImpl implements FormularioRepository {

    private final FormularioPanacheRepository panacheRepository;
    private final PlantillaFormularioPanacheRepository plantillaRepository;
    private final FormularioMapper mapper;
    private final FormularioConsultasStrategyImpl strategy;

    public FormularioRepositoryImpl(FormularioPanacheRepository panacheRepository,
        PlantillaFormularioPanacheRepository plantillaRepository,
        FormularioMapper mapper,
        FormularioConsultasStrategyImpl strategy) {
        this.panacheRepository = panacheRepository;
        this.plantillaRepository = plantillaRepository;
        this.mapper = mapper;
        this.strategy = strategy;
    }

    @Override
    @WithTransaction
    public Uni<Formulario> guardar(Formulario formulario) {
        // Primero buscar la plantilla para establecer la relación
        return plantillaRepository.findById(formulario.getCodigoPlantillaFormulario())
            .onItem().ifNotNull().transformToUni(plantilla -> {
                FormularioEntity entidad = mapper.toEntity(formulario);
                // Establecer la relación con la plantilla
                entidad.setPlantillaFormulario(plantilla);
                // El ID se genera automáticamente por la secuencia de Oracle
                entidad.setFechaCreacion(LocalDateTime.now());
                entidad.setFechaActualizacion(LocalDateTime.now());
                entidad.setAudUsuarioCrea("SYSTEM");
                entidad.setAudFechaCrea(LocalDateTime.now());

                return panacheRepository.persist(entidad)
                    .map(mapper::toDomain);
            })
            .onItem().ifNull().failWith(
                () -> new IllegalArgumentException("Plantilla no encontrada con código: "
                    + formulario.getCodigoPlantillaFormulario()));
    }

    @Override
    @WithTransaction
    public Uni<Formulario> actualizar(Formulario formulario) {
        return panacheRepository.findById(formulario.getCodigoFormulario())
            .onItem().ifNotNull().transformToUni(entidad -> {
                // Actualizar campos simples
                if (formulario.getCodigoUsuario() != null) {
                    entidad.setCodigoUsuario(formulario.getCodigoUsuario());
                }
                if (formulario.getIdentificacionUsuario() != null) {
                    entidad.setIdentificacionUsuario(formulario.getIdentificacionUsuario());
                }
                if (formulario.getElementos() != null) {
                    entidad.setElementos(formulario.getElementos());
                }
                if (formulario.getEstado() != null) {
                    entidad.setEstado(formulario.getEstado().getCodigo());
                }

                // Si se cambió la plantilla, actualizar la relación
                if (formulario.getCodigoPlantillaFormulario() != null
                    && entidad.getPlantillaFormulario() != null
                    && !formulario.getCodigoPlantillaFormulario()
                    .equals(entidad.getPlantillaFormulario().getCodigoPlantillaFormulario())) {
                    return plantillaRepository.findById(formulario.getCodigoPlantillaFormulario())
                        .onItem().ifNotNull().transformToUni(plantilla -> {
                            entidad.setPlantillaFormulario(plantilla);
                            entidad.setFechaActualizacion(LocalDateTime.now());
                            entidad.setAudUsuarioModifica("SYSTEM");
                            entidad.setAudFechaModifica(LocalDateTime.now());

                            return panacheRepository.persist(entidad)
                                .map(mapper::toDomain);
                        })
                        .onItem().ifNull().failWith(
                            () -> new IllegalArgumentException(
                                "Plantilla no encontrada con código: "
                                    + formulario.getCodigoPlantillaFormulario()));
                }

                // Si no se cambió la plantilla, solo actualizar campos
                entidad.setFechaActualizacion(LocalDateTime.now());
                entidad.setAudUsuarioModifica("SYSTEM");
                entidad.setAudFechaModifica(LocalDateTime.now());

                return panacheRepository.persist(entidad)
                    .map(mapper::toDomain);
            });
    }

    @Override
    @WithTransaction
    public Uni<Formulario> buscarPorId(Long codigoFormulario) {
        // ✅ Usando NamedQuery (pre-compilada, mejor performance)
        return panacheRepository.find("#FormularioEntity.buscarPorIdConPlantilla",
                Parameters.with("codigoFormulario", codigoFormulario))
            .firstResult()
            .map(entidad -> entidad != null ? mapper.toDomain(entidad) : null);
    }

    /**
     * Listado optimizado usando JPQL con Strategy.
     * 
     * Estructura:
     * - Strategy: Construye consultas JPQL optimizadas
     * - JOIN FETCH: Carga relaciones eficientemente
     * - FormularioEntity: Usa la entidad directamente (sin DTO)
     */
    @Override
    @WithTransaction
    public Uni<PaginadoResponse<Formulario>> listar(Page pagina, Sort ordenamiento,
        String identificacionUsuario, String buscar) {

        System.out.println("[FormularioRepositoryImpl.listar] 🔍 INICIO - Parámetros recibidos:");
        System.out.println("  - Page index: " + pagina.index);
        System.out.println("  - Page size: " + pagina.size);
        System.out.println("  - Sort: " + ordenamiento);
        System.out.println("  - identificacionUsuario: " + identificacionUsuario);
        System.out.println("  - buscar: " + buscar);

        // Construir consultas JPQL y parámetros usando Strategy
        String consultaListado = strategy.construirConsultaListado(identificacionUsuario, buscar);
        String consultaConteo = strategy.construirConsultaConteo(identificacionUsuario, buscar);
        Map<String, Object> parametros = strategy.construirParametros(identificacionUsuario, buscar);
        
        System.out.println("[FormularioRepositoryImpl.listar] 📝 Consultas construidas:");
        System.out.println("  - Consulta listado: " + consultaListado);
        System.out.println("  - Consulta conteo: " + consultaConteo);
        System.out.println("  - Parámetros: " + parametros);

        // Ejecutar consulta de listado con paginación
        System.out.println("[FormularioRepositoryImpl.listar] 🚀 Ejecutando consulta de listado...");
        Uni<List<FormularioEntity>> listaUni = panacheRepository.find(consultaListado, sortFromStrategy(ordenamiento), parametros)
            .page(pagina)
            .list();

        // Ejecutar consulta de conteo
        System.out.println("[FormularioRepositoryImpl.listar] 🔢 Ejecutando consulta de conteo...");
        Uni<Long> totalUni = panacheRepository.find(consultaConteo, parametros)
            .count();

        // Combinar resultados y calcular metadatos de paginación
        System.out.println("[FormularioRepositoryImpl.listar] 🔄 Combinando resultados...");
        return Uni.combine().all().unis(listaUni, totalUni)
            .asTuple()
            .map(tupla -> {
                List<FormularioEntity> lista = tupla.getItem1();
                long total = tupla.getItem2();

                System.out.println("[FormularioRepositoryImpl.listar] 📊 Resultados de base de datos:");
                System.out.println("  - Registros obtenidos: " + (lista != null ? lista.size() : "null"));
                System.out.println("  - Total en BD: " + total);

                int paginaActual = PaginacionUtil.calcularPaginaActual(pagina.index);
                int tamanio = pagina.size;
                int totalPaginas = PaginacionUtil.calcularTotalPaginas(total, tamanio);

                System.out.println("  - Página actual calculada: " + paginaActual);
                System.out.println("  - Tamaño: " + tamanio);
                System.out.println("  - Total páginas: " + totalPaginas);

                // Mapear entidades a dominio
                List<Formulario> contenido = mapper.toDomainList(lista);
                System.out.println("  - Entidades mapeadas a dominio: " + (contenido != null ? contenido.size() : "null"));

                var respuesta = new PaginadoResponse<>(
                    contenido, total, totalPaginas, paginaActual, tamanio);
                
                System.out.println("[FormularioRepositoryImpl.listar] ✅ FIN - Respuesta creada");
                return respuesta;
            });
    }

    /**
     * Convierte Sort para usarlo con la consulta JPQL.
     */
    private Sort sortFromStrategy(Sort sort) {
        if (sort == null || sort.getColumns().isEmpty()) {
            return Sort.by("f.codigoFormulario", Sort.Direction.Ascending);
        }
        // La estrategia ya mapea los campos, solo necesitamos retornar el sort tal cual
        Sort.Column col = sort.getColumns().get(0);
        return Sort.by("f." + col.getName(), col.getDirection());
    }

    @Override
    @WithTransaction
    public Uni<Boolean> eliminar(Long codigoFormulario) {
        return panacheRepository.findById(codigoFormulario)
            .onItem().ifNotNull().transformToUni(entidad -> {
                entidad.setEliminado("S");
                entidad.setEstado("I");
                entidad.setAudUsuarioElimina("SYSTEM");
                entidad.setAudFechaElimina(LocalDateTime.now());
                entidad.setFechaActualizacion(LocalDateTime.now());

                return panacheRepository.persist(entidad)
                    .map(e -> true);
            })
            .onItem().ifNull().continueWith(false);
    }
}
