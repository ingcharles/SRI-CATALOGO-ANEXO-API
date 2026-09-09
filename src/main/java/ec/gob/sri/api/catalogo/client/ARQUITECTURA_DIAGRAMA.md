# 🏗️ Arquitectura del Cliente REST - Diagrama Visual

## Flujo de Datos - Arquitectura Hexagonal

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          SERVICIO EXTERNO                                │
│                                                                           │
│   http://servidor:8080/sri-grupo-trabajo-fachada/rest/grupoPorIntegrante│
└──────────────────────────────────▲──────────────────────────────────────┘
                                   │
                                   │ HTTP Request
                                   │
┌──────────────────────────────────┴──────────────────────────────────────┐
│                      CAPA DE INFRAESTRUCTURA                             │
│                         (Adaptadores)                                    │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  GrupoPorIntegranteRestClient                                    │   │
│  │  @RegisterRestClient                                             │   │
│  │  • Interface anotada con JAX-RS                                  │   │
│  │  • Define endpoints del servicio externo                         │   │
│  │  • Serialización/Deserialización JSON                            │   │
│  └────────────────────────────▲────────────────────────────────────┘   │
│                                │                                          │
│                                │ Inyección @RestClient                   │
│                                │                                          │
│  ┌────────────────────────────┴────────────────────────────────────┐   │
│  │  GrupoPorIntegranteClientImpl                                    │   │
│  │  @ApplicationScoped                                              │   │
│  │  implements GrupoPorIntegranteClient (Puerto)                    │   │
│  │                                                                   │   │
│  │  + obtenerGruposPorCodigoUsuario(String): DTO                    │   │
│  │  • Logging                                                        │   │
│  │  • Manejo de errores                                              │   │
│  │  • Delega al RestClient                                           │   │
│  └──────────────────────────────────────────────────────────────────┘   │
└──────────────────────────────────▲──────────────────────────────────────┘
                                   │
                                   │ Implementa Puerto
                                   │
┌──────────────────────────────────┴──────────────────────────────────────┐
│                         CAPA DE DOMINIO                                  │
│                        (Lógica de Negocio)                               │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  <<interface>>                                                   │   │
│  │  GrupoPorIntegranteClient                                        │   │
│  │  • Puerto (Contrato del dominio)                                 │   │
│  │  • Independiente de implementación técnica                       │   │
│  │                                                                   │   │
│  │  + obtenerGruposPorCodigoUsuario(String): DTO                    │   │
│  └──────────────────────────────────────────────────────────────────┘   │
└──────────────────────────────────▲──────────────────────────────────────┘
                                   │
                                   │ Usa Puerto
                                   │
┌──────────────────────────────────┴──────────────────────────────────────┐
│                      CAPA DE APLICACIÓN                                  │
│                      (Casos de Uso)                                      │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  GestionarGrupoTrabajoService                                    │   │
│  │  @ApplicationScoped                                              │   │
│  │                                                                   │   │
│  │  @Inject GrupoPorIntegranteClient                                │   │
│  │                                                                   │   │
│  │  + obtenerGruposPorUsuario(String): DTO                          │   │
│  │  • Validaciones de negocio                                        │   │
│  │  • Orquestación de operaciones                                    │   │
│  │  • Logging de negocio                                             │   │
│  └──────────────────────────────────────────────────────────────────┘   │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  DTOs (Data Transfer Objects)                                    │   │
│  │  • GrupoPorIntegranteDTO                                         │   │
│  │  • GrupoPorIntegranteClavePrimariaDTO                            │   │
│  └──────────────────────────────────────────────────────────────────┘   │
└──────────────────────────────────▲──────────────────────────────────────┘
                                   │
                                   │ Consume
                                   │
┌──────────────────────────────────┴──────────────────────────────────────┐
│                    CAPA DE PRESENTACIÓN (REST)                           │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  GrupoTrabajoResource                                            │   │
│  │  @Path("/grupoTrabajo")                                        │   │
│  │                                                                   │   │
│  │  @Inject GestionarGrupoTrabajoService                            │   │
│  │                                                                   │   │
│  │  GET /codigoUsuario/{codigoUsuario}?codigoUsuario={codigo}                         │   │
│  │  • Validación HTTP                                                │   │
│  │  • Transformación Request/Response                                │   │
│  │  • Manejo de errores HTTP                                         │   │
│  │  • Documentación OpenAPI                                          │   │
│  └──────────────────────────────────────────────────────────────────┘   │
└──────────────────────────────────▲──────────────────────────────────────┘
                                   │
                                   │ HTTP Request
                                   │
                              ┌────┴─────┐
                              │  CLIENTE │
                              │  (User)  │
                              └──────────┘
```

## 📂 Estructura de Carpetas

```
src/main/java/ec/gob/sri/api/catalogo/
│
├── 📂 application/                      # CAPA DE APLICACIÓN
│   ├── 📂 dto/
│   │   └── 📂 client/                   # DTOs compartidos
│   │       ├── GrupoPorIntegranteDTO.java
│   │       └── GrupoPorIntegranteClavePrimariaDTO.java
│   │
│   └── 📂 service/                      # Servicios de aplicación
│       └── GestionarGrupoTrabajoService.java
│
├── 📂 domain/                           # CAPA DE DOMINIO
│   └── 📂 repository/                   # Puertos
│       └── GrupoPorIntegranteClient.java (Interface)
│
├── 📂 infraestructure/                  # CAPA DE INFRAESTRUCTURA
│   ├── 📂 client/                       # Adaptadores de cliente
│   │   ├── GrupoPorIntegranteRestClient.java (Interface REST)
│   │   └── 📂 impl/
│   │       └── GrupoPorIntegranteClientImpl.java
│   │
│   └── 📂 rest/                         # Endpoints REST
│       └── GrupoTrabajoResource.java
│
└── 📂 shared/                           # COMPARTIDO
    └── 📂 exception/
```

## 🔄 Flujo de Ejecución Paso a Paso

### Escenario 1: Cliente externo consume endpoint local

```
1. Cliente HTTP → GET /catalogo-anexo/v1.0/grupoTrabajo/codigoUsuario/{codigoUsuario}?codigoUsuario=USER123

2. GrupoTrabajoResource
   ├─ Recibe request HTTP
   ├─ Valida parámetros
   └─ Llama → gestionarGrupoTrabajoService.obtenerGruposPorUsuario("USER123")

3. GestionarGrupoTrabajoService
   ├─ Valida reglas de negocio
   ├─ Log de negocio
   └─ Llama → grupoPorIntegranteClient.obtenerGruposPorCodigoUsuario("USER123")
      (Usa el PUERTO - interface del dominio)

4. GrupoPorIntegranteClientImpl (Implementación del puerto)
   ├─ Log de infraestructura
   ├─ Manejo de errores
   └─ Delega → grupoPorIntegranteRestClient.listarGruposPorCodigoUsuario("USER123")
      (Inyecta @RestClient)

5. GrupoPorIntegranteRestClient (MicroProfile REST Client)
   ├─ Construye request HTTP
   ├─ Serializa parámetros
   ├─ Llama → Servicio Externo
   │   http://servidor:8080/sri-grupo-trabajo-fachada/rest/grupoPorIntegrante/{id}?codigoUsuario=USER123
   ├─ Recibe respuesta JSON
   └─ Deserializa → GrupoPorIntegranteDTO

6. Respuesta viaja de vuelta por las capas
   └─ RestClient → ClientImpl → Service → Resource → Cliente HTTP (200 OK + JSON)
```

### Escenario 2: Servicio interno consume directamente

```
1. OtroServicio
   └─ @Inject GrupoPorIntegranteClient

2. OtroServicio.metodo()
   └─ grupoPorIntegranteClient.obtenerGruposPorCodigoUsuario("USER123")
      (Usa directamente el PUERTO)

3. [Continúa desde paso 4 del Escenario 1]
```

## 🎯 Ventajas de esta Arquitectura

| Característica                | Beneficio                                       |
| ----------------------------- | ----------------------------------------------- |
| **Inversión de Dependencias** | El dominio no depende de detalles técnicos      |
| **Testabilidad**              | Fácil crear mocks del puerto para tests         |
| **Mantenibilidad**            | Cambios en infraestructura no afectan dominio   |
| **Reutilización**             | El puerto puede usarse en múltiples servicios   |
| **Flexibilidad**              | Fácil cambiar implementación (REST → gRPC, etc) |

## 📝 Principios SOLID Aplicados

- **S** - Single Responsibility: Cada clase tiene una responsabilidad única
- **O** - Open/Closed: Abierto a extensión (nuevas implementaciones del puerto)
- **L** - Liskov Substitution: Cualquier implementación del puerto es intercambiable
- **I** - Interface Segregation: Interfaces pequeñas y específicas
- **D** - Dependency Inversion: Dependemos de abstracciones (puerto), no de implementaciones
