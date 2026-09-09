# 🌍 Configuración por Ambientes

## application-dev.properties (Desarrollo)

```properties
# Cliente REST - Grupo por Integrante (Desarrollo)
grupo-por-integrante-rest/mp-rest/url=http://localhost:8080/sri-grupo-trabajo-fachada/rest
grupo-por-integrante-rest/mp-rest/scope=javax.inject.Singleton
grupo-por-integrante-rest/mp-rest/connectTimeout=10000
grupo-por-integrante-rest/mp-rest/readTimeout=20000

# Logging detallado en desarrollo
quarkus.log.category."ec.gob.sri.api.catalogo.infraestructure.client".level=DEBUG
quarkus.log.category."org.jboss.resteasy.reactive.client.logging".level=DEBUG
```

## application-test.properties (Testing)

```properties
# Cliente REST - Grupo por Integrante (Testing/QA)
grupo-por-integrante-rest/mp-rest/url=http://servidor-test:8080/sri-grupo-trabajo-fachada/rest
grupo-por-integrante-rest/mp-rest/scope=javax.inject.Singleton
grupo-por-integrante-rest/mp-rest/connectTimeout=8000
grupo-por-integrante-rest/mp-rest/readTimeout=15000

# Fault Tolerance en test
grupo-por-integrante-rest/mp-rest/providers=io.quarkus.rest.client.reactive.logging.DefaultClientLogger

# Circuit Breaker más permisivo en test
mp.fault.tolerance.CircuitBreaker.requestVolumeThreshold=10
mp.fault.tolerance.CircuitBreaker.failureRatio=0.7
```

## application-prod.properties (Producción)

```properties
# Cliente REST - Grupo por Integrante (Producción)
grupo-por-integrante-rest/mp-rest/url=${GRUPO_INTEGRANTE_PROD_URL}
grupo-por-integrante-rest/mp-rest/scope=javax.inject.Singleton
grupo-por-integrante-rest/mp-rest/connectTimeout=5000
grupo-por-integrante-rest/mp-rest/readTimeout=10000

# Reintentos automáticos
mp.fault.tolerance.retry.maxRetries=3
mp.fault.tolerance.retry.delay=1000

# Circuit Breaker estricto en producción
mp.fault.tolerance.CircuitBreaker.requestVolumeThreshold=5
mp.fault.tolerance.CircuitBreaker.failureRatio=0.5
mp.fault.tolerance.CircuitBreaker.delay=30000

# Timeout global
mp.fault.tolerance.timeout.value=8000

# Logging moderado en producción
quarkus.log.category."ec.gob.sri.api.catalogo.infraestructure.client".level=INFO
```

## Variables de Entorno por Ambiente

### Desarrollo Local

```bash
# .env.development
GRUPO_INTEGRANTE_URL=http://localhost:8080/sri-grupo-trabajo-fachada/rest
QUARKUS_PROFILE=dev
```

### Testing/QA

```bash
# .env.testing
GRUPO_INTEGRANTE_URL=http://servidor-qa:8080/sri-grupo-trabajo-fachada/rest
QUARKUS_PROFILE=test
```

### Producción

```bash
# .env.production
GRUPO_INTEGRANTE_PROD_URL=https://api-prod.sri.gob.ec/sri-grupo-trabajo-fachada/rest
QUARKUS_PROFILE=prod

# Configuración SSL
TRUSTSTORE_PATH=/etc/ssl/certs/truststore.jks
TRUSTSTORE_PASSWORD=secret
KEYSTORE_PATH=/etc/ssl/certs/keystore.jks
KEYSTORE_PASSWORD=secret
```

## Docker Compose - Desarrollo

```yaml
version: '3.8'

services:
  catalogo-api:
    image: sri-catalogo-api:latest
    environment:
      - QUARKUS_PROFILE=dev
      - GRUPO_INTEGRANTE_URL=http://grupo-trabajo-service:8080/sri-grupo-trabajo-fachada/rest
      - QUARKUS_LOG_LEVEL=DEBUG
    ports:
      - "8680:8680"
    depends_on:
      - grupo-trabajo-service
  
  grupo-trabajo-service:
    image: sri-grupo-trabajo-fachada:latest
    ports:
      - "8080:8080"
```

## Kubernetes ConfigMap

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: catalogo-api-config
  namespace: sri-prod
data:
  application.properties: |
    grupo-por-integrante-rest/mp-rest/url=http://grupo-trabajo-service.sri-prod.svc.cluster.local:8080/sri-grupo-trabajo-fachada/rest
    grupo-por-integrante-rest/mp-rest/connectTimeout=5000
    grupo-por-integrante-rest/mp-rest/readTimeout=10000
    
    mp.fault.tolerance.retry.maxRetries=3
    mp.fault.tolerance.CircuitBreaker.requestVolumeThreshold=5
    
    quarkus.log.level=INFO
```

## Activar Perfiles

### Comando Maven

```bash
# Desarrollo
./mvnw quarkus:dev -Dquarkus.profile=dev

# Testing
./mvnw test -Dquarkus.profile=test

# Producción
./mvnw package -Dquarkus.profile=prod
```

### Variable de Entorno

```bash
export QUARKUS_PROFILE=prod
java -jar target/quarkus-app/quarkus-run.jar
```

### Argumento JVM

```bash
java -Dquarkus.profile=prod -jar target/quarkus-app/quarkus-run.jar
```
