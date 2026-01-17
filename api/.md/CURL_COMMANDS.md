# API Testing - cURL Commands

## Configuration
```bash
BASE_URL="http://localhost:8080"
CONTENT_TYPE="Content-Type: application/json"
```

## RECETTES - OPERATIONS

### 1. Lister toutes les recettes (Query)
```bash
curl -X GET "${BASE_URL}/recettes" \
  -H "Accept: application/json"
```

### 2. Récupérer une recette par ID (Query)
```bash
curl -X GET "${BASE_URL}/recettes/1" \
  -H "Accept: application/json"
```

### 3. Créer une nouvelle recette (Command)
```bash
curl -X POST "${BASE_URL}/recettes" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Pizza Margherita",
    "description": "Pizza classique avec tomate, mozzarella et basilic",
    "tempsPreparation": 30,
    "tempsCuisson": 15
  }'
```

### 4. Mettre à jour une recette (Command)
```bash
curl -X PUT "${BASE_URL}/recettes/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Pizza Napolitaine",
    "description": "Pizza traditionnelle napolitaine",
    "tempsPreparation": 30,
    "tempsCuisson": 20
  }'
```

### 5. Supprimer une recette (Command)
```bash
curl -X DELETE "${BASE_URL}/recettes/1" \
  -H "Accept: application/json"
```

---

## INGREDIENTS - OPERATIONS

### 6. Récupérer tous les ingrédients d'une recette (Query)
```bash
curl -X GET "${BASE_URL}/recettes/1/ingredients" \
  -H "Accept: application/json"
```

### 7. Récupérer un ingrédient spécifique (Query)
```bash
curl -X GET "${BASE_URL}/recettes/1/ingredients/1" \
  -H "Accept: application/json"
```

### 8. Ajouter un ingrédient (Command)
```bash
curl -X POST "${BASE_URL}/recettes/1/ingredients" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Tomate",
    "quantite": "500g",
    "prix": 2.50,
    "unite": "g"
  }'
```

### 9. Mettre à jour un ingrédient (Command)
```bash
curl -X PUT "${BASE_URL}/recettes/1/ingredients/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Tomate bio",
    "quantite": "600g",
    "prix": 3.50,
    "unite": "g"
  }'
```

### 10. Supprimer un ingrédient (Command)
```bash
curl -X DELETE "${BASE_URL}/recettes/1/ingredients/1" \
  -H "Accept: application/json"
```

---

## RECHERCHE & PAGINATION

### 11. Rechercher des recettes par nom
```bash
curl -X GET "${BASE_URL}/recettes?nom=Pizza" \
  -H "Accept: application/json"
```

### 12. Pagination - Page 1 (10 éléments)
```bash
curl -X GET "${BASE_URL}/recettes?page=0&size=10" \
  -H "Accept: application/json"
```

### 13. Trier par nom (ordre ascendant)
```bash
curl -X GET "${BASE_URL}/recettes?sort=nom,asc" \
  -H "Accept: application/json"
```

---

## SANTÉ & MONITORING

### 14. Health Check
```bash
curl -X GET "${BASE_URL}/actuator/health" \
  -H "Accept: application/json"
```

### 15. Métriques Resilience4J
```bash
curl -X GET "${BASE_URL}/actuator/metrics" \
  -H "Accept: application/json"
```

### 16. État du Circuit Breaker
```bash
curl -X GET "${BASE_URL}/actuator/metrics/resilience4j.circuitbreaker.state" \
  -H "Accept: application/json"
```

---

## SCRIPTS D'INTEGRATION

### Créer une recette avec ingrédients (Batch)
```bash
#!/bin/bash

# 1. Créer la recette
RECIPE_ID=$(curl -s -X POST "${BASE_URL}/recettes" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Pâtes Carbonara",
    "description": "Pâtes classiques italiennes",
    "tempsPreparation": 10,
    "tempsCuisson": 15
  }' | jq -r '.id')

echo "Recette créée avec ID: $RECIPE_ID"

# 2. Ajouter ingrédients
for ingredient in \
  '{"nom":"Pâtes","quantite":"500g","prix":1.50,"unite":"g"}' \
  '{"nom":"Oeufs","quantite":"4","prix":2.00,"unite":"pièces"}' \
  '{"nom":"Pancetta","quantite":"200g","prix":4.50,"unite":"g"}' \
  '{"nom":"Fromage","quantite":"100g","prix":3.00,"unite":"g"}'
do
  curl -s -X POST "${BASE_URL}/recettes/${RECIPE_ID}/ingredients" \
    -H "Content-Type: application/json" \
    -d "$ingredient" | jq '.'
done

echo "Ingrédients ajoutés!"
```

### Tester la résilience (Retry Policy)
```bash
#!/bin/bash

# Teste si les retries fonctionnent
for i in {1..5}; do
  echo "Tentative $i..."
  curl -X POST "${BASE_URL}/recettes" \
    -H "Content-Type: application/json" \
    -d '{
      "nom": "Test Recette '$i'",
      "description": "Test de resilience",
      "tempsPreparation": 10,
      "tempsCuisson": 20
    }' \
    -w "\nStatus: %{http_code}\n\n"
  sleep 1
done
```

---

## NOTES

### En développement
```bash
# Lancer l'application
task run

# Lancer les tests BDD
task test:bdd

# Vérifier la qualité du code
task verify
```

### Endpoints Actuator disponibles
- `/actuator/health` - État de santé
- `/actuator/metrics` - Toutes les métriques
- `/actuator/env` - Variables d'environnement
- `/actuator/loggers` - Configuration des logs
- `/swagger-ui.html` - Documentation API (si Swagger activé)

