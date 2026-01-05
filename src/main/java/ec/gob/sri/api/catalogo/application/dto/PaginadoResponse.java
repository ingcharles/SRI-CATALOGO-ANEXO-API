package ec.gob.sri.api.catalogo.application.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO genérico para respuestas paginadas
 * <p>
 * Similar a Page<T> de Spring, proporciona metadatos completos de paginación
 */
@Getter
@Setter
public class PaginadoResponse<T> {

  /**
   * Lista de contenido de la página actual
   */
  private List<T> contenido;

  /**
   * Total de elementos en toda la base de datos (sin paginar)
   */
  private long totalElementos;

  /**
   * Total de páginas disponibles
   */
  private int totalPaginas;

  /**
   * Página actual (base 1 para el cliente)
   */
  private int paginaActual;

  /**
   * Tamaño de la página (registros por página)
   */
  private int tamanio;

  /**
   * Constructor vacío para serializador
   */
  public PaginadoResponse() {
  }

  /**
   * Constructor completo
   */
  public PaginadoResponse(List<T> contenido, long totalElementos, int totalPaginas,
      int paginaActual, int tamanio) {
    this.contenido = contenido;
    this.totalElementos = totalElementos;
    this.totalPaginas = totalPaginas;
    this.paginaActual = paginaActual;
    this.tamanio = tamanio;
  }
}
