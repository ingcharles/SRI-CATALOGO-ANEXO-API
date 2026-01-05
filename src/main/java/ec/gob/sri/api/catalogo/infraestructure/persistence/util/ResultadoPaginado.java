package ec.gob.sri.api.catalogo.infraestructure.persistence.util;


import java.util.List;

/**
 * Clase auxiliar para manejar resultados paginados con metadatos
 * <p>
 * Encapsula: - Lista de resultados - Total de elementos - Total de páginas - Página actual - Tamaño
 * de página
 */
public class ResultadoPaginado<T> {

  public final List<T> contenido;
  public final long totalElementos;
  public final int totalPaginas;
  public final int paginaActual;
  public final int tamanio;

  public ResultadoPaginado(List<T> contenido, long totalElementos, int totalPaginas,
      int paginaActual, int tamanio) {
    this.contenido = contenido;
    this.totalElementos = totalElementos;
    this.totalPaginas = totalPaginas;
    this.paginaActual = paginaActual;
    this.tamanio = tamanio;
  }
}
