# ⚡ Aide-Mémoire Rapide

## 🚀 Démarrage Rapide (2 minutes)

### Lancer l'application
```bash
task run
# Ou
mvn spring-boot:run
```
L'app est sur `http://localhost:8080`

### Tester l'API (Choisir une option)

**Option 1: VS Code REST Client** ⭐
```
1. Installer extension "REST Client"
2. Ouvrir api/recettes.http
3. Cliquer "Send Request"
```

**Option 2: Terminal cURL**
```bash
curl http://localhost:8080/recettes
```

**Option 3: Taskfile**
```bash
task api:recettes:list
```

**Option 4: Postman**
```
File → Import → api/recettes.postman_collection.json
```

---

## 📝 Commandes Essentielles

```bash
# Build & Test
task build              # Compiler
task test               # Tous les tests
task test:cucumber      # Tests BDD uniquement
task verify             # Build + test + quality

# Run
task run                # Démarrer l'app
task run:dev            # Mode développement

# API Testing
task api:recettes:list       # GET /recettes
task api:recettes:create     # POST /recettes
task api:ingredients:list    # GET /recettes/1/ingredients

# Git
task git:status         # État du repo
task git:log            # Historique
task git:push           # Pousser les changements

# Autres
task clean              # Nettoyer target/
task format             # Formater le code
task quality            # Vérifier qualité
task help               # Voir toutes les commandes
```

---

## 📖 Documentation Clés

| Fichier | Contenu | Durée |
|---------|---------|-------|
| [LIVRAISON_COMPLETE.md](LIVRAISON_COMPLETE.md) | Vue d'ensemble complète | 5 min |
| [api/GUIDE_UTILISATION.md](api/GUIDE_UTILISATION.md) | Comment tester l'API | 10 min |
| [api/README.md](api/README.md) | Spécification API détaillée | 15 min |
| [.md/ADR-001](​.md/ADR-001-ARCHITECTURE-CQRS.md) | Architecture CQRS | 20 min |
| [.md/ADR-002](​.md/ADR-002-KAFKA-MESSAGE-BUS.md) | Kafka configuration | 15 min |
| [.md/ADR-003](​.md/ADR-003-RESILIENCE-STRATEGY.md) | Résilience patterns | 15 min |

---

## 🔗 Fichiers de Requêtes API

### REST Client (VS Code)
📄 **[api/recettes.http](api/recettes.http)**
```http
### Lister les recettes
GET {{baseUrl}}/recettes

### Créer une recette
POST {{baseUrl}}/recettes
Content-Type: {{contentType}}

{"nom":"Pizza Margherita",...}
```

### Postman Collection
📄 **[api/recettes.postman_collection.json](api/recettes.postman_collection.json)**
```bash
File → Import → Importer le JSON
```

### cURL Commandes
📄 **[api/CURL_COMMANDS.md](api/CURL_COMMANDS.md)**
```bash
curl http://localhost:8080/recettes
```

### Taskfile Integration
📄 **[Taskfile.yml](Taskfile.yml)**
```bash
task api:recettes:list
```

---

## 🧪 Tests

### Exécuter les tests
```bash
# BDD (Cucumber)
task test:cucumber

# Tous les tests
task test

# Tests spécifiques
task test:archunit      # Architecture tests
task test:commands      # Command services
task test:integration   # Integration tests
task test:entity        # Entity tests
```

### Fichiers de tests
- **Features:** `src/test/resources/features/`
  - `cqrs.feature` - 7 scénarios CQRS
  - `recette.feature` - 6+ scénarios recettes
  - `ingredient.feature` - Ingrédients
- **Step Definitions:** `src/test/java/.../bdd/`
  - `RecetteStepDefs.java`
  - `IngredientStepDefs.java`
  - `CQRSStepDefs.java`
  - `CucumberTestRunner.java`

---

## 🏛️ Architecture

### Composants Clés
- **Commands:** Opérations d'écriture
- **Queries:** Opérations de lecture
- **Events:** Publication d'événements
- **Entities:** Domaine métier
- **Controllers:** API REST

### Technologies
- **Framework:** Spring Boot 3.x
- **Messaging:** Apache Kafka 3.x
- **Database:** PostgreSQL
- **Résilience:** Resilience4J 2.3.0
- **Testing:** JUnit5, Cucumber, ArchUnit
- **Build:** Maven 4.0+

### Patterns
- **CQRS:** Séparation Lecture/Écriture
- **DDD:** Domain-Driven Design
- **Event-Driven:** Architecture orientée événements
- **Resilience:** Retry, Circuit Breaker, Rate Limiter

---

## ⚙️ Configuration

### Application
```properties
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/recettes
spring.jpa.hibernate.ddl-auto=update
spring.kafka.bootstrap-servers=localhost:9092
```

### Resilience4J
```properties
resilience4j.retry.instances.recette-command-service.max-attempts=3
resilience4j.circuitbreaker.instances.recette-command-service.failure-rate-threshold=50
resilience4j.ratelimiter.instances.recette-api.limit-for-period=100
```

### Docker Compose
```bash
docker-compose up    # Démarrer Kafka, Postgres, etc.
docker-compose down  # Arrêter les services
```

---

## 🐛 Dépannage Rapide

| Problème | Solution |
|----------|----------|
| `Connection refused` | `task run` pour démarrer l'app |
| `404 Not Found` | Créer une recette d'abord |
| `503 Service Unavailable` | Circuit breaker ouvert, attendre 60s |
| `Port already in use` | Tuer le processus: `lsof -i :8080` |
| `Database error` | Vérifier PostgreSQL: `docker ps` |

---

## 📊 Endpoints Principaux

### Recettes
```
GET    /recettes              # Lister
GET    /recettes/{id}         # Détails
POST   /recettes              # Créer
PUT    /recettes/{id}         # Mettre à jour
DELETE /recettes/{id}         # Supprimer
```

### Ingrédients
```
GET    /recettes/{id}/ingredients         # Lister
POST   /recettes/{id}/ingredients         # Ajouter
PUT    /recettes/{id}/ingredients/{igId}  # Mettre à jour
DELETE /recettes/{id}/ingredients/{igId}  # Supprimer
```

### Santé
```
GET /actuator/health          # État de santé
GET /actuator/metrics         # Métriques
GET /actuator/env             # Variables d'env
```

---

## 📈 Monitoring

### Vérifier la santé
```bash
curl http://localhost:8080/actuator/health

# Sortie
{
  "status": "UP",
  "components": {
    "circuitBreakers": {"status": "UP"},
    "db": {"status": "UP"},
    "kafka": {"status": "UP"}
  }
}
```

### Voir les métriques
```bash
curl http://localhost:8080/actuator/metrics
```

---

## 🎯 Checklist d'Utilisation

### Première utilisation
- [ ] Lire [LIVRAISON_COMPLETE.md](LIVRAISON_COMPLETE.md)
- [ ] Démarrer: `task run`
- [ ] Tester: `curl http://localhost:8080/recettes`
- [ ] Importer dans Postman ou ouvrir `.http` dans VS Code

### Développement
- [ ] Modifier le code
- [ ] `task format` - Formater
- [ ] `task test` - Tester
- [ ] `task verify` - Vérifier quality
- [ ] `git push` - Pusher

### Avant de livrer
- [ ] `task verify` ✅ passe
- [ ] Tests BDD `task test:cucumber` ✅ passe
- [ ] Code review
- [ ] Lire les 3 ADRs

---

## 💡 Tips & Tricks

### Ouvrir les fichiers clés
```bash
# Ouvrir dans VS Code
code api/recettes.http           # Fichier HTTP
code .md/ADR-001-ARCHITECTURE-CQRS.md  # Architecture

# Ouvrir directement
cat api/README.md                # Documentation API
cat api/CURL_COMMANDS.md         # Commandes cURL
```

### Lancer plusieurs commandes
```bash
# Terminal 1: Démarrer l'app
task run

# Terminal 2: Lancer les tests
task test:cucumber

# Terminal 3: Tester l'API
curl http://localhost:8080/recettes
```

### Scripts utiles
```bash
# Vérifier que tout fonctionne
task verify

# Nettoyer avant commit
task clean && task format

# Voir l'état du projet
task git:status
task git:log
```

---

## 🔐 Résilience - Comportement attendu

### Retry
- **Max 3 tentatives** avec délais: 1s, 2s, 4s
- Erreurs temporaires: IOException, SQLException
- Ignorer: IllegalArgumentException

### Circuit Breaker
- **Ouverture:** 50% d'erreurs
- **État:** CLOSED → OPEN → HALF_OPEN
- **Timeout:** 60 secondes
- **Demi-ouverture:** 3 appels autorisés

### Rate Limiter
- **Limite:** 100 requêtes par minute
- **Timeout:** 5 secondes
- **Réponse:** 429 Too Many Requests

---

## 📚 Ressources Externes

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Kafka Documentation](https://kafka.apache.org/)
- [Resilience4J](https://resilience4j.readme.io/)
- [Cucumber Guide](https://cucumber.io/docs/cucumber/)
- [REST Client Extension](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)

---

**Version:** 1.0  
**Last Updated:** Janvier 2025  
**Status:** ✅ Complete
