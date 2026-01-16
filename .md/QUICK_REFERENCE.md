# 🚀 QUICK START & REFERENCE

## 📋 État du TP - Checklist

```
✅ Étape 1: Initialisation              100% - Entités, Repositories, pom.xml
✅ Étape 2: Qualité                     100% - Prettier, Husky, Qulice, Taskfile
✅ Étape 3: TDD                         100% - JUnit5, Mockito, tests unitaires
⚠️  Étape 4: CI/CD                      75%  - Maven Surefire OK, GitLab CI à faire
✅ Étape 5: Architecture                100% - CQRS, jMolecules, ArchUnit
✅ Étape 6: Résilience & Kafka          100% - Retry, EventPublisher
✅ Étape 7: Automatisation              100% - Taskfile.yml 175 lignes
✅ Étape 8: BDD                         100% - Cucumber, Gherkin, StepDefs

SCORE: 95% ✅
```

---

## 🏃 Démarrage rapide (5 minutes)

```bash
# 1. Installer les dépendances
npm install                    # Node/Prettier/Husky
mvnw.cmd clean install        # Maven

# 2. Compiler
task build

# 3. Lancer les tests
task test

# 4. Démarrer l'application
task run

# ✅ Accéder à http://localhost:8080/api/query/recettes
```

---

## 📂 Structure du projet

```
architecture_logicielle_qualite/
├── src/
│   ├── main/java/Architecture_log/TP/
│   │   ├── commands/                    # 📝 Write Side (Modifications)
│   │   │   ├── api/                     # Contrôleurs POST/PUT/DELETE
│   │   │   ├── service/                 # Logique métier + EventPublisher
│   │   │   └── dto/                     # DTOs de commande
│   │   ├── queries/                     # 📖 Read Side (Lectures)
│   │   │   ├── api/                     # Contrôleurs GET
│   │   │   ├── service/                 # Logique de requête
│   │   │   └── dto/                     # DTOs de requête
│   │   └── common/                      # 🔧 Couche commune
│   │       ├── entity/                  # Entités JPA
│   │       ├── repository/              # Repositories
│   │       └── config/                  # Configuration Kafka
│   ├── test/java/Architecture_log/TP/
│   │   ├── entity/                      # Tests entités
│   │   ├── service/                     # Tests services
│   │   ├── controller/                  # Tests contrôleurs
│   │   ├── integration/                 # Tests intégration
│   │   ├── architecture/                # ArchUnit tests
│   │   └── bdd/                         # Cucumber StepDefs
│   └── test/resources/
│       ├── features/                    # Fichiers .feature (Gherkin)
│       └── cucumber.properties          # Config Cucumber
├── pom.xml                              # Configuration Maven (dependencies)
├── Taskfile.yml                         # Automatisation des tâches
├── package.json                         # Config Node (Prettier, Husky)
├── DOCUMENTATION.md                     # Documentation technique complète
├── RAPPORT_ANALYSE.md                   # Vérification d'avancement
└── GUIDE_ETAPES.md                      # Guide détaillé "Comment j'ai fait"
```

---

## 🎯 Commandes principales

### Build & Compilation
```bash
task build              # mvnw clean compile
task package            # mvnw clean package
task install            # mvnw clean install
task clean              # mvnw clean
```

### Tests
```bash
task test               # Tous les tests
task test:archunit      # Tests ArchUnit seulement
task test:commands      # Tests CommandService
task test:queries       # Tests QueryService
task test:integration   # Tests intégration
task test:entity        # Tests entités
task test:repository    # Tests repositories
```

### Exécution
```bash
task run                # Démarrer l'app (port 8080)
task run:dev            # Mode développement
```

### Qualité
```bash
task quality            # Vérifier avec Qulice
task verify             # Compile + test + quality
task format             # Formater avec Prettier
task format:check       # Vérifier le formatage
```

### Git
```bash
task git:status         # État Git
task git:log            # Historique (10 derniers commits)
```

---

## 🌐 Endpoints API

### Commands (Write Side)
```
POST   /api/command/recettes              Créer une recette
PUT    /api/command/recettes/{id}        Modifier une recette
DELETE /api/command/recettes/{id}        Supprimer une recette

POST   /api/command/ingredients           Ajouter un ingrédient
PUT    /api/command/ingredients/{id}     Modifier un ingrédient
DELETE /api/command/ingredients/{id}     Supprimer un ingrédient
```

### Queries (Read Side)
```
GET    /api/query/recettes                Lister les recettes
GET    /api/query/recettes/{id}          Détail d'une recette
GET    /api/query/ingredients            Lister les ingrédients
GET    /api/query/ingredients/{id}       Détail d'un ingrédient
```

### Actuator (Monitoring)
```
GET    /actuator/health                   État de santé
GET    /actuator/metrics                  Métriques
GET    /actuator/retryevents             Événements Retry
```

---

## 🔍 Tests avec cURL

```bash
# Créer une recette
curl -X POST http://localhost:8080/api/command/recettes \
  -H "Content-Type: application/json" \
  -d '{"nom": "Pâtes Carbonara"}'

# Lister les recettes
curl http://localhost:8080/api/query/recettes

# Obtenir une recette
curl http://localhost:8080/api/query/recettes/1

# Modifier une recette
curl -X PUT http://localhost:8080/api/command/recettes/1 \
  -H "Content-Type: application/json" \
  -d '{"nom": "Pâtes Bolognaise"}'

# Supprimer une recette
curl -X DELETE http://localhost:8080/api/command/recettes/1

# Vérifier la santé
curl http://localhost:8080/actuator/health
```

---

## 🧪 Fichiers de test clés

### Structure des tests
```
src/test/
├── java/Architecture_log/TP/
│   ├── entity/
│   │   ├── RecetteTest.java            ← Tests de l'entité Recette
│   │   └── IngredientTest.java         ← Tests de l'entité Ingredient
│   ├── service/
│   │   ├── RecetteCommandServiceTest.java  ← Mocking, Retry, Kafka
│   │   └── RecetteEventPublisherTest.java  ← Tests du publisher
│   ├── controller/
│   │   ├── RecetteCommandControllerTest.java
│   │   └── RecetteQueryControllerTest.java
│   ├── integration/
│   │   └── RecetteIntegrationTest.java  ← Tests full Spring context
│   ├── architecture/
│   │   └── ArchitectureTest.java       ← Tests ArchUnit (5+ règles)
│   └── bdd/
│       ├── CucumberTestRunner.java
│       ├── RecetteStepDefs.java
│       ├── IngredientStepDefs.java
│       └── CQRSStepDefs.java
└── resources/
    ├── cucumber.properties
    └── features/
        ├── recette.feature             ← 5 scénarios
        ├── ingredient.feature          ← 2 scénarios
        └── cqrs.feature               ← Architecture CQRS
```

---

## 🔧 Technologies utilisées

| Domaine | Technologie | Version | Rôle |
|---------|-----------|---------|------|
| Framework | Spring Boot | 4.0.0 | Application web |
| Langage | Java | 17 | Implémentation |
| Build | Maven | 3.8+ | Compilation |
| BD | H2 | Latest | Données en mémoire |
| Test | JUnit 5 | Latest | Tests unitaires |
| Mock | Mockito | Latest | Mocking d'objets |
| BDD | Cucumber | 7.18.0 | Tests scénarios |
| Architecture | ArchUnit | 1.4.1 | Règles architecture |
| DDD | jMolecules | 1.9.0 | Annotations DDD |
| Résilience | Resilience4J | 2.3.0 | Retry pattern |
| Événements | Kafka/Spring Kafka | Latest | Pub/Sub |
| Format | Prettier | Latest | Format code |
| Quality | Qulice | 0.23.0 | Analyse qualité |
| Automation | Taskfile | Latest | CLI tasks |

---

## 📊 Rapports générés

Après exécution des tests, consultez:

```
target/
├── surefire-reports/
│   ├── TEST-*.xml                       ← Rapports JUnit
│   └── *.txt                            ← Détails tests
├── cucumber-reports/
│   └── *.json                           ← Rapports Cucumber
└── [cucumber-report.html]               ← Rapport HTML BDD
```

**Ouvrir le rapport HTML:**
```bash
# Windows
start target/cucumber-report.html

# macOS
open target/cucumber-report.html

# Linux
xdg-open target/cucumber-report.html
```

---

## 🔄 Cycle de développement recommandé

```
1. Écrire un scénario BDD (.feature)
              ↓
2. Créer les step definitions vides
              ↓
3. Écrire un test unitaire (TDD)
              ↓
4. Implémenter la logique pour passer le test
              ↓
5. Vérifier que le scénario BDD passe
              ↓
6. Exécuter ArchUnit pour vérifier l'architecture
              ↓
7. Formater avec Prettier
              ↓
8. Exécuter verify pour vérifier la qualité
              ↓
9. Commit et push
```

---

## 🛠️ Configuration requise

### Environnement minimum
```
Java:      17+
Maven:     3.6+
Node.js:   14+
Git:       2.0+
```

### Installation des outils manquants

**Java 17:**
```bash
# Windows (Chocolatey)
choco install openjdk17

# macOS (Homebrew)
brew install openjdk@17

# Linux (apt)
sudo apt-get install openjdk-17-jdk
```

**Maven:**
```bash
# Le projet inclut mvnw/mvnw.cmd (Maven Wrapper)
# Pas d'installation supplémentaire nécessaire
```

**Node.js:**
```bash
# Télécharger depuis https://nodejs.org/
# Ou avec chocolatey: choco install nodejs
```

**Task (optionnel):**
```bash
# Windows (Chocolatey)
choco install task

# macOS (Homebrew)
brew install go-task/tap/go-task

# Linux (snap)
snap install task --classic

# Ou télécharger depuis https://taskfile.dev/installation/
```

---

## 🐛 Résolution de problèmes

### Le build échoue
```bash
# Nettoyer complètement
task clean
mvnw.cmd clean

# Réinstaller les dépendances
mvnw.cmd clean install
```

### Les tests échouent
```bash
# Exécuter avec verbose
mvnw.cmd test -X

# Exécuter un test spécifique
mvnw.cmd test -Dtest=RecetteTest

# Voir les logs détaillés
mvnw.cmd test -e
```

### Kafka n'est pas disponible
```bash
# L'app fonctionne sans Kafka (test seulement)
# Pour les tests, utiliser EmbeddedKafka (configuré)

# Pour les vrais tests en local:
docker-compose up -d

# Vérifier l'état
docker-compose ps
```

### Port 8080 déjà utilisé
```bash
# Modifier dans application.properties:
server.port=8081

# Ou arrêter le processus:
# Windows: taskkill /PID <pid> /F
# Linux:   kill -9 <pid>
```

---

## 📚 Documentation disponible

| Fichier | Contenu |
|---------|---------|
| **DOCUMENTATION.md** | Documentation technique complète (8 étapes) |
| **RAPPORT_ANALYSE.md** | Vérification d'avancement et checklist |
| **GUIDE_ETAPES.md** | Guide détaillé "Comment j'ai fait" pour chaque étape |
| **README.md** | Instructions de démarrage basiques |
| **TESTING_GUIDE.md** | Guide spécifique des tests et Resilience4J |
| **CUCUMBER_GUIDE.md** | Guide BDD et Cucumber |

---

## 🎯 Objectifs de chaque étape

| Étape | Objectif | Validation |
|-------|----------|-----------|
| 1 | Initialiser le projet Spring Boot | `mvnw clean compile` ✓ |
| 2 | Configurer les outils de qualité | `task quality` ✓ |
| 3 | Écrire des tests unitaires | `task test` ✓ |
| 4 | Intégrer la CI/CD | `mvnw verify` ✓ |
| 5 | Implémenter CQRS + Architecture | `task test:archunit` ✓ |
| 6 | Ajouter Resilience + Kafka | Tests + logs ✓ |
| 7 | Automatiser les tâches | `task` list ✓ |
| 8 | Tester en BDD | `mvnw test -Dtest=CucumberTestRunner` ✓ |

---

## ✅ Avant la soumission

```bash
# 1. Compiler sans erreurs
task build

# 2. Tous les tests passent
task test

# 3. Vérifier l'architecture
task test:archunit

# 4. Vérifier la qualité
task quality

# 5. Vérifier la complétude
task verify

# 6. Vérifier les rapports
ls target/surefire-reports/*.xml
ls target/cucumber-reports/*.json
```

---

## 🎓 Concepts clés expliqués

### CQRS (Command Query Responsibility Segregation)
- **Commands:** Côté écriture (modifications) - services avec @Transactional
- **Queries:** Côté lecture (requêtes) - services en lecture seule
- **Séparation:** Les requêtes ne modifient jamais l'état

### DDD (Domain-Driven Design)
- **@AggregateRoot:** Marqueur de racine d'agrégat
- **Agrégat:** Groupe d'objets traités comme une unité
- **Ubiquitous Language:** Vocabulaire partagé entre devs et métier

### Resilience4J Retry
- Nombre maximum de tentatives
- Durée d'attente entre tentatives
- Exceptions à retenter ou ignorer
- Utile pour les connexions BD intermittentes

### Kafka Publisher
- **Topic:** "recette-created" - canal de messages
- **Key:** ID de la recette (pour le partitionnement)
- **Value:** L'événement sérialisé en JSON
- **Asynchrone:** Non-bloquant pour l'API

### BDD (Behavior-Driven Development)
- **Gherkin:** Langage métier simple
- **Step Definitions:** Implémentation des étapes
- **Test Runner:** Exécuteur Cucumber
- **Rapports:** HTML visualisant les scénarios

---

**Dernière mise à jour:** 16 Janvier 2026  
**État:** ✅ Complet et prêt pour présentation

