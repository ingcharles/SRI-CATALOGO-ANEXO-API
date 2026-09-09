# 🚀 Cliente REST - Configuraciones Avanzadas y Mejores Prácticas

## 📋 Tabla de Contenido

1. [Configuraciones Avanzadas](#configuraciones-avanzadas)
2. [Manejo de Errores](#manejo-de-errores)
3. [Timeouts y Reintentos](#timeouts-y-reintentos)
4. [Seguridad y Autenticación](#seguridad-y-autenticación)
5. [Mejores Prácticas](#mejores-prácticas)
6. [Troubleshooting](#troubleshooting)

---

## Configuraciones Avanzadas

### 1. Configuración Completa en application.properties

```properties
# URL del servicio
grupo-por-integrante-rest/mp-rest/url=${GRUPO_INTEGRANTE_URL:http://localhost:8080/sri-grupo-trabajo-fachada/rest}
grupo-por-integrante-rest/mp-rest/scope=javax.inject.Singleton

# Timeouts (en milisegundos)
grupo-por-integrante-rest/mp-rest/connectTimeout=5000
grupo-por-integrante-rest/mp-rest/readTimeout=10000

# Configuración de seguimiento y logs
grupo-por-integrante-rest/mp-rest/providers=io.quarkus.rest.client.reactive.logging.DefaultClientLogger

# Configuración de proxy (si aplica)
#grupo-por-integrante-rest/mp-rest/proxyAddress=proxy.empresa.com:8080

# Configuración por ambiente
%dev.grupo-por-integrante-rest/mp-rest/url=http://localhost:8080/sri-grupo-trabajo-fachada/rest
%dev.grupo-por-integrante-rest/mp-rest/connectTimeout=10000
%dev.grupo-por-integrante-rest/mp-rest/readTimeout=20000

%test.grupo-por-integrante-rest/mp-rest/url=http://localhost:8080/sri-grupo-trabajo-fachada/rest

%prod.grupo-por-integrante-rest/mp-rest/url=${GRUPO_INTEGRANTE_URL}
%prod.grupo-por-integrante-rest/mp-rest/connectTimeout=3000
%prod.grupo-por-integrante-rest/mp-rest/readTimeout=8000

# Habilitar logging HTTP
quarkus.log.category."org.jboss.resteasy.reactive.client.logging".level=DEBUG
```

### 2. Configuración con SSL/TLS

```properties
# Configuración SSL
grupo-por-integrante-rest/mp-rest/trustStore=/path/to/truststore.jks
grupo-por-integrante-rest/mp-rest/trustStorePassword=${TRUSTSTORE_PASSWORD}
grupo-por-integrante-rest/mp-rest/keyStore=/path/to/keystore.jks
grupo-por-integrante-rest/mp-rest/keyStorePassword=${KEYSTORE_PASSWORD}

# Verificación de hostname
grupo-por-integrante-rest/mp-rest/hostnameVerifier=io.quarkus.restclient.NoopHostnameVerifier
```

---

## Manejo de Errores

### 1. Exception Mapper Personalizado

```java
package ec.gob.sri.api.catalogo.infraestructure.client.mapper;

import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrupoIntegranteExceptionMapper implements ResponseExceptionMapper<RuntimeException> {

    private static final Logger logger = LoggerFactory.getLogger(GrupoIntegranteExceptionMapper.class);

    @Override
    public RuntimeException toThrowable(Response response) {
        int status = response.getStatus();
        String message = response.readEntity(String.class);

        logger.error("Error en llamada REST: Status={}, Message={}", status, message);

        return switch (status) {
            case 404 -> new GrupoNoEncontradoException("Grupo no encontrado para el usuario");
            case 400 -> new IllegalArgumentException("Solicitud inválida: " + message);
            case 401, 403 -> new SecurityException("Error de autenticación/autorización");
            case 500, 502, 503 -> new ServiceUnavailableException("Servicio no disponible");
            default -> new RuntimeException("Error inesperado: " + status);
        };
    }
}
```

**Uso en el RestClient:**

```java
@RegisterRestClient(configKey = "grupo-por-integrante-rest")
@RegisterProvider(GrupoIntegranteExceptionMapper.class)
public interface GrupoPorIntegranteRestClient {
    // ...
}
```

### 2. Excepciones Personalizadas

```java
package ec.gob.sri.api.catalogo.shared.exception;

public class GrupoNoEncontradoException extends RuntimeException {
    public GrupoNoEncontradoException(String message) {
        super(message);
    }
}

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
```

---

## Timeouts y Reintentos

### 1. Configuración de Reintentos (Fault Tolerance)

**Agregar dependencia en pom.xml:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-fault-tolerance</artifactId>
</dependency>
```

**Implementar reintentos:**

```java
package ec.gob.sri.api.catalogo.infraestructure.client.impl;

import io.smallrye.faulttolerance.api.CircuitBreakerName;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.faulttolerance.*;

import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class GrupoPorIntegranteClientImpl implements GrupoPorIntegranteClient {

    @Inject
    @RestClient
    GrupoPorIntegranteRestClient grupoPorIntegranteRestClient;

    @Override
    @Retry(
        maxRetries = 3,
        delay = 1000,
        delayUnit = ChronoUnit.MILLIS,
        jitter = 200
    )
    @Timeout(value = 5000, unit = ChronoUnit.MILLIS)
    @Fallback(fallbackMethod = "obtenerGruposPorCodigoUsuarioFallback")
    @CircuitBreaker(
        requestVolumeThreshold = 4,
        failureRatio = 0.5,
        delay = 10000,
        delayUnit = ChronoUnit.MILLIS
    )
    @CircuitBreakerName("grupo-integrante-service")
    public GrupoPorIntegranteDTO obtenerGruposPorCodigoUsuario(String codigoUsuario) {
        logger.info("Consultando grupos para el usuario: {}", codigoUsuario);

        try {
            return grupoPorIntegranteRestClient.listarGruposPorCodigoUsuario(codigoUsuario);
        } catch (Exception e) {
            logger.error("Error al consultar grupos: {}", codigoUsuario, e);
            throw e;
        }
    }

    // Método fallback cuando falla el servicio
    private GrupoPorIntegranteDTO obtenerGruposPorCodigoUsuarioFallback(String codigoUsuario) {
        logger.warn("Ejecutando fallback para usuario: {}", codigoUsuario);

        // Retornar datos por defecto o desde caché
        GrupoPorIntegranteDTO fallbackDTO = new GrupoPorIntegranteDTO();
        fallbackDTO.setNombreGrupoTrabajo("Grupo No Disponible");
        fallbackDTO.setEstadoGrupo("DESCONOCIDO");

        return fallbackDTO;
    }
}
```

### 2. Configuración de Fault Tolerance

```properties
# Configuración de Circuit Breaker
grupo-integrante-service/CircuitBreaker/enabled=true
grupo-integrante-service/CircuitBreaker/requestVolumeThreshold=4
grupo-integrante-service/CircuitBreaker/failureRatio=0.5
grupo-integrante-service/CircuitBreaker/delay=10000

# Configuración de Retry
mp.fault.tolerance.retry.maxRetries=3
mp.fault.tolerance.retry.delay=1000

# Configuración de Timeout
mp.fault.tolerance.timeout.value=5000
```

---

## Seguridad y Autenticación

### 1. Propagación de Token OIDC

Ya está incluido con `quarkus-rest-client-oidc-token-propagation`

**Configuración:**

```properties
# Propagar token automáticamente
grupo-por-integrante-rest/mp-rest/providers=io.quarkus.oidc.token.propagation.AccessTokenRequestFilter
```

**O usando anotación:**

```java
@RegisterRestClient(configKey = "grupo-por-integrante-rest")
@RegisterProvider(AccessTokenRequestFilter.class)
public interface GrupoPorIntegranteRestClient {
    // El token se propagará automáticamente
}
```

### 2. Autenticación Basic

```java
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

public class BasicAuthHeaderFactory implements ClientHeadersFactory {

    @Override
    public MultivaluedMap<String, String> update(
            MultivaluedMap<String, String> incomingHeaders,
            MultivaluedMap<String, String> clientOutgoingHeaders) {

        String credentials = "username:password";
        String encodedCredentials = Base64.getEncoder()
            .encodeToString(credentials.getBytes());

        clientOutgoingHeaders.putSingle("Authorization",
            "Basic " + encodedCredentials);

        return clientOutgoingHeaders;
    }
}
```

**Registrar en el cliente:**

```java
@RegisterRestClient(configKey = "grupo-por-integrante-rest")
@RegisterClientHeaders(BasicAuthHeaderFactory.class)
public interface GrupoPorIntegranteRestClient {
    // ...
}
```

---

## Mejores Prácticas

### ✅ DO (Hacer)

1. **Usar Puerto (Interface) en Servicios**

   ```java
   @Inject
   GrupoPorIntegranteClient client; // ✅ Correcto (Puerto)
   ```

2. **Validar Datos de Entrada**

   ```java
   if (codigoUsuario == null || codigoUsuario.trim().isEmpty()) {
       throw new IllegalArgumentException("Código requerido");
   }
   ```

3. **Logging Apropiado**

   ```java
   logger.info("Consultando usuario: {}", codigoUsuario);
   logger.error("Error al consultar: {}", codigoUsuario, e);
   ```

4. **Manejo de Errores**

   ```java
   try {
       return client.obtenerGruposPorCodigoUsuario(codigoUsuario);
   } catch (Exception e) {
       logger.error("Error: ", e);
       throw new BusinessException("Error al obtener grupo", e);
   }
   ```

5. **Configurar Timeouts**
   ```properties
   client/mp-rest/connectTimeout=5000
   client/mp-rest/readTimeout=10000
   ```

### ❌ DON'T (No Hacer)

1. **Inyectar RestClient Directamente en Servicios**

   ```java
   @RestClient
   GrupoPorIntegranteRestClient restClient; // ❌ Incorrecto
   ```

2. **Hardcodear URLs**

   ```java
   String url = "http://servidor:8080/api"; // ❌ Incorrecto
   ```

3. **Ignorar Excepciones**

   ```java
   try {
       client.obtener();
   } catch (Exception e) {
       // ❌ Nunca dejar vacío
   }
   ```

4. **No Configurar Timeouts**
   - Puede causar bloqueos indefinidos

5. **Exponer DTOs del Cliente en APIs Públicas**
   - Usar DTOs específicos para cada capa

---

## Troubleshooting

### Problema 1: Connection Timeout

**Síntoma:**

```
java.net.ConnectException: Connection timed out
```

**Solución:**

```properties
# Incrementar timeout de conexión
grupo-por-integrante-rest/mp-rest/connectTimeout=10000
```

### Problema 2: Read Timeout

**Síntoma:**

```
java.net.SocketTimeoutException: Read timed out
```

**Solución:**

```properties
# Incrementar timeout de lectura
grupo-por-integrante-rest/mp-rest/readTimeout=30000
```

### Problema 3: SSL Certificate

**Síntoma:**

```
javax.net.ssl.SSLHandshakeException
```

**Solución:**

```properties
# Configurar truststore
grupo-por-integrante-rest/mp-rest/trustStore=/path/to/truststore.jks
grupo-por-integrante-rest/mp-rest/trustStorePassword=password
```

### Problema 4: 404 Not Found

**Verificar:**

1. URL correcta en properties
2. Path del endpoint
3. Servicio externo funcionando

```bash
# Test manual
curl -X GET "http://localhost:8080/sri-grupo-trabajo-fachada/rest/grupoPorIntegrante/{id}?codigoUsuario=TEST"
```

### Problema 5: Deserialización JSON

**Síntoma:**

```
JsonMappingException: Cannot deserialize
```

**Solución:**

- Verificar que los DTOs coincidan con la estructura JSON
- Agregar anotaciones Jackson si es necesario

```java
@JsonProperty("nombre_campo")
private String nombreCampo;
```

---

## 🔍 Monitoreo y Métricas

### Habilitar Métricas

```properties
quarkus.micrometer.enabled=true
quarkus.micrometer.export.prometheus.enabled=true
```

### Métricas Disponibles

- `http.client.requests` - Total de peticiones
- `http.client.requests.duration` - Duración de peticiones
- `resilience4j.circuitbreaker.calls` - Estado del Circuit Breaker

### Visualizar Estado del Circuit Breaker

```bash
curl http://localhost:8680/q/health/ready
```

---

## 📚 Referencias

- [Quarkus REST Client Guide](https://quarkus.io/guides/rest-client)
- [MicroProfile REST Client](https://download.eclipse.org/microprofile/microprofile-rest-client-3.0/microprofile-rest-client-spec-3.0.html)
- [SmallRye Fault Tolerance](https://smallrye.io/smallrye-fault-tolerance/)
- [Circuit Breaker Pattern](https://martinfowler.com/bliki/CircuitBreaker.html)
