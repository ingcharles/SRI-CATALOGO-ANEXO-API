package ec.gob.sri.api.catalogo.infraestructure.persistence.util;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

/**
 * Utilidad genérica para manejo de paginación con Panache
 */
public class PaginacionUtil {

  /**
   * Constructor privado para evitar instanciación
   */
  private PaginacionUtil() {
    // Utility class, do not instantiate
  }

  /**
   * Normaliza el número de página (convierte de base 1 a base 0) Panache utiliza índice 0 para la
   * primera página
   */
  public static int normalizarPagina(Integer pagina) {
    if (pagina == null || pagina < 1) {
      return 0;
    }
    return pagina - 1;
  }

  /**
   * Normaliza el tamaño de página con valor por defecto
   */
  public static int normalizarTamanio(Integer limite) {
    if (limite == null || limite < 1) {
      return 10; // Default: 10 registros por página
    }
    return limite;
  }

  /**
   * Normaliza el campo de ordenamiento
   */
  public static String normalizarOrdenarPor(String ordenarPor) {
    if (ordenarPor == null || ordenarPor.isEmpty()) {
      return "fechaCreacion";
    }
    return ordenarPor;
  }

  /**
   * Normaliza la dirección de ordenamiento (asc/desc)
   */
  public static String normalizarOrden(String orden) {
    if (orden == null || orden.isEmpty()) {
      return "desc";
    }
    return orden;
  }

  /**
   * Crea un objeto Page de Panache con los parámetros normalizados
   */
  public static Page crearPage(Integer pagina, Integer tamanio) {
    int paginaIndex = normalizarPagina(pagina);
    int tamanioNormalizado = normalizarTamanio(tamanio);
    return Page.of(paginaIndex, tamanioNormalizado);
  }

  /**
   * Crea un objeto Sort de Panache basado en campo y dirección
   */
  public static Sort crearOrdenamiento(String ordenarPor, String orden) {
    String campo = switch (normalizarOrdenarPor(ordenarPor)) {
      case "codigoFormulario" -> "codigoFormulario";
      case "fechaActualizacion" -> "fechaActualizacion";
      default -> "fechaCreacion";
    };

    String ordenNormalizado = normalizarOrden(orden);
    return "asc".equalsIgnoreCase(ordenNormalizado)
        ? Sort.by(campo).ascending()
        : Sort.by(campo).descending();
  }


  /**
   * Calcula el total de páginas
   */
  public static int calcularTotalPaginas(Long totalElementos, int tamanio) {
    if (totalElementos == null || totalElementos == 0) {
      return 0;
    }
    return (int) Math.ceil((double) totalElementos / tamanio);
  }
}
