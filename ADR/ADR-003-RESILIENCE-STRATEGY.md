# ADR-003: Stratégie de Résilience avec Resilience4J

**Date:** Janvier 2025  
**Status:** Accepté  
**Authors:** Équipe Logicielle  
**Context:** Gestion des défaillances en architecture distribuée CQRS

---

## 1. Problem Statement

L'application utilise des composants externes (Kafka, Base de données) qui peuvent échouer :
- Timeouts réseau
- Base de données non disponible
- Kafka temporairement indisponible
- Surcharges momentanées

Sans stratégie de résilience, l'application échouerait complètement.

**Besoins:**
- Retry automatique des opérations échouées
- Circuit breaker pour éviter les cascades de défaillances
- Rate limiting pour éviter les surcharges
- Fallback graceful

---

## 2. Decision

Utiliser **Resilience4J 2.3.0** pour implémenter les patterns de résilience.

### Patterns implémentés

```
┌──────────────────────────────────────┐
│         Request Incoming             │
└────────────────┬─────────────────────┘
                 │
         ┌───────▼────────┐
         │ Rate Limiter?  │
         └───────┬────────┘
                 │
         ┌───────▼────────────────┐
         │ Circuit Breaker?       │
         │ (Estado: CLOSED?)      │
         └───────┬────────────────┘
                 │
         ┌───────▼────────────────┐
         │ Tentative #1           │
         │ (avec timeout)         │
         └───┬────────────────┬───┘
             │ Succès         │ Échec
             │                │
          ✓  │          ┌─────▼──────────┐
             │          │ Retry Attempt? │
             │          └─────┬──────────┘
             │                │
             │        ┌───────▼──────────┐
             │        │ Tentative #2-3   │
             │        │ (avec backoff)   │
             │        └─────┬──────────┬─┘
             │          ✓   │          │ Échec final
             │              │          │
             └──────┬───────┘          │
                    │               ┌──▼──────┐
                    │               │ Fallback│
                    │               └──┬──────┘
                    │                  │
                    └──────────┬───────┘
                               │
                        ┌──────▼──────┐
                        │   Response  │
                        └─────────────┘
```

---

## 3. Configuration Implémentée

### 3.1 Maven Dependency

```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
    <version>2.3.0</version>
</dependency>

<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-retry</artifactId>
    <version>2.3.0</version>
</dependency>

<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-circuitbreaker</artifactId>
    <version>2.3.0</version>
</dependency>

<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-ratelimiter</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 3.2 Application Configuration

```yaml
# application.properties
resilience4j.retry:
  instances:
    recette-command-service:
      max-attempts: 3
      wait-duration: 1000
      retry-exceptions: java.io.IOException, java.sql.SQLException
      ignore-exceptions: java.lang.IllegalArgumentException
      exponential-backoff-multiplier: 2

resilience4j.circuitbreaker:
  instances:
    recette-command-service:
      failure-rate-threshold: 50
      slow-call-rate-threshold: 50
      slow-call-duration-threshold: 5000
      permitted-number-of-calls-in-half-open-state: 3
      minimum-number-of-calls: 10
      wait-duration-in-open-state: 60000

resilience4j.ratelimiter:
  instances:
    recette-api:
      register-health-indicator: true
      limit-refresh-period: 1m
      limit-for-period: 100
      timeout-duration: 5s
```

### 3.3 Implémentation Code

```java
@Service
public class RecetteCommandService {
    
    @Retry(name = "recette-command-service")
    @CircuitBreaker(name = "recette-command-service", fallbackMethod = "fallbackCreate")
    public RecetteDTO createRecette(CreateRecetteCommand command) {
        // Business logic
        return new RecetteDTO(...);
    }
    
    // Fallback method
    public RecetteDTO fallbackCreate(CreateRecetteCommand command, Exception ex) {
        logger.error("Failed to create recette, using fallback", ex);
        // Return cached data or error response
        throw new ServiceUnavailableException("Création échouée", ex);
    }
}

@RestController
@RequestMapping("/recettes")
public class RecetteController {
    
    @RateLimiter(name = "recette-api")
    @PostMapping
    public ResponseEntity<RecetteDTO> createRecette(@RequestBody CreateRecetteCommand command) {
        RecetteDTO recette = recetteCommandService.createRecette(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(recette);
    }
}
```

---

## 4. Patterns de Résilience

### 4.1 Retry Pattern

**Configuration:**
- Max attempts: 3
- Wait duration: 1 second
- Exponential backoff: multiplier 2 (1s, 2s, 4s)

**Cas d'usage:**
- Erreurs temporaires réseau
- Database connection timeouts
- Kafka producer errors

```
Tentative 1: ─╯ (immédiate)
Tentative 2: ──╯ (après 1s)
Tentative 3: ────╯ (après 2s)
Échec final: ──────✗ (après 4s)
```

### 4.2 Circuit Breaker Pattern

**États:**
- **CLOSED:** Fonctionnement normal (requests passent)
- **OPEN:** Défaillance détectée (requests échouent immédiatement)
- **HALF_OPEN:** Test de rétablissement (quelques requests autorisées)

**Seuils:**
- Failure rate: 50% (ouvre après 50% d'erreurs)
- Minimum calls: 10 (attend 10 appels avant évaluation)
- Wait duration: 60 secondes en OPEN

```
CLOSED ─────────────────┐
       │ 50% failures   │
       └────────▶ OPEN ◀┘
                 │
            après 60s
                 │
                 ▼
            HALF_OPEN
                 │
     ┌───────────┴───────────┐
     │ succès                │ échec
     │                       │
     ▼                       ▼
  CLOSED                  OPEN
```

### 4.3 Rate Limiter Pattern

**Configuration:**
- Limit: 100 requêtes
- Period: 1 minute
- Timeout: 5 secondes

**Comportement:**
```
Minute 1: 1  2  3  ...  100  101(rejected)
Minute 2: reset → 1  2  3  ...  100
```

---

## 5. Monitoring & Observability

### 5.1 Métriques Actuator

```
GET /actuator/metrics/resilience4j.circuitbreaker.state
GET /actuator/metrics/resilience4j.retry.attempts
GET /actuator/metrics/resilience4j.ratelimiter.available.permits
```

### 5.2 Health Endpoints

```
GET /actuator/health
→ circuitBreakers:
    recette-command-service: UP|DOWN
→ retries:
    recette-command-service: UP
→ rateLimiters:
    recette-api: UP
```

### 5.3 Logging des événements

```java
@Slf4j
public class RecetteCommandService {
    
    @Retry(name = "recette-command-service")
    public RecetteDTO createRecette(CreateRecetteCommand command) {
        try {
            logger.info("Creating recette: {}", command.getNom());
            // ...
        } catch (RetryableException e) {
            logger.warn("Retry triggered for recette creation", e);
            throw e;
        }
    }
}
```

---

## 6. Testing avec Resilience4J

### 6.1 Test de Retry

```java
@Test
void testRetryOnFailure() {
    // Mock un service qui échoue 2 fois
    when(recetteRepository.save(any()))
        .thenThrow(SQLException.class)
        .thenThrow(SQLException.class)
        .thenReturn(new Recette());
    
    // Devrait réussir après 2 retries
    RecetteDTO result = recetteCommandService.createRecette(command);
    
    assertNotNull(result);
    verify(recetteRepository, times(3)).save(any());
}
```

### 6.2 Test de Circuit Breaker

```java
@Test
void testCircuitBreakerOpens() {
    // Mock pour toujours échouer
    when(recetteRepository.save(any()))
        .thenThrow(SQLException.class);
    
    // Faire échouer 10+ fois pour ouvrir le circuit
    for (int i = 0; i < 15; i++) {
        assertThrows(CircuitBreakerOpenException.class, 
                    () -> recetteCommandService.createRecette(command));
    }
    
    // Vérifier que circuit est OPEN
    assertEquals(CircuitBreaker.State.OPEN, 
                 getCircuitBreakerState("recette-command-service"));
}
```

### 6.3 Test de Rate Limiter

```java
@Test
void testRateLimiterEnforces() {
    RateLimiter limiter = RateLimiter.of("test", RateLimiterConfig.custom()
        .limitForPeriod(3)
        .limitRefreshPeriod(Duration.ofSeconds(1))
        .build());
    
    // Autoriser 3 requests
    assertTrue(limiter.acquirePermission());
    assertTrue(limiter.acquirePermission());
    assertTrue(limiter.acquirePermission());
    
    // 4e request rejetée
    assertFalse(limiter.acquirePermission());
}
```

---

## 7. Scenarios de Défaillance Couverts

| Scénario | Pattern | Action |
|----------|---------|--------|
| Base de données down | Retry + Circuit Breaker | Retry 3x, puis fail-fast |
| Kafka indisponible | Retry + Fallback | Retry, puis log et continue |
| Overload du serveur | Rate Limiter | Reject avec 429 Too Many Requests |
| Network timeout | Retry + Exponential Backoff | Retry avec délai croissant |
| Intermittent failures | Retry + Half-Open | Récupération automatique |

---

## 8. Consequences

### Positives ✅
- Haute disponibilité
- Dégradation gracieuse
- Récupération automatique
- Métriques complètes
- Configurations centralisées

### Negatives ⚠️
- Latence augmentée (retries)
- Consistency garantie (cohérence éventuelle)
- Configuration complexity
- Needs monitoring

---

## 9. Alternatives Rejectées

| Alternative | Raison |
|-------------|--------|
| **Istio/Service Mesh** | Overhead, pas besoin au niveau app |
| **Manual retry logic** | Error-prone, hard to maintain |
| **Hystrix** | Deprecated, Resilience4J plus moderne |
| **AWS SDK built-in** | Cloud-dependent |

---

## 10. Migration & Rollout

### Phase 1
- Retry pattern uniquement
- Circuit breaker en monitor-only

### Phase 2
- Circuit breaker actif
- Rate limiter en staging

### Phase 3
- Tous les patterns en production
- Monitoring et alertes activés

---

**Approved by:** [À compléter]  
**Last reviewed:** Janvier 2025
