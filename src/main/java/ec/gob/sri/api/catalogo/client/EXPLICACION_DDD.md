# 🎯 Explicación: Diferencia entre Cliente REST y Endpoint Local en DDD

## ❓ ¿Por qué hay dos @Path diferentes?

### Es CORRECTO tener paths diferentes porque son componentes distintos:

## 1️⃣ GrupoPorIntegranteRestClient (Adaptador SALIENTE)

**Archivo**: `infraestructure/client/GrupoPorIntegranteRestClient.java`

```java
@Path("/grupoPorIntegrante")
@RegisterRestClient(configKey = "grupo-por-integrante-rest")
public interface GrupoPorIntegranteRestClient {

    @GET
    @Path("/codigoUsuario/{codigoUsuario}")
    GrupoPorIntegranteDTO listarGruposPorCodigoUsuario(
        @QueryParam("codigoUsuario") String codigoUsuario);
}
```

**Propósito**: Consumir servicio EXTERNO
**Dirección**: Tu app → Servicio externo (SALIENTE)
**URL generada**:

```
http://servidor-externo:8080/sri-grupo-trabajo-fachada/rest/grupoPorIntegrante/codigoUsuario/{codigoUsuario}?codigoUsuario=MDCHAVEZ
```

**Configuración** (application.properties):

```properties
grupo-por-integrante-rest/mp-rest/url=http://localhost:8080/sri-grupo-trabajo-fachada/rest
```

---

## 2️⃣ GrupoTrabajoResource (Adaptador ENTRANTE)

**Archivo**: `infraestructure/rest/GrupoTrabajoResource.java`

```java
@Path("/grupoTrabajo")
public class GrupoTrabajoResource {

    @GET
    @Path("/codigoUsuario/{codigoUsuario}")
    public Response obtenerGruposPorUsuario(
        @QueryParam("codigoUsuario") String codigoUsuario) {
        // ...
    }
}
```

**Propósito**: Exponer funcionalidad LOCAL
**Dirección**: Cliente externo → Tu app (ENTRANTE)
**URL expuesta**:

```
http://localhost:8680/catalogo-anexo/v1.0/grupoTrabajo/codigoUsuario/{codigoUsuario}?codigoUsuario=MDCHAVEZ
```

**Configuración** (application.properties):

```properties
quarkus.http.root-path=/catalogo-anexo/v1.0
```

---

## 🔄 Flujo Completo en DDD

```
┌──────────────┐
│   Cliente    │ (navegador, Postman, otra app)
│   Externo    │
└──────┬───────┘
       │ HTTP GET
       │ http://localhost:8680/catalogo-anexo/v1.0/grupoTrabajo/codigoUsuario/{codigoUsuario}?codigoUsuario=MDCHAVEZ
       ▼
┌──────────────────────────────────────────────────────────┐
│  TU APLICACIÓN (sri-catalogo-anexo-api)                 │
│                                                           │
│  ┌─────────────────────────────────────────────────┐    │
│  │ CAPA PRESENTACIÓN (Adaptador ENTRANTE)          │    │
│  │ GrupoTrabajoResource                            │    │
│  │ @Path("/grupoTrabajo")                        │    │
│  └────────────────┬────────────────────────────────┘    │
│                   │                                       │
│                   ▼                                       │
│  ┌─────────────────────────────────────────────────┐    │
│  │ CAPA APLICACIÓN                                 │    │
│  │ GestionarGrupoTrabajoService                    │    │
│  │ • Validaciones de negocio                       │    │
│  │ • Orquestación                                  │    │
│  └────────────────┬────────────────────────────────┘    │
│                   │                                       │
│                   ▼ Usa Puerto (Interface)               │
│  ┌─────────────────────────────────────────────────┐    │
│  │ CAPA DOMINIO                                    │    │
│  │ GrupoPorIntegranteClient (Interface/Puerto)     │    │
│  └────────────────┬────────────────────────────────┘    │
│                   │                                       │
│                   ▼ Implementación                        │
│  ┌─────────────────────────────────────────────────┐    │
│  │ CAPA INFRAESTRUCTURA (Adaptador SALIENTE)       │    │
│  │ GrupoPorIntegranteClientImpl                    │    │
│  │ ├─ Logging                                      │    │
│  │ ├─ Manejo de errores                            │    │
│  │ └─ Delega a RestClient                          │    │
│  │     │                                            │    │
│  │     ▼                                            │    │
│  │ GrupoPorIntegranteRestClient                    │    │
│  │ @RegisterRestClient                             │    │
│  │ @Path("/grupoPorIntegrante")                  │    │
│  └─────────────────┬───────────────────────────────┘    │
│                    │                                      │
└────────────────────┼──────────────────────────────────────┘
                     │ HTTP GET
                     │ http://servidor-externo:8080/sri-grupo-trabajo-fachada/rest/
                     │ grupo-por-integrante/codigoUsuario/{codigoUsuario}?codigoUsuario=MDCHAVEZ
                     ▼
            ┌────────────────────┐
            │ SERVICIO EXTERNO   │
            │ sri-grupo-trabajo  │
            │     -fachada       │
            └────────────────────┘
```

---

## ✅ Resumen: ¿Está Duplicado?

**NO**, no está duplicado. Son dos paths diferentes para dos propósitos diferentes:

| Componente                       | Path                                                | Propósito                 | Dirección    |
| -------------------------------- | --------------------------------------------------- | ------------------------- | ------------ |
| **GrupoPorIntegranteRestClient** | `/grupoPorIntegrante/codigoUsuario/{codigoUsuario}` | Consumir servicio EXTERNO | SALIENTE (→) |
| **GrupoTrabajoResource**         | `/grupoTrabajo/codigoUsuario/{codigoUsuario}`       | Exponer API LOCAL         | ENTRANTE (←) |

---

## 🎓 Principios DDD Aplicados

1. **Separación de Responsabilidades**:
   - El RestClient solo sabe cómo consumir el servicio externo
   - El Resource solo sabe cómo exponer la funcionalidad local

2. **Inversión de Dependencias**:
   - El dominio define el puerto (GrupoPorIntegranteClient)
   - La infraestructura implementa el adaptador (GrupoPorIntegranteClientImpl)

3. **Adaptadores**:
   - **Adaptador ENTRANTE**: GrupoTrabajoResource (recibe peticiones)
   - **Adaptador SALIENTE**: GrupoPorIntegranteRestClient (hace peticiones)

---

## 🔍 Ejemplo Real

Cuando alguien hace esta petición:

```bash
curl http://localhost:8680/catalogo-anexo/v1.0/grupoTrabajo/codigoUsuario/{codigoUsuario}?codigoUsuario=MDCHAVEZ
```

Internamente tu aplicación hace esta petición al servicio externo:

```bash
curl http://servidor-externo:8080/sri-grupo-trabajo-fachada/rest/grupoPorIntegrante/codigoUsuario/{codigoUsuario}?codigoUsuario=MDCHAVEZ
```

**¡Son URLs completamente diferentes porque son servicios diferentes!**

---

## ⚙️ Configuración Opción B (Query Parameter)

**GrupoPorIntegranteRestClient** (Correcto ✅):

```java
@GET
@Path("/codigoUsuario/{codigoUsuario}")
GrupoPorIntegranteDTO listarGruposPorCodigoUsuario(
    @QueryParam("codigoUsuario") String codigoUsuario);
```

**NO usar** (Opción A - Path Parameter):

```java
@GET
@Path("/{codigoUsuario}")  // ❌ Incorrecto para tu caso
GrupoPorIntegranteDTO listarGruposPorCodigoUsuario(
    @PathParam("codigoUsuario") String codigoUsuario);
```
