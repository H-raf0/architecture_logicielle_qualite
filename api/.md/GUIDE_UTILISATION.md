# 🔧 Guide d'Utilisation - Requêtes API

## 📌 Vue d'ensemble des outils

Vous avez 4 options pour tester l'API REST:

| Outil | Fichier | Avantage | OS |
|-------|---------|----------|-----|
| **REST Client** | `recettes.http` | Intégré VS Code | All |
| **Postman** | `recettes.postman_collection.json` | Interface graphique | All |
| **cURL** | Terminal | Lightweight, script-friendly | All |
| **Taskfile** | `task api:*` | Intégration avec build | All |

---

## 1️⃣ REST Client (VS Code) ⭐ RECOMMANDÉ

### Installation
1. Ouvrir VS Code
2. Extensions → Rechercher "REST Client"
3. Installer par "Huachao Mao"

### Utilisation
1. Ouvrir le fichier: `api/recettes.http`
2. Regarder les blocs de requête séparés par `###`
3. Cliquer sur **"Send Request"** au-dessus de chaque bloc
4. Voir la réponse dans un onglet dédié

### Exemple
```http
### 1. Lister toutes les recettes (Query)
GET {{baseUrl}}/recettes
Accept: {{contentType}}
```

**Résultat:** Onglet de réponse avec JSON formaté

### Variables
```http
@baseUrl = http://localhost:8080
@contentType = application/json
```

Modifiez la première ligne pour changer l'environnement.

---

## 2️⃣ Postman Collection

### Installation
1. Télécharger ou ouvrir : `api/recettes.postman_collection.json`
2. Ouvrir Postman (https://www.postman.com/downloads/)
3. **File** → **Import**
4. Sélectionner le fichier JSON
5. Cliquer **Import**

### Utilisation
1. La collection **"API Gestion des Recettes"** apparaît à gauche
2. Dossiers: **Recettes**, **Ingrédients**, **Recherche & Pagination**, **Santé**
3. Cliquer sur une requête
4. Voir le détail au centre
5. Cliquer **Send**
6. Voir la réponse en bas

### Variables d'environnement
```
baseUrl = http://localhost:8080
contentType = application/json
```

**Modifier les variables:**
1. Coin supérieur droit → **Settings** (⚙️)
2. **Environments**
3. Editer **recettes** environment
4. Changer les valeurs

---

## 3️⃣ cURL (Terminal)

### Commande basique
```bash
curl http://localhost:8080/recettes
```

### Avec options
```bash
curl -X GET "http://localhost:8080/recettes" \
  -H "Accept: application/json"
```

### Voir le fichier de commandes
```bash
cat api/CURL_COMMANDS.md
```

### Copier-coller une commande
1. Ouvrir `api/CURL_COMMANDS.md`
2. Copier le bloc de code
3. Coller dans le terminal
4. Appuyer sur Enter

### Exécuter un script
```bash
bash api/scripts/test-integration.sh
```

### Sur Windows (PowerShell)
```powershell
$body = @{
    nom = "Pizza Margherita"
    tempsPreparation = 30
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/recettes" `
  -Method POST `
  -Body $body `
  -ContentType "application/json"
```

---

## 4️⃣ Taskfile Integration

### Commandes disponibles
```bash
task api:recettes:list          # GET /recettes
task api:recettes:get           # GET /recettes/1
task api:recettes:create        # POST /recettes
task api:recettes:update        # PUT /recettes/1
task api:recettes:delete        # DELETE /recettes/1
task api:ingredients:list       # GET /recettes/1/ingredients
task api:ingredients:create     # POST /recettes/1/ingredients
```

### Utilisation
```bash
# Installer Task si nécessaire
# https://taskfile.dev/installation/

# Lancer l'application
task run

# Dans un autre terminal, tester l'API
task api:recettes:list

# Voir toutes les commandes
task --list
```

---

## 📊 Comparaison détaillée

### REST Client (VS Code)
```
✅ Avantages:
  - Intégré dans VS Code
  - Pas d'installation supplémentaire
  - Variables de remplacement automatique
  - Historique des requêtes
  - Parfait pour développement
  
❌ Inconvénients:
  - Moins d'options de test avancées
  - Pas d'interface graphique complète
  - Scripts limités
```

### Postman
```
✅ Avantages:
  - Interface graphique intuitive
  - Tests & assertions intégrés
  - Environment management puissant
  - Collections organisées
  - Collaboration simple
  
❌ Inconvénients:
  - Application externe à installer
  - Plus lourde que cURL
  - Version gratuite limitée
```

### cURL
```
✅ Avantages:
  - Lightweight
  - Scriptable
  - Disponible partout (Linux, Mac, Windows)
  - Parfait pour CI/CD
  - Pas dépendance
  
❌ Inconvénients:
  - Syntaxe complexe pour requêtes avancées
  - Pas de GUI
  - Difficile pour les tests complexes
```

### Taskfile
```
✅ Avantages:
  - Commandes mémorisées
  - Intégration build/test/API
  - Documenté en français
  - Facile à partager
  
❌ Inconvénients:
  - Requiert installation de Task
  - Commandes pré-définies uniquement
```

---

## 🎯 Cas d'usage recommandés

### Développement local
👉 **REST Client** ou **Postman**
- Rapide et interactif
- Modification facile des requêtes
- Voir les détails complets

### Tests automatisés (CI/CD)
👉 **cURL** ou **Postman (en CLI)**
```bash
# Exemple script
curl -X GET http://localhost:8080/recettes \
  -w "\nStatus: %{http_code}\n"
```

### Prototypage / Démonstration
👉 **Postman Collection**
- Interface visuelle
- Partager facilement
- Montrer les étapes

### Intégration avec le workflow
👉 **Taskfile**
```bash
task run              # Démarrer l'app
task test             # Tests
task api:recettes:list # Vérifier que ça marche
```

---

## 🔄 Workflow Complet

### Scénario 1: Développement
```bash
# Terminal 1: Lancer l'app
task run

# Terminal 2: Tester l'API
# Option A: REST Client dans VS Code
# Option B: Postman
# Option C: cURL
```

### Scénario 2: CI/CD Pipeline
```bash
#!/bin/bash
# Build
mvn clean package

# Test
mvn test

# Vérifier que l'app démarre
task run &
sleep 5

# Tester l'API avec cURL
curl -f http://localhost:8080/recettes || exit 1
echo "API test passed!"
```

### Scénario 3: Démonstration
```
1. Ouvrir Postman
2. Importer recettes.postman_collection.json
3. Montrer les endpoints
4. Exécuter les requêtes
5. Expliquer CQRS & Résilience
```

---

## 🐛 Dépannage

### Erreur: "Connection refused"
```
❌ curl: (7) Failed to connect to localhost:8080
```

**Solution:**
```bash
# Vérifier que l'app est démarrée
task run

# Ou vérifier le port
netstat -an | grep 8080  # Linux/Mac
netstat -ano | grep 8080 # Windows
```

### Erreur: "404 Not Found"
```
❌ {"error":"Recette non trouvée"}
```

**Solution:**
```bash
# Vérifier l'ID existe
curl http://localhost:8080/recettes

# Créer une recette d'abord
curl -X POST http://localhost:8080/recettes \
  -H "Content-Type: application/json" \
  -d '{"nom":"Pizza"}'
```

### Erreur: "503 Service Unavailable"
```
❌ Circuit breaker is OPEN
```

**Solution:**
```bash
# Vérifier la health
curl http://localhost:8080/actuator/health

# Attendre 60s (Circuit breaker timeout)
# Ou redémarrer l'app
task run
```

### Postman: Import échoue
**Solution:**
```
1. Vérifier que le fichier n'est pas corrompu
2. Ouvrir le JSON dans un éditeur
3. Vérifier la structure {} valide
4. Essayer File > Import (et non drag-drop)
```

---

## 📋 Checklist d'utilisation

### Avant de tester
- [ ] Application en cours d'exécution (`task run`)
- [ ] Port 8080 disponible
- [ ] Kafka démarré (optionnel, mais recommandé)
- [ ] Base de données accessible

### Première requête (Recommandé)
```bash
curl http://localhost:8080/recettes
```

Si réponse vide `[]`, normal (première utilisation).

### Créer une recette
```bash
curl -X POST http://localhost:8080/recettes \
  -H "Content-Type: application/json" \
  -d '{"nom":"Pizza Margherita","tempsPreparation":30,"tempsCuisson":15}'
```

Copier l'ID retourné (ex: `"id":1`)

### Vérifier la création
```bash
curl http://localhost:8080/recettes/1
```

---

## 🎓 Ressources supplémentaires

- [REST Client Docs](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)
- [Postman Docs](https://learning.postman.com/)
- [cURL Docs](https://curl.se/docs/)
- [API README](README.md) - Documentation complète

---

**Version:** 1.0  
**Last Updated:** Janvier 2025  
**Questions?** Voir [DOCUMENTATION.md](../.md/DOCUMENTATION.md)
