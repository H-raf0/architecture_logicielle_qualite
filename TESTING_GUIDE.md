# Guide de Test pour Resilience4J et Kafka

Ce guide explique comment tester que Resilience4J (retry) et Kafka fonctionnent correctement.

## 1. Tests Automatiques

### Exécuter les tests unitaires

```bash
./mvnw test
```

Les tests suivants sont disponibles :
- `RecetteCommandServiceTest` : Teste la création de recette avec retry et publication Kafka
- `RecetteEventPublisherTest` : Teste la publication d'événements Kafka

## 2. Test Manuel - Resilience4J Retry

### Méthode 1 : Tester avec une base de données temporairement indisponible

1. **Modifier temporairement le service pour simuler une erreur** :

Ajoutez temporairement dans `RecetteCommandService.createRecette()` :

```java
@Retry(name = "recetteRetry")
public Recette createRecette(CreateRecetteDTO dto) {
    // Simulation d'erreur - à retirer après test
    if (System.currentTimeMillis() % 2 == 0) {
        throw new org.springframework.dao.QueryTimeoutException("Test timeout");
    }
    
    Recette recette = new Recette(dto.getNom());
    Recette savedRecette = recetteRepository.save(recette);
    // ...
}
```

2. **Créer une recette via l'API** :

```bash
curl -X POST http://localhost:8080/recettes \
  -H "Content-Type: application/json" \
  -d '{"nom":"Test Retry"}'
```

3. **Observer les logs** : Vous devriez voir plusieurs tentatives dans les logs avec le préfixe Resilience4J.

### Méthode 2 : Vérifier via les métriques Actuator

1. **Activer les endpoints Actuator** dans `application.properties` :

```properties
management.endpoints.web.exposure.include=health,metrics,retryevents
management.endpoint.health.show-details=always
```

2. **Vérifier les métriques** :

```bash
# Vérifier les événements de retry
curl http://localhost:8080/actuator/retryevents

# Vérifier les métriques
curl http://localhost:8080/actuator/metrics/resilience4j.retry.calls
```

## 3. Test Manuel - Kafka

### Prérequis

1. **Démarrer Kafka** (si vous utilisez Docker) :

```bash
docker-compose up -d
```

Ou si vous avez Kafka installé localement, démarrer Zookeeper et Kafka.

2. **Vérifier que Kafka est accessible** :

```bash
# Vérifier les brokers
kafka-broker-api-versions --bootstrap-server localhost:9092
```

### Test 1 : Créer une recette et vérifier le topic

1. **Créer un consumer pour écouter le topic** :

```bash
# Dans un terminal séparé
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic recette-created \
  --from-beginning
```

2. **Créer une recette via l'API** :

```bash
curl -X POST http://localhost:8080/recettes \
  -H "Content-Type: application/json" \
  -d '{"nom":"Tarte aux pommes"}'
```

3. **Observer le consumer** : Vous devriez voir l'événement JSON apparaître dans le terminal du consumer.

### Test 2 : Vérifier avec un consumer Java simple

Créez un test d'intégration ou un listener simple pour consommer les messages.

### Test 3 : Vérifier les logs de l'application

Après avoir créé une recette, vérifiez les logs de l'application. Vous devriez voir :

```
INFO  - Recette created event published successfully: 1 to topic: recette-created
```

### Test 4 : Vérifier que le topic existe

```bash
# Lister les topics
kafka-topics --bootstrap-server localhost:9092 --list

# Vérifier les détails du topic
kafka-topics --bootstrap-server localhost:9092 \
  --describe --topic recette-created
```

## 4. Test d'Intégration Complet

### Créer un test d'intégration avec Kafka embarqué

1. **Ajouter la dépendance** (si pas déjà présente) :

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka-test</artifactId>
    <scope>test</scope>
</dependency>
```

2. **Exécuter le test d'intégration** (voir exemple ci-dessous)

## 5. Dépannage

### Resilience4J ne retry pas

- Vérifier que `spring-boot-starter-aop` est présent (ou inclus transitivement)
- Vérifier la configuration dans `application.properties`
- Vérifier que l'annotation `@Retry` est bien présente sur la méthode
- Vérifier que l'exception levée est dans la liste `retryExceptions`

### Kafka ne publie pas

- Vérifier que Kafka est démarré : `kafka-broker-api-versions --bootstrap-server localhost:9092`
- Vérifier la configuration dans `application.properties`
- Vérifier les logs d'erreur de l'application
- Vérifier que le topic existe ou est créé automatiquement
- Vérifier les permissions du topic

### Logs à vérifier

```bash
# Logs de l'application
tail -f logs/application.log | grep -i "kafka\|resilience\|retry"
```

## 6. Commandes utiles Kafka

```bash
# Créer le topic manuellement (si nécessaire)
kafka-topics --create --bootstrap-server localhost:9092 \
  --topic recette-created \
  --partitions 3 \
  --replication-factor 1

# Consommer tous les messages du topic
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic recette-created \
  --from-beginning \
  --property print.key=true \
  --property print.value=true

# Vérifier les messages dans le topic
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic recette-created \
  --max-messages 10
```

** TEST MANUEL **

  ** Test manuel - Resilience4J : **
Démarrer l'application

Vérifier les métriques :

curl http://localhost:8080/actuator/retryevents
curl http://localhost:8080/actuator/metrics/resilience4j.retry.calls

  ** Test manuel - Kafka : **
Démarrer Kafka (si pas déjà fait)
Créer un consumer :
kafka-console-consumer --bootstrap-server localhost:9092 \  --topic recette-created \  --from-beginning
Créer une recette via l'API :
curl -X POST http://localhost:8080/recettes \  -H "Content-Type: application/json" \  -d '{"nom":"Tarte aux pommes"}'
Vérifier que l'événement apparaît dans le consumer

