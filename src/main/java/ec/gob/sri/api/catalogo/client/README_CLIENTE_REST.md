# Cliente REST - Grupo por Integrante

## 📋 Descripción

Cliente REST implementado con Quarkus siguiendo arquitectura hexagonal/DDD para consumir servicios externos de gestión de grupos de trabajo.

## 🏗️ Arquitectura

La implementación sigue el patrón de **Arquitectura Hexagonal (Puertos y Adaptadores)** con DDD:

```
📦 Estructura del Cliente REST
├── 📂 application/dto/client/           # DTOs compartidos
│   ├── GrupoPorIntegranteDTO.java
│   └── GrupoPorIntegranteClavePrimariaDTO.java
├── 📂 domain/repository/                # Puerto (Interfaz del dominio)
│   └── GrupoPorIntegranteClient.java
├── 📂 infraestructure/client/          # Adaptadores
│   ├── GrupoPorIntegranteRestClient.java (REST Client Interface)
│   └── impl/
│       └── GrupoPorIntegranteClientImpl.java (Implementación)
├── 📂 application/service/              # Servicios de aplicación
│   └── GestionarGrupoTrabajoService.java
└── 📂 infraestructure/rest/            # Endpoints REST (opcional)
    └── GrupoTrabajoResource.java
```

## 🔧 Componentes

### 1. DTOs (Data Transfer Objects)

**GrupoPorIntegranteClavePrimariaDTO**

- Representa la clave primaria compuesta
- Contiene: codigoGrupoTrabajo, codigoUsuarioAdministrador, codigoUsuario

**GrupoPorIntegranteDTO**

- DTO principal con toda la información del grupo
- Incluye datos del grupo, administrador, integrante, porcentajes de asignación

### 2. Puerto (Domain Layer)

**GrupoPorIntegranteClient** (Interface)

- Define el contrato para obtener información de grupos
- Pertenece a la capa de dominio
- Independiente de la implementación técnica

### 3. Adaptador REST Client (Infrastructure Layer)

**GrupoPorIntegranteRestClient**

- Interfaz anotada con `@RegisterRestClient`
- Define los endpoints del servicio externo
- Usa MicroProfile REST Client

**GrupoPorIntegranteClientImpl**

- Implementa el puerto `GrupoPorIntegranteClient`
- Delega al `GrupoPorIntegranteRestClient`
- Incluye logging y manejo de errores

### 4. Servicio de Aplicación

**GestionarGrupoTrabajoService**

- Orquesta operaciones de negocio
- Usa el puerto `GrupoPorIntegranteClient`
- Valida datos de entrada

### 5. Recurso REST (opcional)

**GrupoTrabajoResource**

- Expone endpoint REST local
- Consume el servicio de aplicación
- Documentado con OpenAPI

## ⚙️ Configuración

### application.properties

```properties
# REST Client - Grupo por Integrante
grupo-por-integrante-rest/mp-rest/url=${GRUPO_INTEGRANTE_URL:http://localhost:8080/sri-grupo-trabajo-fachada/rest}
grupo-por-integrante-rest/mp-rest/scope=javax.inject.Singleton

# Perfil desarrollo
%dev.grupo-por-integrante-rest/mp-rest/url=http://localhost:8080/sri-grupo-trabajo-fachada/rest

# Perfil test
%test.grupo-por-integrante-rest/mp-rest/url=http://localhost:8080/sri-grupo-trabajo-fachada/rest
```

### Variables de Entorno

```bash
# Producción
GRUPO_INTEGRANTE_URL=http://servidor-produccion:8080/sri-grupo-trabajo-fachada/rest
```

## 💻 Uso

### Opción 1: Inyección directa del Cliente (Recomendado en Servicios)

```java
@ApplicationScoped
public class MiServicio {

    @Inject
    GrupoPorIntegranteClient grupoPorIntegranteClient;

    public void procesarGrupo(String codigoUsuario) {
        GrupoPorIntegranteDTO grupo = grupoPorIntegranteClient
            .obtenerGruposPorCodigoUsuario(codigoUsuario);

        // Procesar grupo...
    }
}
```

### Opción 2: Usar el Servicio de Aplicación

```java
@Inject
GestionarGrupoTrabajoService grupoTrabajoService;

public void ejemplo() {
    GrupoPorIntegranteDTO grupo = grupoTrabajoService
        .obtenerGruposPorUsuario("USUARIO123");

    System.out.println("Grupo: " + grupo.getNombreGrupoTrabajo());
}
```

### Opción 3: Consumir el Endpoint REST Local

```bash
# GET request al endpoint LOCAL de esta aplicación
curl -X GET "http://localhost:8680/catalogo-anexo/v1.0/grupoTrabajo/codigoUsuario/{codigoUsuario}?codigoUsuario=USUARIO123"

# Nota: Este endpoint LOCAL consume internamente el servicio EXTERNO:
# http://servidor:8080/sri-grupo-trabajo-fachada/rest/grupoPorIntegrante/codigoUsuario/{codigoUsuario}?codigoUsuario=USUARIO123
```

## 🔍 Testing

### Ejemplo de Test Unitario

```java
@QuarkusTest
class GrupoPorIntegranteClientImplTest {

    @Inject
    GrupoPorIntegranteClient client;

    @Test
    void testObtenerGrupos() {
        GrupoPorIntegranteDTO resultado = client
            .obtenerGruposPorCodigoUsuario("TEST123");

        assertNotNull(resultado);
        assertEquals("Grupo Test", resultado.getNombreGrupoTrabajo());
    }
}
```

## 🎯 Ventajas de esta Arquitectura

1. **Separación de responsabilidades**: Cada capa tiene una responsabilidad clara
2. **Testabilidad**: Fácil crear mocks del puerto para testing
3. **Independencia**: El dominio no depende de detalles de infraestructura
4. **Flexibilidad**: Puedes cambiar la implementación del cliente sin afectar el dominio
5. **Mantenibilidad**: Código organizado y fácil de mantener

## 📚 Referencias

- [Quarkus REST Client](https://quarkus.io/guides/rest-client)
- [MicroProfile REST Client](https://github.com/eclipse/microprofile-rest-client)
- [Arquitectura Hexagonal](https://alistair.cockburn.us/hexagonal-architecture/)
