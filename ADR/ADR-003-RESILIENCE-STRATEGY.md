# ADR-003: Stratégie de Résilience avec Resilience4J

**Date:** Janvier 2025 
**Authors:** Achraf EL ALLALI, WIAM ABDELLAOUI
**Context:** Projet TP - Architecture Logicielle et Qualité

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
- Gestion gracieuse des erreurs

---

## 2. Decision

Utiliser **Resilience4J 2.3.0** pour implémenter le pattern **Retry automatique**.

### Pattern implémenté: Retry Automatique

```
┌──────────────────────────────────────┐
│         Request Incoming             │
└────────────────┬─────────────────────┘
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
                    │               ┌──▼──────────────────┐
                    │               │ Fallback            │
                    │               │ (log error, return) │
                    │               └──┬──────────────────┘
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
```

### 3.2 Application Configuration

```properties
# application.properties
resilience4j.retry.instances.recetteRetry.maxAttempts=3
resilience4j.retry.instances.recetteRetry.waitDuration=1000
resilience4j.retry.instances.recetteRetry.retryExceptions=org.springframework.dao.DataAccessException,java.sql.SQLException
resilience4j.retry.instances.recetteRetry.ignoreExceptions=
```

### 3.3 Implémentation Code

```java
@Service
public class RecetteCommandService {
    
    @Retry(name = "recetteRetry")
    public Recette createRecette(CreateRecetteDTO dto) {
        // Validation et création avec retry automatique
        Recette recette = new Recette(dto.getName());
        return recetteRepository.save(recette);
    }
}
```

### 3.4 Gestion des Erreurs de Kafka

Les erreurs de publication Kafka sont gérées de manière **non-bloquante** :

```java
@Service
public class RecetteEventPublisher {
    
    private static final String TOPIC_NAME = "recette-events";
    
    public void publishRecetteCreated(RecetteCreatedEvent event) {
        try {
            CompletableFuture<SendResult<String, RecetteCreatedEvent>> future =
                kafkaTemplate.send(TOPIC_NAME, event.getId().toString(), event);
            
            future.whenComplete((result, exception) -> {
                if (exception != null) {
                    logger.error("Failed to publish event: {}", event.getId());
                    // L'erreur est loggée mais ne bloque pas la création
                }
            });
        } catch (Exception e) {
            logger.error("Error publishing event: {}", e.getMessage());
            // Non-bloquant : la recette a déjà été créée en BD
        }
    }
}
```
