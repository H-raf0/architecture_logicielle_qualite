# 🐳 Guide Docker & Docker Compose

## 📋 Table des matières
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Services](#services)
- [Dépannage](#dépannage)
- [Architecture](#architecture)

---

## 🚀 Installation

### Prérequis
- **Docker Desktop** (v20.10+) : https://www.docker.com/products/docker-desktop
- **Docker Compose** (v1.29+) : Inclus avec Docker Desktop

### Vérification de l'installation
```bash
docker --version
docker-compose --version
```

---

## 🎯 Utilisation

### Démarrer tous les services
```bash
docker-compose up -d
```

**Output attendu:**
```
Creating network "app-network" with driver "bridge"
Creating kafka ... done
Creating kafka-ui ... done
Building app
...
Creating architecture-app ... done
```

### Arrêter tous les services
```bash
docker-compose down
```

### Arrêter et nettoyer les volumes
```bash
docker-compose down -v
```

### Voir l'état des services
```bash
docker-compose ps
```

### Afficher les logs
```bash
# Tous les services
docker-compose logs -f

# Un service spécifique
docker-compose logs -f app
docker-compose logs -f kafka

# Dernières N lignes
docker-compose logs --tail=50
```

### Accéder à l'application
```
API: http://localhost:8080
Kafka UI: http://localhost:8081
H2 Console: http://localhost:8080/h2-console (si activée)
```

---

## 🔧 Services

### 1. **app** (Application Spring Boot)
- **Port:** 8080
- **Build:** Multi-stage avec Maven
- **Base de données:** H2 en mémoire
- **Message Broker:** Kafka
- **Healthcheck:** Chaque 30 secondes

**Variables d'environnement principales:**
```
SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
LOGGING_LEVEL_ARCHITECTURE_LOG=DEBUG
```

### 2. **kafka** (Message Broker)
- **Port:** 9092 (interne) / 9092 (externe)
- **Image:** apache/kafka:latest
- **Partitions:** 3
- **Replication Factor:** 1
- **Healthcheck:** Chaque 10 secondes

**Fonctionnalités:**
- Auto-création des topics
- Persistance des messages
- Intégration avec l'app

### 3. **kafka-ui** (Interface utilisateur Kafka)
- **Port:** 8081
- **Image:** provectuslabs/kafka-ui:latest
- **Fonctionnalités:**
  - Visualiser les brokers
  - Explorer les topics
  - Voir les messages
  - Gérer les consumer groups

**Accès:** http://localhost:8081

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Docker Network                        │
│                    (app-network)                         │
├─────────────────────────────────────────────────────────┤
│                                                           │
│  ┌──────────────────┐        ┌─────────────┐           │
│  │   app:8080       │◄──────►│  kafka:9092 │           │
│  │  (Spring Boot)   │        │  (Broker)   │           │
│  └──────────────────┘        └─────────────┘           │
│         ▲                            ▲                   │
│         │                            │                   │
│    (port 8080)                 (port 9092)             │
│         │                            │                   │
│    [HOST]                      [HOST]                    │
│         │                            │                   │
│    localhost:8080         kafka-ui:8081                  │
│                           ◄──────────┘                   │
│                                                           │
└─────────────────────────────────────────────────────────┘
```

---

## 🐛 Dépannage

### 1. Le port 8080 est déjà utilisé
```bash
# Modifier docker-compose.yml
ports:
  - "8089:8080"  # Utiliser 8089 au lieu de 8080

# Puis reconstruire
docker-compose up -d --build
```

### 2. Le port 9092 (Kafka) est déjà utilisé
```bash
# Voir quel processus l'utilise (Linux/Mac)
lsof -i :9092

# Tuer le processus
kill -9 <PID>

# Ou modifier docker-compose.yml
ports:
  - "9093:9092"
```

### 3. L'application ne se connecte pas à Kafka
**Vérifier les logs:**
```bash
docker-compose logs app
```

**Causes courantes:**
- Kafka n'est pas prêt (attendre le healthcheck)
- Mauvaise URL Kafka : doit être `kafka:9092` (pas localhost)
- Firewall bloquant la communication

### 4. Reconstruire après changement de code
```bash
docker-compose up -d --build
```

### 5. Voir les images construites
```bash
docker images | grep architecture
```

### 6. Supprimer une image
```bash
docker rmi architecture-app
```

---

## 📊 Monitoring

### Vérifier la santé des services
```bash
docker-compose ps
```

**Output exemple:**
```
NAME           STATUS              PORTS
app            Up 2 minutes        0.0.0.0:8080->8080/tcp
kafka          Up 2 minutes        0.0.0.0:9092->9092/tcp
kafka-ui       Up 2 minutes        0.0.0.0:8081->8080/tcp
```

### Inspectionner un container
```bash
docker-compose exec app bash
docker-compose exec kafka bash
```

### CPU/Mémoire usage
```bash
docker stats
```

---

## 🔐 Sécurité

### Production Checklist
- ✅ Utiliser des variables d'environnement pour secrets
- ✅ Non-root user (déjà implémenté dans Dockerfile)
- ✅ Healthchecks (déjà implémenté)
- ✅ Resource limits (à ajouter si nécessaire)
- ✅ Network isolation (déjà implémenté)

### Ajouter des limits de ressources (optionnel)
```yaml
app:
  # ...
  deploy:
    resources:
      limits:
        cpus: '1'
        memory: 512M
      reservations:
        cpus: '0.5'
        memory: 256M
```

---

## 🧪 Tests avec Docker

### Exécuter les tests dans Docker
```bash
docker-compose exec app mvn test
```

### Voir les rapports de test
```bash
docker-compose exec app cat target/surefire-reports/index.html
```

---

## 📝 Fichiers importants

| Fichier | Description |
|---------|-------------|
| `Dockerfile` | Instructions de build de l'image Docker |
| `docker-compose.yml` | Configuration des services |
| `.dockerignore` | Fichiers à ignorer lors du build |

---

## 💡 Tips & Tricks

### 1. Rebuild rapide sans cache
```bash
docker-compose up -d --build --no-cache
```

### 2. Voir les changements en temps réel
```bash
docker-compose logs -f --timestamps
```

### 3. Exécuter une commande one-shot
```bash
docker-compose run --rm app mvn clean package
```

### 4. Accéder à la console H2 (si activée)
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (vide)
```

### 5. Créer un backup de Kafka topics
```bash
docker-compose exec kafka kafka-topics --list --bootstrap-server localhost:9092
```

---

## 📚 Références

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)

---

## ✅ Checklist avant production

- [ ] Tester le démarrage avec `docker-compose up -d`
- [ ] Vérifier les logs : `docker-compose logs`
- [ ] Tester l'API : `curl http://localhost:8080/api/recettes`
- [ ] Vérifier Kafka UI : http://localhost:8081
- [ ] Arrêter gracieusement : `docker-compose down`
- [ ] Tester la reconstruction : `docker-compose up -d --build`
- [ ] Tester le nettoyage complet : `docker-compose down -v`

---

**Dernière mise à jour:** 17 Janvier 2026  
**Document:** Guide Docker & Docker Compose
