# Guía de Implementación - Paginación Optimizada

## Descripción General

Se ha reestructurado el servicio de listado paginado de Formularios siguiendo un patrón claro y optimizado con Panache. Esta guía explica la implementación y cómo utilizarla.

---

## 1. Estructura de Paginación en 4 Pasos

### Paso 1: Validar y Normalizar Parámetros

```java
int paginaIndex = normalizarPagina(request.pagina);      // 1 → 0 (base 0 para Panache)
int tamanio = normalizarTamanio(request.limite);         // null → 10 (default)
String ordenarPor = normalizarOrdenarPor(request.ordenarPor);
String orden = normalizarOrden(request.orden);
```

**Notas Importantes:**
- Panache usa índice **0** para la primera página
- Los clientes envían página **1** (base 1)
- Se convierte automáticamente: `pagina - 1`

### Paso 2: Crear Objetos Page y Sort

```java
Page page = Page.of(paginaIndex, tamanio);
Sort sort = crearOrdenamiento(ordenarPor, orden);
```

**Page.of(index, size):**
- `index`: Índice base 0 (página 0 = primer resultado)
- `size`: Cantidad de registros por página

**Sort:**
```java
// Ascendente
Sort.by("fechaCreacion").ascending()

// Descendente
Sort.by("fechaCreacion").descending()
```

### Paso 3: Ejecutar Consulta y Conteo en Paralelo

```java
Uni<List<Formulario>> listaUni = repository.listar(page, sort,
    request.codigoPlantillaFormulario, request.codigoUsuario,
    request.identificacionUsuario, request.buscar);

Uni<Long> totalUni = repository.contarPaginado(
    request.codigoPlantillaFormulario, request.codigoUsuario,
    request.identificacionUsuario, request.buscar);
```

**Beneficio:** Ambas consultas se ejecutan **simultáneamente**, no secuencialmente.

### Paso 4: Mapear a DTO con Metadatos

```java
return Uni.combine().all()
    .unis(listaUni, totalUni)
    .asTuple()
    .map(tuple -> construirRespuestaPaginada(
        tuple.getItem1(),    // List<Formulario>
        tuple.getItem2(),    // Long totalElementos
        paginaIndex, 
        tamanio));
```

---

## 2. Construcción de Filtros en el Repository

### Query Base Limpia

```java
String queryConditions = construirCondicionesFiltro(
    codigoPlantillaFormulario, codigoUsuario, 
    identificacionUsuario, buscar);
```

Genera:
```
eliminado = 'N' 
  AND codigoPlantillaFormulario = :codigoPlantillaFormulario
  AND LOWER(codigoUsuario) LIKE :codigoUsuario
  AND ...
```

### Parámetros Seguros

```java
Map<String, Object> params = construirParametrosFiltro(...);
```

Los parámetros se construyen con:
- Parámetros nombrados (`:codigoPlantillaFormulario`)
- Wildcards: `"%" + valor.toLowerCase() + "%"`
- Previene inyección SQL automáticamente

### Ejecución en Panache

```java
return panacheRepository.find(queryConditions, sort, params)
    .page(page)
    .list()
    .map(mapper::toDomainList);
```

**Sintaxis Panache:**
```
find(String, Sort, Map) → find(query, sort, parameters)
```

---

## 3. Cálculo de Metadatos

### Total de Páginas

```java
private int calcularTotalPaginas(Long totalElementos, int tamanio) {
    if (totalElementos == null || totalElementos == 0) {
        return 0;
    }
    return (int) Math.ceil((double) totalElementos / tamanio);
}
```

**Ejemplo:**
- Total: 150 registros
- Tamaño: 10 por página
- Páginas: ceil(150 / 10) = **15 páginas**

### Conversión de Página para Cliente

```java
response.pagina = paginaIndex + 1;  // 0 → 1 (base 1)
```

**Ejemplo:**
- Internamente: página 0 (Panache)
- Para cliente: página 1 (base 1)

---

## 4. Respuesta Paginada Completa

```json
{
  "formularios": [
    {
      "codigoFormulario": 1,
      "codigoPlantillaFormulario": 5,
      "codigoUsuario": "USR001",
      "identificacionUsuario": "1234567890",
      "estado": "Activo",
      "elementos": [...],
      "fechaCreacion": "2025-12-02T10:30:00",
      "fechaActualizacion": "2025-12-02T14:45:00"
    },
    // ... más formularios ...
  ],
  "total": 150,           // ✅ Total de registros
  "totalPaginas": 15,     // ✅ Total de páginas
  "pagina": 1,            // ✅ Página actual (base 1)
  "tamanio": 10           // ✅ Registros por página
}
```

---

## 5. Ejemplo de Uso en el Recurso REST

No requiere cambios. El recurso sigue funcionando igual:

```java
@GET
public Uni<Response> listar(
    @QueryParam("pagina") Integer pagina,           // 1
    @QueryParam("limite") Integer limite,           // 10
    @QueryParam("codigoPlantillaFormulario") Long codigoPlantillaFormulario,
    @QueryParam("codigoUsuario") String codigoUsuario,
    @QueryParam("identificacionUsuario") String identificacionUsuario,
    @QueryParam("buscar") String buscar,
    @QueryParam("ordenarPor") String ordenarPor,   // "fechaCreacion"
    @QueryParam("orden") String orden) {            // "desc"

    ConsultarFormulariosRequest request = new ConsultarFormulariosRequest();
    request.pagina = pagina;
    request.limite = limite;
    // ... resto de mapeo ...

    return service.listar(request)
        .onItem().transform(result -> Response.ok(result).build())
        .onFailure()
        .recoverWithItem(err -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
}
```

---

## 6. Normalización de Parámetros

### Página
```java
private int normalizarPagina(Integer pagina) {
    if (pagina == null || pagina < 1) {
        return 0;  // Default: primera página
    }
    return pagina - 1;  // Convertir a base 0
}
```

### Tamaño
```java
private int normalizarTamanio(Integer limite) {
    if (limite == null || limite < 1) {
        return 10;  // Default: 10 registros por página
    }
    return limite;
}
```

### Ordenamiento
```java
private String normalizarOrdenarPor(String ordenarPor) {
    if (ordenarPor == null || ordenarPor.isEmpty()) {
        return "fechaCreacion";  // Default
    }
    return ordenarPor;
}

private String normalizarOrden(String orden) {
    if (orden == null || orden.isEmpty()) {
        return "desc";  // Default: descendente
    }
    return orden;
}
```

### Sort de Panache
```java
private Sort crearOrdenamiento(String ordenarPor, String orden) {
    String campo = switch (ordenarPor) {
        case "codigoFormulario" -> "codigoFormulario";
        case "fechaActualizacion" -> "fechaActualizacion";
        default -> "fechaCreacion";
    };

    return "asc".equalsIgnoreCase(orden) 
        ? Sort.by(campo).ascending()
        : Sort.by(campo).descending();
}
```

---

## 7. Casos de Uso Comunes

### Listar Todos (Sin Filtros)

```http
GET /formulario?pagina=1&limite=10
```

### Búsqueda por Usuario

```http
GET /formulario?pagina=1&limite=10&codigoUsuario=USR001
```

### Filtro por Plantilla y Búsqueda

```http
GET /formulario?pagina=2&limite=20&codigoPlantillaFormulario=5&buscar=anexo
```

### Ordenamiento Personalizado

```http
GET /formulario?pagina=1&limite=10&ordenarPor=fechaCreacion&orden=asc
```

---

## 8. Ventajas de Esta Implementación

| Aspecto | Ventaja |
|---|---|
| **Rendimiento** | Consulta y conteo en paralelo |
| **Escalabilidad** | Metadatos completos para paginación frontend |
| **Mantenibilidad** | Código limpio, métodos pequeños y enfocados |
| **Reutilización** | Métodos helper para construir filtros |
| **Seguridad** | Parámetros nombrados previenen inyección SQL |
| **Testabilidad** | Métodos pequeños son fáciles de unit test |
| **Documentación** | Código auto-documentado con métodos claros |

---

## 9. Troubleshooting

### Problema: No aparecen metadatos en respuesta

**Causa:** El servicio usa el método antiguo sin conteo

**Solución:** Asegúrate de usar `repository.contarPaginado()` en el service

### Problema: Paginación desalineada

**Causa:** Confusión entre índice base 0 y base 1

**Solución:**
- Panache: `Page.of(0, 10)` → primera página
- Cliente: `?pagina=1` → primera página
- Service: `normalizarPagina(1)` → devuelve `0`

### Problema: Orden no aplica correctamente

**Causa:** Campo de ordenamiento no existe en la entidad

**Solución:** Validar nombres de campos en el switch de `crearOrdenamiento()`

---

## 10. Diagrama de Flujo Completo

```
┌─────────────────────────────────────────────────────┐
│ Cliente HTTP                                         │
│ GET /formulario?pagina=1&limite=10&buscar=anexo   │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│ FormularioResource.listar()                         │
│ ├─ Mapea parámetros a ConsultarFormulariosRequest  │
│ └─ Llama service.listar()                           │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│ FormularioService.listar()                          │
│                                                      │
│ 1. normalizarPagina(1) → 0                          │
│ 2. Page.of(0, 10)                                   │
│                                                      │
│ 3. Paralelo:                                        │
│    ├─ repository.listar() → List<Formulario>       │
│    └─ repository.contarPaginado() → 150            │
│                                                      │
│ 4. construirRespuestaPaginada(...)                  │
│    ├─ formularios = mapper.toResponseList()         │
│    ├─ total = 150                                   │
│    ├─ totalPaginas = 15                             │
│    ├─ pagina = 1 (base 1)                           │
│    └─ tamanio = 10                                  │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│ FormularioRepositoryImpl.listar()                    │
│                                                      │
│ queryConditions = "eliminado = 'N'                  │
│                   AND LOWER(codigoUsuario) LIKE ... │
│                                                      │
│ panacheRepository.find(queryConditions, sort, params)│
│   .page(Page.of(0, 10))                             │
│   .list()                                           │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│ Panache/Hibernate/BD                                │
│ SELECT * FROM FORMULARIO                            │
│ WHERE eliminado = 'N'                               │
│   AND LOWER(codigo_usuario) LIKE '%anexo%'         │
│ ORDER BY fecha_creacion DESC                        │
│ OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY              │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│ Respuesta JSON                                       │
│ {                                                    │
│   "formularios": [...],                             │
│   "total": 150,                                     │
│   "totalPaginas": 15,                               │
│   "pagina": 1,                                      │
│   "tamanio": 10                                     │
│ }                                                    │
└─────────────────────────────────────────────────────┘
```

---

## Resumen de Cambios

✅ **Repository:** Métodos `listar()` y `contarPaginado()` simplificados  
✅ **Service:** Estructura clara de 4 pasos  
✅ **Interface:** Cambio de firma `contar()` → `contarPaginado()`  
✅ **Resource:** Sin cambios requeridos  
✅ **DTO:** Respuesta completa con metadatos  

