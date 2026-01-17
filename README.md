# TP - Architecture Logicielle et Qualité

Application de gestion de recettes avec **CQRS**, **Kafka** et **Resilience4J**.

---

## ⚡ Quick Start

```bash
# Démarrer
task start

# Attendre
task health

# API sur http://localhost:8080
```

Arrêter: `task stop`

---

## 📚 Tester l'API

**Méthode recommandée:** Fichier `.http` dans VS Code

1. Installer [REST Client extension](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)
2. Ouvrir [api/recettes.http](api/recettes.http)
3. Cliquer "Send Request"

Ou: `task api` pour ouvrir le fichier

---

## 🏗️ Architecture

**CQRS:** Séparation lecture (Query) / écriture (Command)

```
API REST
  ├─ Queries (GET) → Query Service (Lecture)
  └─ Commands (POST/PUT/DELETE) → Command Service (Écriture)
       ↓
    Kafka (Événements)
```

**Stack:** Java 17, Spring Boot 4.0.0, Kafka, H2, Resilience4J, Docker

---

## 🧪 Tests

```bash
task test              # Tous les tests
```

---

## 📋 Commandes Essentielles

```bash
task start             # Démarrer (build + docker up)
task stop              # Arrêter (docker down + clean)
task build             # Compiler
task test              # Tests
task logs              # Voir logs
task health            # Vérifier santé
task api               # Ouvrir API (recettes.http)
```

Voir toutes: `task --list`

---

## 🎯 ADR (Décisions Architecturales)

- [ADR-001: CQRS](ADR/ADR-001-ARCHITECTURE-CQRS.md)
- [ADR-002: Kafka](ADR/ADR-002-KAFKA-MESSAGE-BUS.md)
- [ADR-003: Resilience4J](ADR/ADR-003-RESILIENCE-STRATEGY.md)

---

## 📂 Structure

```
src/main/java/Architecture_log/TP/
├── commands/    # Write Model
├── queries/     # Read Model
├── controller/  # API REST
└── entity/      # Entities

api/
├── recettes.http          # Endpoints à tester
└── *.postman_collection.json

ADR/                      # Décisions architecturales
```

---

## 👥 Auteurs

- Achraf EL ALLALI
- Wiam ABDELLAOUI

**Janvier 2025**

---

## 📖 Documentation

- **Architecture Decisions:** [ADR/](ADR/)
- **JavaDoc:** `mvn javadoc:javadoc` puis `target/reports/apidocs/index.html`
