# 📚 API REST - Documentation Complète

## 🎯 Vue d'ensemble

Cette API REST implémente une architecture **CQRS** (Command Query Responsibility Segregation) pour la gestion des recettes et ingrédients.

### Base URL
```
http://localhost:8080
```

### Authentification
Actuellement sans authentification. À implémenter : JWT/OAuth2

### Format des réponses
Toutes les réponses sont en JSON avec le content-type `application/json`.

---

## 📦 Fichiers de test disponibles

### 1. **HTTP Client (VS Code REST Client)**
📄 [recettes.http](recettes.http)
- Utiliser l'extension "REST Client" dans VS Code
- Commandes directes dans le fichier `.http`

### 2. **Postman Collection**
📄 [recettes.postman_collection.json](recettes.postman_collection.json)
- Importer dans Postman : `File > Import`
- Collection organisée par domaine
- Variables d'environnement préconfigurées

### 3. **cURL Commands**
📄 [CURL_COMMANDS.md](CURL_COMMANDS.md)
- Commandes complètes pour terminal
- Scripts d'intégration
- Tests de résilience

---

## 🔄 Architecture CQRS

### Write Path (Commandes)
```
Client → POST /recettes → RecetteCommandService → Database
                            ↓
                      Publish Event
                            ↓
                         Kafka
```

### Read Path (Requêtes)
```
Client → GET /recettes → RecetteQueryService → Cache/Database
```

---

## 📋 Endpoints API

### RECETTES

#### `GET /recettes` - Lister toutes les recettes
**Type:** Query  
**Description:** Récupère la liste complète des recettes

**Query Parameters:**
- `page` (int, default: 0) - Numéro de page
- `size` (int, default: 20) - Éléments par page
- `sort` (string) - Tri (ex: `nom,asc` ou `id,desc`)
- `nom` (string) - Recherche par nom

**Exemple:**
```bash
GET /recettes?page=0&size=10&sort=nom,asc
GET /recettes?nom=Pizza
```

**Response:** 200 OK
```json
{
  "content": [
    {
      "id": 1,
      "nom": "Pizza Margherita",
      "description": "Pizza classique...",
      "tempsPreparation": 30,
      "tempsCuisson": 15,
      "createdAt": "2025-01-16T10:30:00Z"
    }
  ],
  "totalElements": 10,
  "totalPages": 1,
  "currentPage": 0,
  "size": 20
}
```

---

#### `GET /recettes/{id}` - Récupérer une recette
**Type:** Query  
**Description:** Récupère les détails d'une recette spécifique

**Path Parameters:**
- `id` (long, required) - ID de la recette

**Exemple:**
```bash
GET /recettes/1
```

**Response:** 200 OK
```json
{
  "id": 1,
  "nom": "Pizza Margherita",
  "description": "Pizza classique avec tomate, mozzarella et basilic",
  "tempsPreparation": 30,
  "tempsCuisson": 15,
  "createdAt": "2025-01-16T10:30:00Z",
  "updatedAt": "2025-01-16T11:00:00Z",
  "ingredients": [
    {
      "id": 1,
      "nom": "Tomate",
      "quantite": "500g",
      "prix": 2.50,
      "unite": "g"
    }
  ]
}
```

**Response:** 404 Not Found
```json
{
  "error": "Recette non trouvée",
  "errorCode": "RECETTE_NOT_FOUND",
  "timestamp": "2025-01-16T10:30:00Z"
}
```

---

#### `POST /recettes` - Créer une recette
**Type:** Command  
**Description:** Crée une nouvelle recette et publie un événement

**Request Body:**
```json
{
  "nom": "Pizza Margherita",
  "description": "Pizza classique avec tomate, mozzarella et basilic",
  "tempsPreparation": 30,
  "tempsCuisson": 15
}
```

**Validations:**
- `nom` : Required, 1-255 chars
- `description` : Optional, max 1000 chars
- `tempsPreparation` : Positive integer
- `tempsCuisson` : Positive integer

**Response:** 201 Created
```json
{
  "id": 5,
  "nom": "Pizza Margherita",
  "description": "Pizza classique...",
  "tempsPreparation": 30,
  "tempsCuisson": 15,
  "createdAt": "2025-01-16T10:45:00Z"
}
```

**Response:** 400 Bad Request
```json
{
  "error": "Validation failed",
  "details": {
    "nom": "nom is required",
    "tempsPreparation": "must be > 0"
  }
}
```

**Events Publiés:**
- Topic: `recette-events`
- Event Type: `RecetteCreatedEvent`
- Payload:
  ```json
  {
    "eventId": "uuid-1234",
    "recetteId": 5,
    "nom": "Pizza Margherita",
    "timestamp": "2025-01-16T10:45:00Z",
    "eventType": "RECETTE_CREATED"
  }
  ```

---

#### `PUT /recettes/{id}` - Mettre à jour une recette
**Type:** Command  
**Description:** Met à jour une recette existante

**Path Parameters:**
- `id` (long, required) - ID de la recette

**Request Body:**
```json
{
  "nom": "Pizza Napolitaine",
  "description": "Pizza traditionnelle napolitaine",
  "tempsPreparation": 30,
  "tempsCuisson": 20
}
```

**Response:** 200 OK
```json
{
  "id": 1,
  "nom": "Pizza Napolitaine",
  "description": "Pizza traditionnelle napolitaine",
  "tempsPreparation": 30,
  "tempsCuisson": 20,
  "updatedAt": "2025-01-16T11:00:00Z"
}
```

**Events Publiés:**
- Event Type: `RecetteUpdatedEvent`

---

#### `DELETE /recettes/{id}` - Supprimer une recette
**Type:** Command  
**Description:** Supprime une recette

**Path Parameters:**
- `id` (long, required) - ID de la recette

**Response:** 204 No Content

**Events Publiés:**
- Event Type: `RecetteDeletedEvent`

---

### INGREDIENTS

#### `GET /recettes/{recetteId}/ingredients` - Lister les ingrédients
**Type:** Query

**Path Parameters:**
- `recetteId` (long, required) - ID de la recette

**Response:** 200 OK
```json
[
  {
    "id": 1,
    "nom": "Tomate",
    "quantite": "500g",
    "prix": 2.50,
    "unite": "g",
    "createdAt": "2025-01-16T10:30:00Z"
  }
]
```

---

#### `GET /recettes/{recetteId}/ingredients/{id}` - Récupérer un ingrédient

**Response:** 200 OK
```json
{
  "id": 1,
  "nom": "Tomate",
  "quantite": "500g",
  "prix": 2.50,
  "unite": "g",
  "recetteId": 1,
  "createdAt": "2025-01-16T10:30:00Z"
}
```

---

#### `POST /recettes/{recetteId}/ingredients` - Ajouter un ingrédient
**Type:** Command

**Request Body:**
```json
{
  "nom": "Tomate",
  "quantite": "500g",
  "prix": 2.50,
  "unite": "g"
}
```

**Response:** 201 Created
```json
{
  "id": 1,
  "nom": "Tomate",
  "quantite": "500g",
  "prix": 2.50,
  "unite": "g",
  "recetteId": 1,
  "createdAt": "2025-01-16T10:30:00Z"
}
```

---

#### `PUT /recettes/{recetteId}/ingredients/{id}` - Mettre à jour un ingrédient
**Type:** Command

---

#### `DELETE /recettes/{recetteId}/ingredients/{id}` - Supprimer un ingrédient
**Type:** Command

**Response:** 204 No Content

---

## 🛡️ Résilience & Error Handling

### Retry Policy
```
Max Attempts: 3
Wait Duration: 1s
Exponential Backoff: 2x multiplier
→ 1s, 2s, 4s delays
```

### Circuit Breaker
```
Failure Rate Threshold: 50%
Minimum Calls: 10
Wait Duration (Open→HalfOpen): 60s
```

### Rate Limiter
```
Limit: 100 requêtes/minute
Timeout: 5 secondes
```

### Status Codes

| Code | Meaning | Retry ? |
|------|---------|---------|
| 200 | OK | Non |
| 201 | Created | Non |
| 204 | No Content | Non |
| 400 | Bad Request | Non |
| 404 | Not Found | Non |
| 409 | Conflict | Non |
| 429 | Too Many Requests | Oui (Rate Limited) |
| 500 | Internal Server Error | Oui (Retry) |
| 503 | Service Unavailable | Oui (Circuit Breaker Open) |

---

## 📊 Monitoring & Health Checks

### Health Endpoint
```bash
GET /actuator/health
```

Response:
```json
{
  "status": "UP",
  "components": {
    "circuitBreakers": {
      "status": "UP",
      "details": {
        "recette-command-service": "CLOSED"
      }
    },
    "retries": {
      "status": "UP"
    },
    "rateLimiters": {
      "status": "UP"
    },
    "db": {
      "status": "UP",
      "database": "PostgreSQL"
    },
    "kafka": {
      "status": "UP"
    }
  }
}
```

### Metrics
```bash
GET /actuator/metrics
GET /actuator/metrics/resilience4j.circuitbreaker.state
GET /actuator/metrics/resilience4j.retry.attempts
```

---

## 🧪 Fichiers de Test

### Unit Tests
- `src/test/java/.../entity/RecetteTest.java`
- `src/test/java/.../service/RecetteCommandServiceTest.java`

### Integration Tests
- `src/test/java/.../integration/RecetteIntegrationTest.java`

### BDD Tests (Cucumber)
- `src/test/resources/features/recette.feature`
- `src/test/resources/features/cqrs.feature`
- `src/test/java/.../bdd/RecetteStepDefs.java`

---

## 🚀 Quick Start

### 1. Lancer l'application
```bash
task run
```

### 2. Tester avec cURL
```bash
curl http://localhost:8080/recettes
```

### 3. Importer dans Postman
- Ouvrir Postman
- File → Import
- Sélectionner `recettes.postman_collection.json`
- Cliquer sur une requête et "Send"

### 4. Utiliser REST Client (VS Code)
- Installer extension "REST Client"
- Ouvrir `recettes.http`
- Cliquer sur "Send Request" au-dessus de chaque bloc

---

## 📚 Documentation complémentaire

- [ADR-001: Architecture CQRS](../.md/ADR-001-ARCHITECTURE-CQRS.md)
- [ADR-002: Kafka Message Bus](../.md/ADR-002-KAFKA-MESSAGE-BUS.md)
- [ADR-003: Résilience Strategy](../.md/ADR-003-RESILIENCE-STRATEGY.md)
- [Documentation complète](../.md/DOCUMENTATION.md)

---

**Last Updated:** Janvier 2025  
**API Version:** 1.0.0  
**Status:** Production Ready ✅
