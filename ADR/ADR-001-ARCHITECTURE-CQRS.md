# ADR-001: Architecture CQRS pour la Gestion des Recettes

**Date:** Janvier 2025 
**Authors:** Achraf EL ALLALI, WIAM ABDELLAOUI
**Context:** Projet TP - Architecture Logicielle et Qualité

---

## 1. Problem Statement (Problème)

L'application de gestion de recettes nécessite :
- Une **scalabilité** indépendante entre les opérations de lecture et d'écriture
- Une **performance optimale** pour les requêtes fréquentes
- Une **séparation des préoccupations** entre la persistence et la consultation
- Une **cohérence éventuelle** acceptable pour les données consultées

Les architectures traditionnelles CRUD unique ne peuvent pas répondre efficacement à ces besoins :
- Même modèle de données pour lecture et écriture
- Surcharge lors de pics de consultation
- Difficulté à mettre en cache de manière efficace

---

## 2. Decision (Décision Prise)

Nous avons choisi d'implémenter une **architecture CQRS (Command Query Responsibility Segregation)** avec :

### 2.1 Composants principaux

```
┌─────────────────────────────────────────────────────┐
│                    Client (API REST)                 │
└─────────────────────────────────────────────────────┘
                          │
        ┌─────────────────┴─────────────────┐
        │                                   │
   ┌────▼──────┐                     ┌─────▼─────┐
   │  Commands │                     │  Queries  │
   └────┬──────┘                     └─────┬─────┘
        │                                   │
   ┌────▼──────────────────┐        ┌──────▼─────────┐
   │ Command Service       │        │ Query Service  │
   │ (Write Model)         │        │ (Read Model)   │
   │ - Business Logic      │        │ - Cache        │
   │ - Validation          │        │ - Optimization │
   │ - Event Publishing    │        │ - Views        │
   └────┬──────────────────┘        └──────┬─────────┘
        │                                   │
        └─────────────────┬─────────────────┘
                          │
              ┌───────────▼──────────┐
              │    Event Store       │
              │   (Kafka / Broker)   │
              └───────────┬──────────┘
                          │
              ┌───────────▼──────────┐
              │   Write Database     │
              │   (PostgreSQL)       │
              └─────────────────────┘
```

### 2.2 Implémentation détaillée

**Write Path (Commandes):**
1. Réception de la commande (CreateRecetteCommand)
2. Validation métier (RecetteValidator)
3. Persistance en base de données
4. Génération et publication d'événements (RecetteCreatedEvent)
5. Gestion des erreurs et retry (Resilience4J)

**Read Path (Requêtes):**
1. Réception de la requête (GetRecettesQuery)
2. Consultation du cache ou du modèle de lecture optimisé
3. Retour des données sans logique métier

---

## 3. Rationale (Justification)

### 3.1 Avantages du CQRS

| Aspect | Bénéfice |
|--------|----------|
| **Performance** | Modèles de lecture optimisés indépendamment |
| **Scalabilité** | Scaling différencié (plus de replicas pour lectures) |
| **Maintenabilité** | Code séparé et organisé (Command ≠ Query) |
| **Testabilité** | Logique métier isolée et facile à tester |
| **Compliance** | Audit trail complet via les événements |
| **Résilience** | Cohérence éventuelle acceptable |

### 3.2 Trade-offs acceptés

| Inconvénient | Mitigation |
|--------------|-----------|
| **Complexité accrue** | Documentation et architecture claire |
| **Cohérence éventuelle** | Délai acceptable (<5s) pour notre cas |
| **Coût d'infrastructure** | Justifié par la scalabilité |
| **Nombre de BD** | Peut être optionnel (même BD en dev) |

---

## 4. Architecture Implémentée

### 4.1 Stack technologique

```yaml
Framework:
  - Spring Boot 3.x (DDD & Clean Architecture)
  - Spring Data JPA (Persistence)
  - Spring REST (API)

Messaging:
  - Apache Kafka (Event Bus)
    - Pour publication d'événements
    - Scaling des traitements asynchrones
  
Resilience:
  - Resilience4J 2.3.0
    - Retry Policy (3 tentatives, 1s délai)
    - Circuit Breaker (50% threshold)
    - Rate Limiter
  
Quality:
  - JUnit 5 (Unit tests)
  - Mockito (Mocking)
  - Cucumber (BDD/ATDD)
  - ArchUnit (Architecture tests)
  - Sonar (Code quality)

Database:
  - PostgreSQL (Persistence)
  - H2 (In-memory pour tests)

Build & Tools:
  - Maven 4.0+
  - Task runner (Taskfile.yml)
  - Prettier (Code formatting)
```

### 4.2 Dépendances Maven clés

```xml
<!-- Spring Boot -->
<spring-boot-starter-web>
<spring-boot-starter-data-jpa>
<spring-boot-starter-kafka>

<!-- Kafka Events -->
<spring-kafka>

<!-- Resilience -->
<resilience4j-spring-boot3>
<resilience4j-retry>

<!-- Testing -->
<junit-jupiter>
<mockito-core>
<io.cucumber:cucumber-java>
<io.cucumber:cucumber-spring>

<!-- Code Quality -->
<com.tngtech.archunit:archunit>
```

### 4.3 Structure des packages

```
src/main/java/Architecture_log/TP/
├── commands/
│   ├── CreateRecetteCommand.java
│   ├── UpdateRecetteCommand.java
│   ├── DeleteRecetteCommand.java
│   └── RecetteCommandService.java
├── queries/
│   ├── GetRecettesQuery.java
│   ├── GetRecetteQuery.java
│   ├── RecetteQueryService.java
│   └── RecetteDTO.java (Read Model)
├── events/
│   ├── RecetteCreatedEvent.java
│   ├── RecetteUpdatedEvent.java
│   ├── RecetteDeletedEvent.java
│   └── RecetteEventPublisher.java
├── entities/
│   ├── Recette.java
│   ├── Ingredient.java
│   └── RecetteRepository.java
├── controller/
│   ├── RecetteController.java (REST API)
│   └── IngredientController.java
└── architecture/
    └── ArchitectureTest.java (ArchUnit)

src/test/java/...
└── bdd/
    ├── CucumberTestRunner.java
    ├── RecetteStepDefs.java
    └── CQRSStepDefs.java
```

### 5.2 Patterns utilisés

| Pattern | Location | Purpose |
|---------|----------|---------|
| **4.4S** | Command/Query Services | Séparation Lecture/Écriture |
| **DDD** | Entities/Commands | Domain-Driven Design |
| **Repository** | entities/RecetteRepository | Data Access Layer |
| **Publisher/Subscriber** | events/ | Event-driven communication |
| **DTO** | queries/RecetteDTO | Data Transfer Object |
| **Resilience** | Config | Retry/Circuit Breaker |

---

## 5. Test Strategy (Stratégie de Test)

### 5.1 Niveaux de test

```
Unit Tests (80%)
├── ArchitectureTest (ArchUnit)      ✓ Valide la structure
├── RecetteCommandServiceTest        ✓ Logique métier
├── RecetteQueryServiceTest          ✓ Requêtes
└── EntityTest                       ✓ Validations

Integration Tests (15%)
├── RecetteIntegrationTest           ✓ CQRS end-to-end
├── RecetteRepositoryTest            ✓ Persistance
└── ControllerTest                   ✓ API REST

BDD/Acceptance Tests (5%)
└── Cucumber Features
    ├── recette.feature              ✓ Scénarios métier
    ├── ingredient.feature           ✓ Ingrédients
    └── cqrs.feature                 ✓ Comportement CQRS
```

### 5.2 Couverture de test

- **Cible:** ≥ 85% de couverture
- **Critère:** Commands et Services prioritaires
- **Outils:** JaCoCo pour la mesure

---

## 6. Monitoring & Observability

### 6.1 Points de monitoring

```
1. Command Publishing
   - Succès/Erreur des commandes
   - Latence de persistance
   - Retry attempts

2. Event Publishing
   - Événements publiés sur Kafka
   - Erreurs de publication
   - Partition distribution

3. Query Execution
   - Temps de réponse des requêtes
   - Cache hit rate
   - N+1 queries
```

### 6.2 Logging

```java
- INFO: Exécution des commandes réussies
- WARN: Retry attempts, cohérence éventuelle
- ERROR: Échecs, erreurs de persistance
- DEBUG: Détails des événements
```

---

## 7. Migration Path (Chemin d'évolution)

### Phase 1 (Actuelle)
- ✅ CQRS basique avec même base de données
- ✅ Kafka pour les événements
- ✅ Tests et documentation complète

### Phase 2 (Future)
- 📋 Séparation en 2 bases de données (Write/Read)
- 📋 Cache distribuée (Redis) pour Read Model
- 📋 Event Sourcing optionnel
- 📋 Saga Pattern pour les transactions distribuées

### Phase 3 (Long terme)
- 📋 Microservices par domaine métier
- 📋 CQRS distribué multi-services
- 📋 Stream Processing (Kafka Streams)

---

## 8. Consequences (Conséquences)

### Positives
✅ Scalabilité indépendante  
✅ Performance optimale  
✅ Code maintenable  
✅ Testabilité excellente  

### Négatives
⚠️ Complexité augmentée  
⚠️ Cohérence éventuelle à gérer  
⚠️ Plus de code à écrire  

---

## 9. Related ADRs

- **ADR-002:** Choix de Kafka pour le message bus
- **ADR-003:** Stratégie de résilience avec Resilience4J

---

## 10. References

- [CQRS Pattern - Martin Fowler](https://martinfowler.com/bliki/CQRS.html)
- [Spring CQRS Guide](https://spring.io/guides)
- [Kafka with Spring Boot](https://kafka.apache.org/documentation/)
- [ArchUnit for Architecture Testing](https://www.archunit.org/)

---

**Approved by:** Achraf EL ALLALI, WIAM ABDELLAOUI 
**Last reviewed:** Janvier 2025
