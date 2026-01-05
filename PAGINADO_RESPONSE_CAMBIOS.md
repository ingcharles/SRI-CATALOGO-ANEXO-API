# Implementación de PaginadoResponse - Resumen de Cambios

## 📋 Cambios Realizados

Se ha implementado una respuesta paginada completa similar a `Page<T>` de Spring, ahora devolviendo todos los metadatos de paginación.

---

## 📁 Archivos Creados

### 1. **PaginadoResponse.java** (DTO Genérico)
**Ubicación:** `src/main/java/ec/gob/sri/api/catalogo/application/dto/`

```java
public class PaginadoResponse<T> {
    public List<T> contenido;           // Lista de elementos
    public long totalElementos;         // Total de registros en BD
    public int totalPaginas;            // Total de páginas
    public int paginaActual;            // Página actual (base 1)
    public int tamanio;                 // Registros por página
}
```

**Propósito:** DTO genérico reutilizable para cualquier tipo de lista paginada.

### 2. **ResultadoPaginado.java** (Clase Interna)
**Ubicación:** `src/main/java/ec/gob/sri/api/catalogo/infraestructure/persistence/util/`

```java
public class ResultadoPaginado<T> {
    public final List<T> contenido;
    public final long totalElementos;
    public final int totalPaginas;
    public final int paginaActual;
    public final int tamanio;
}
```

**Propósito:** Encapsular metadatos de paginación en la capa de persistencia.

---

## 🔄 Archivos Modificados

### 1. **FormularioRepository.java** (Interfaz)

**Antes:**
```java
Uni<List<Formulario>> listar(Page page, Sort sort, ...);
```

**Después:**
```java
Uni<ResultadoPaginado<Formulario>> listar(Page page, Sort sort, ...);
```

**Cambios:**
- Ahora devuelve `ResultadoPaginado` con metadatos completos
- Importa `ResultadoPaginado` de la utilidad

---

### 2. **FormularioRepositoryImpl.java** (Implementación)

**Antes:**
```java
return panacheRepository.find(condicionesFiltro, sort, parametros)
    .page(page)
    .list()
    .map(mapper::toDomainList);
```

**Después:**
```java
var consulta = panacheRepository.find(condicionesFiltro, sort, parametros)
    .page(page);

Uni<List<FormularioEntity>> listaUni = consulta.list();
Uni<Long> totalUni = consulta.count();
Uni<Integer> totalPaginasUni = consulta.pageCount();

return Uni.combine().all()
    .unis(listaUni, totalUni, totalPaginasUni)
    .asTuple()
    .map(tuple -> {
        List<FormularioEntity> lista = tuple.getItem1();
        long total = tuple.getItem2();
        int totalPaginas = tuple.getItem3();

        List<Formulario> contenido = mapper.toDomainList(lista);
        int paginaActual = page.index + 1;
        int tamanio = page.size;

        return new ResultadoPaginado<>(contenido, total, totalPaginas,
            paginaActual, tamanio);
    });
```

**Cambios:**
- Obtiene lista, total de elementos y total de páginas en paralelo
- Combina resultados con `Uni.combine()`
- Retorna `ResultadoPaginado` con metadatos completos

---

### 3. **FormularioService.java** (Servicio)

**Antes:**
```java
public Uni<ConsultarFormulariosResponse> listar(ConsultarFormulariosRequest solicitud) {
    Page pagina = PaginacionUtil.crearPage(solicitud.pagina, solicitud.limite);
    Sort ordenamiento = PaginacionUtil.crearOrdenamiento(solicitud.ordenarPor, solicitud.orden);

    Uni<List<Formulario>> listaUni = repository.listar(pagina, ordenamiento, ...);

    return listaUni.map(lista -> {
        ConsultarFormulariosResponse respuesta = new ConsultarFormulariosResponse();
        respuesta.formularios = mapper.toResponseList(lista);
        respuesta.pagina = PaginacionUtil.normalizarPagina(solicitud.pagina) + 1;
        respuesta.tamanio = PaginacionUtil.normalizarTamanio(solicitud.limite);
        return respuesta;
    });
}
```

**Después:**
```java
public Uni<PaginadoResponse<FormularioResponse>> listar(ConsultarFormulariosRequest solicitud) {
    Page pagina = PaginacionUtil.crearPage(solicitud.pagina, solicitud.limite);
    Sort ordenamiento = PaginacionUtil.crearOrdenamiento(solicitud.ordenarPor, solicitud.orden);

    Uni<ResultadoPaginado<Formulario>> resultadoUni = repository.listar(pagina, ordenamiento, ...);

    return resultadoUni.map(resultado -> {
        List<FormularioResponse> contenidoFormularios = mapper.toResponseList(resultado.contenido);
        
        return new PaginadoResponse<>(
            contenidoFormularios,
            resultado.totalElementos,
            resultado.totalPaginas,
            resultado.paginaActual,
            resultado.tamanio
        );
    });
}
```

**Cambios:**
- Cambio de tipo de retorno: `ConsultarFormulariosResponse` → `PaginadoResponse<FormularioResponse>`
- Usa `ResultadoPaginado` del repositorio
- Devuelve metadata completa: `totalElementos`, `totalPaginas`, `paginaActual`, `tamanio`

---

## 📊 Flujo de Datos

```
Cliente
   ↓
Controller (ConsultarFormulariosRequest)
   ↓
FormularioService.listar()
   ├─ Normaliza parámetros (PaginacionUtil)
   ├─ Llama a FormularioRepository.listar()
   │
   └─ FormularioRepositoryImpl.listar()
      ├─ Obtiene: List<Formulario>
      ├─ Obtiene: Long totalElementos (count())
      ├─ Obtiene: Int totalPaginas (pageCount())
      └─ Retorna: ResultadoPaginado<Formulario>
   
   └─ Mapea a: PaginadoResponse<FormularioResponse>
         ├─ contenido: List<FormularioResponse>
         ├─ totalElementos: Long
         ├─ totalPaginas: Int
         ├─ paginaActual: Int (base 1)
         └─ tamanio: Int
   ↓
Cliente (JSON)
```

---

## 🎯 Ventajas

✅ **Metadatos Completos** - Total de elementos y páginas en cada respuesta  
✅ **Similar a Spring** - Mismo patrón que `Page<T>` de Spring Data  
✅ **Genérico** - `PaginadoResponse<T>` reutilizable para cualquier DTO  
✅ **Eficiente** - Usa `Uni.combine()` para obtener metadatos en paralelo  
✅ **Limpio** - Responsabilidades claras: DTO vs Clase Interna vs Servicio  

---

## 📝 Respuesta JSON de Ejemplo

```json
{
  "contenido": [
    {
      "codigoFormulario": 1,
      "codigoPlantillaFormulario": 10,
      "codigoUsuario": "usuario123",
      "identificacionUsuario": "1234567890",
      "elementos": "{...}",
      "estado": "A"
    },
    {
      "codigoFormulario": 2,
      "codigoPlantillaFormulario": 10,
      "codigoUsuario": "usuario456",
      "identificacionUsuario": "0987654321",
      "elementos": "{...}",
      "estado": "A"
    }
  ],
  "totalElementos": 150,
  "totalPaginas": 15,
  "paginaActual": 1,
  "tamanio": 10
}
```

---

## ✅ Verificación

- ✅ FormularioService.java - **Sin errores**
- ✅ FormularioRepositoryImpl.java - **Sin errores**
- ✅ FormularioRepository.java - **Sin errores**
- ✅ PaginadoResponse.java - **Sin errores**
- ✅ ResultadoPaginado.java - **Sin errores**

---

**Estado:** ✅ Implementación Completa

