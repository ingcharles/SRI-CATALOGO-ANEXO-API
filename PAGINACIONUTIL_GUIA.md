# PaginacionUtil - Guía de Uso

## 📦 Clase Utilidad de Paginación

La clase `PaginacionUtil` centraliza toda la lógica de paginación para ser reutilizada en cualquier repositorio.

**Ubicación:** `src/main/java/ec/gob/sri/api/catalogo/infraestructure/persistence/util/PaginacionUtil.java`

---

## 🔧 Métodos Disponibles

### 1. Normalización de Página

```java
/**
 * Convierte página de base 1 (cliente) a base 0 (Panache)
 * 
 * @param pagina Página enviada por cliente (base 1)
 * @return Índice para Panache (base 0)
 */
public static int normalizarPagina(Integer pagina) {
    // pagina=1 → retorna 0
    // pagina=null → retorna 0
    // pagina=5 → retorna 4
}

// Ejemplo
int indice = PaginacionUtil.normalizarPagina(1);  // 0
int indice = PaginacionUtil.normalizarPagina(5);  // 4
```

### 2. Normalización de Tamaño

```java
/**
 * Valida y normaliza el tamaño de página
 * 
 * @param limite Registros por página
 * @return Tamaño normalizado (default 10)
 */
public static int normalizarTamanio(Integer limite) {
    // limite=10 → retorna 10
    // limite=null → retorna 10 (default)
    // limite=0 → retorna 10 (default)
}

// Ejemplo
int tamanio = PaginacionUtil.normalizarTamanio(20);   // 20
int tamanio = PaginacionUtil.normalizarTamanio(null); // 10
```

### 3. Normalización de Campo de Ordenamiento

```java
/**
 * Valida el campo de ordenamiento
 * 
 * @param ordenarPor Campo para ordenar
 * @return Campo validado o default "fechaCreacion"
 */
public static String normalizarOrdenarPor(String ordenarPor) {
    // "codigoFormulario" → "codigoFormulario"
    // "fechaActualizacion" → "fechaActualizacion"
    // null → "fechaCreacion"
    // "invalid" → "fechaCreacion"
}

// Ejemplo
String campo = PaginacionUtil.normalizarOrdenarPor("codigoFormulario"); // "codigoFormulario"
String campo = PaginacionUtil.normalizarOrdenarPor(null);               // "fechaCreacion"
```

### 4. Normalización de Dirección

```java
/**
 * Valida la dirección de ordenamiento
 * 
 * @param orden Dirección: "asc" o "desc"
 * @return Dirección validada o default "desc"
 */
public static String normalizarOrden(String orden) {
    // "asc" → "asc"
    // "desc" → "desc"
    // null → "desc"
    // "invalid" → "desc"
}

// Ejemplo
String dir = PaginacionUtil.normalizarOrden("asc");  // "asc"
String dir = PaginacionUtil.normalizarOrden(null);   // "desc"
```

### 5. Crear Page Directamente

```java
/**
 * Crea un objeto Page de Panache con parámetros normalizados
 * 
 * @param pagina Página del cliente (base 1)
 * @param tamanio Registros por página
 * @return Page configurado para Panache
 */
public static Page crearPage(Integer pagina, Integer tamanio) {
    // Combina normalización + creación de Page
    // Page.of(0, 10) para pagina=1, tamanio=10
}

// Ejemplo
Page page = PaginacionUtil.crearPage(1, 10);      // Page.of(0, 10)
Page page = PaginacionUtil.crearPage(5, 20);      // Page.of(4, 20)
Page page = PaginacionUtil.crearPage(null, null); // Page.of(0, 10) default
```

### 6. Crear Sort Directamente

```java
/**
 * Crea un objeto Sort de Panache para ordenamiento
 * 
 * @param ordenarPor Campo para ordenar
 * @param orden Dirección "asc" o "desc"
 * @return Sort configurado
 */
public static Sort crearOrdenamiento(String ordenarPor, String orden) {
    // Valida campo + dirección
    // Retorna Sort.by(campo).ascending() o .descending()
}

// Ejemplo
Sort sort = PaginacionUtil.crearOrdenamiento("fechaCreacion", "desc");
// Retorna: Sort.by("fechaCreacion").descending()

Sort sort = PaginacionUtil.crearOrdenamiento("codigoFormulario", "asc");
// Retorna: Sort.by("codigoFormulario").ascending()
```

### 7. Convertir Página para Cliente

```java
/**
 * Convierte página de base 0 a base 1 para respuesta
 * 
 * @param paginaIndex Índice base 0 (Panache)
 * @return Página base 1 (para cliente)
 */
public static int convertirPaginaACliente(int paginaIndex) {
    // paginaIndex=0 → 1
    // paginaIndex=4 → 5
}

// Ejemplo
int paginaRespuesta = PaginacionUtil.convertirPaginaACliente(0);  // 1
int paginaRespuesta = PaginacionUtil.convertirPaginaACliente(4);  // 5
```

### 8. Calcular Total de Páginas

```java
/**
 * Calcula el total de páginas
 * 
 * @param totalElementos Total de registros
 * @param tamanio Registros por página
 * @return Total de páginas
 */
public static int calcularTotalPaginas(Long totalElementos, int tamanio) {
    // Retorna ceil(totalElementos / tamanio)
    // 150 registros / 10 = 15 páginas
}

// Ejemplo
int paginas = PaginacionUtil.calcularTotalPaginas(150L, 10);  // 15
int paginas = PaginacionUtil.calcularTotalPaginas(7L, 10);    // 1
int paginas = PaginacionUtil.calcularTotalPaginas(null, 10);  // 0
```

---

## 💡 Casos de Uso

### Caso 1: Normalizar Todos los Parámetros

```java
// Entrada del cliente
Integer pagina = 5;
Integer limite = 20;
String ordenarPor = "fechaCreacion";
String orden = "asc";

// Normalizar con utilidad
Page page = PaginacionUtil.crearPage(pagina, limite);
Sort sort = PaginacionUtil.crearOrdenamiento(ordenarPor, orden);

// Usar en Panache
repository.find(query, sort, params)
    .page(page)
    .list();
```

### Caso 2: Normalizar Parámetros Individuales

```java
// Parámetro específico
Integer paginaCliente = request.pagina;

// Normalizar solo ese parámetro
int paginaIndex = PaginacionUtil.normalizarPagina(paginaCliente);

// Usar en Page.of()
Page page = Page.of(paginaIndex, 10);
```

### Caso 3: Convertir Respuesta para Cliente

```java
// Índice interno (base 0)
int paginaIndex = 0;

// Convertir para respuesta
int paginaRespuesta = PaginacionUtil.convertirPaginaACliente(paginaIndex);  // 1

// Enviar al cliente
response.pagina = paginaRespuesta;
```

### Caso 4: Calcular Páginas Totales

```java
// Del conteo en base de datos
Long totalElementos = 150;
int tamanio = 10;

// Calcular páginas
int totalPaginas = PaginacionUtil.calcularTotalPaginas(totalElementos, tamanio);  // 15

// Enviar al cliente
response.totalPaginas = totalPaginas;
```

---

## 🔀 Uso en Diferentes Repositorios

La utilidad es **genérica y reutilizable** en cualquier repositorio:

### En FormularioRepositoryImpl:
```java
Page page = PaginacionUtil.crearPage(solicitud.pagina, solicitud.limite);
Sort sort = PaginacionUtil.crearOrdenamiento(solicitud.ordenarPor, solicitud.orden);

repository.find(query, sort, params)
    .page(page)
    .list();
```

### En PlantillaFormularioRepositoryImpl:
```java
Page page = PaginacionUtil.crearPage(solicitud.pagina, solicitud.limite);
Sort sort = PaginacionUtil.crearOrdenamiento(solicitud.ordenarPor, solicitud.orden);

repository.find(query, sort, params)
    .page(page)
    .list();
```

### En ParametroAmbienteRepositoryImpl:
```java
Page page = PaginacionUtil.crearPage(solicitud.pagina, solicitud.limite);
Sort sort = PaginacionUtil.crearOrdenamiento(solicitud.ordenarPor, solicitud.orden);

repository.find(query, sort, params)
    .page(page)
    .list();
```

---

## 📊 Tabla Resumen

| Método | Entrada | Salida | Default |
|--------|---------|--------|---------|
| `normalizarPagina()` | Integer | int | 0 |
| `normalizarTamanio()` | Integer | int | 10 |
| `normalizarOrdenarPor()` | String | String | "fechaCreacion" |
| `normalizarOrden()` | String | String | "desc" |
| `crearPage()` | Int, Int | Page | Page.of(0, 10) |
| `crearOrdenamiento()` | String, String | Sort | Sort.by("fechaCreacion").desc() |
| `convertirPaginaACliente()` | int | int | N/A |
| `calcularTotalPaginas()` | Long, int | int | 0 |

---

## ✅ Beneficios

- ✅ **Reutilizable** - Usar en todos los repositorios
- ✅ **Centralizado** - Cambios en un solo lugar
- ✅ **Consistente** - Misma lógica en todas partes
- ✅ **Testeable** - Métodos estáticos fáciles de testear
- ✅ **Mantenible** - Código limpio y claro

---

**Versión:** 1.0  
**Estado:** ✅ Listo para usar

