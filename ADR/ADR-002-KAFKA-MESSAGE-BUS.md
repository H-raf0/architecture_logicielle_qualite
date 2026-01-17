# ADR-002: Utilisation de Kafka comme Message Bus pour les Événements

**Date:** Janvier 2025 
**Authors:** Achraf EL ALLALI, WIAM ABDELLAOUI
**Context:** Projet TP - Architecture Logicielle et Qualité

---

## 1. Problem Statement

L'architecture CQRS nécessite un système de publication/souscription fiable pour :
- Découpler le Command Service du Query Service
- Garantir la persistence des événements
- Supporter la scalabilité horizontale
- Gérer les messages perdus et les retries

Les alternatives simples (RabbitMQ, ActiveMQ) présentent des limitations :
- Pas de event log permanent
- Difficulté de replay des événements
- Problèmes de scaling horizontal

---

## 2. Decision

Utiliser **Apache Kafka 3.x** comme message broker pour tous les événements métier.

### Architecture avec Kafka

```
┌─────────────────┐
│ Command Service │
└────────┬────────┘
         │ publishes
         │
         ▼
    ┌─────────────────────────────────┐
    │  Kafka Cluster                  │
    │  ┌──────────────────────────┐   │
    │  │ recette-events topic     │   │
    │  │ (3 partitions)           │   │
    │  │ - RecetteCreatedEvent    │   │
    │  │ - RecetteUpdatedEvent    │   │
    │  │ - RecetteDeletedEvent    │   │
    │  └──────────────────────────┘   │
    │                                  │
    │  ┌──────────────────────────┐   │
    │  │ ingredient-events topic  │   │
    │  │ (3 partitions)           │   │
    │  │ - IngredientAddedEvent   │   │
    │  │ - IngredientRemovedEvent │   │
    │  └──────────────────────────┘   │
    └──────────────────────────────────┘
         │ consumes
         │
         ▼
    ┌─────────────────┐
    │ Query Service   │
    │ (Cache/Update)  │
    └─────────────────┘
```

---

## 3. Rationale

### 3.1 Pourquoi Kafka ?

| Critère | Kafka | Autres |
|---------|-------|--------|
| **Event Log** | ✅ Permanent, réplayable | ❌ Généralement perdu |
| **Scalabilité** | ✅ Partitions horizontales | ⚠️ Limité |
| **Performance** | ✅ ~1M msg/sec | ⚠️ 10K-100K msg/sec |
| **Durabilité** | ✅ Replication | ✅ Similaire |
| **Ordering** | ✅ Par partition | ❌ Global ou pas |
| **Replay** | ✅ Historique complet | ❌ Rarement possible |
| **Community** | ✅ Très grand | ⚠️ Moins large |

### 3.2 Kafka en production

Kafka est utilisé par :
- Netflix, Uber, LinkedIn
- Airbnb, Shopify, PayPal
- Et des milliers d'autres entreprises

---

## 4. Configuration Implémentée

### 4.1 Topics Kafka

```yaml
Topics:
  recette-events:
    partitions: 3
    replication-factor: 2
    retention-ms: 604800000  # 7 jours
    segment-ms: 86400000      # 1 jour
    
  ingredient-events:
    partitions: 3
    replication-factor: 2
    retention-ms: 604800000
    segment-ms: 86400000
```

### 4.2 Producer Configuration

```properties
# application.properties
spring.kafka.producer.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.acks=all
spring.kafka.producer.retries=3
spring.kafka.producer.properties.linger.ms=10
spring.kafka.producer.properties.batch.size=16384
```

### 4.3 Consumer Configuration

```properties
spring.kafka.consumer.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=recette-query-service
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
spring.kafka.consumer.max-poll-records=500
spring.kafka.consumer.session-timeout-ms=30000
```

### 4.4 Code d'implémentation

```java
// Event Publisher
@Service
public class RecetteEventPublisher {
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    public void publishRecetteCreatedEvent(RecetteCreatedEvent event) {
        kafkaTemplate.send("recette-events", 
            event.getRecetteId().toString(), 
            event);
    }
}

// Event Consumer
@Service
public class RecetteEventListener {
    
    @KafkaListener(topics = "recette-events", 
                   groupId = "recette-query-service")
    public void handleRecetteCreatedEvent(RecetteCreatedEvent event) {
        // Update Read Model
    }
}
```

---

## 5. Resilience & Error Handling

### 5.1 Dead Letter Topic (DLT)

```java
@Bean
public ConsumerFactory<String, RecetteCreatedEvent> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerProps());
}

@Bean
public ConcurrentKafkaListenerContainerFactory<String, RecetteCreatedEvent> 
       kafkaListenerContainerFactory() {
    
    ConcurrentKafkaListenerContainerFactory<String, RecetteCreatedEvent> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    
    // Configure DLT
    factory.setCommonErrorHandler(
        new DefaultErrorHandler(
            new DeadLetterPublishingRecoverer(kafkaTemplate),
            new FixedBackOff(1000, 3)));
    
    return factory;
}
```

### 5.2 Retry Policy

```
1ère tentative: immédiate
2e tentative: 1 seconde
3e tentative: 2 secondes
→ Dead Letter Topic après 3 échecs
```

---

## 6. Docker Compose Setup

```yaml
# docker-compose.yml
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_SYNC_LIMIT: 2
      ZOOKEEPER_INIT_LIMIT: 5

  kafka:
    image: confluentinc/cp-kafka:7.5.0
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1

  kafka-ui:  # Optional: Web UI for Kafka management
    image: provectuslabs/kafka-ui:latest
    ports:
      - "8081:8080"
    depends_on:
      - kafka
```

---

## 7. Monitoring & Observability

### 7.1 Métriques clés

```
- Messages produced par topic/seconde
- Messages consumed par topic/seconde
- Consumer lag (retard)
- Error rate par topic
- DLT messages count
```

### 7.2 Alertes recommandées

- Consumer lag > 1000 messages
- Error rate > 1%
- DLT non vide depuis > 1 heure

---

## 8. Testing avec Kafka

### 8.1 Embedded Kafka pour les tests

```java
@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {
    "log.dir=/tmp/kafka-logs",
    "listeners=PLAINTEXT://localhost:9092"
})
public class RecetteEventPublisherTest {
    
    @Test
    public void testEventPublishing() {
        // Test with real Kafka
    }
}
```

### 8.2 Testcontainers approach

```java
@Testcontainers
@SpringBootTest
public class KafkaIntegrationTest {
    
    @Container
    static KafkaContainer kafka = new KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));
    
    @Test
    public void testWithRealKafka() {
        // Test against real Kafka container
    }
}
```

---

## 9. Consequences

### Positives ✅
- Event sourcing possible
- Audit trail complet
- Scaling horizontal facile
- Replay des événements
- Haute disponibilité

### Negatives ⚠️
- Infrastructure supplémentaire
- Courbe d'apprentissage
- Configuration complexity
- Monitoring requis

---

## 10. Alternatives Rejected

| Alternative | Raison du rejet |
|-------------|-----------------|
| **RabbitMQ** | Pas de persistent log, replay difficile |
| **ActiveMQ** | Performance limitée à 100K msg/sec |
| **Redis Pub/Sub** | Pas de persistence, perte des messages |
| **AWS SQS** | Cloud-dependent, coûteux |
| **Message DB** | Complexity, pas de streaming |

---

## 11. Future Enhancements

- [ ] Kafka Streams pour processing temps réel
- [ ] Schema Registry (Avro serialization)
- [ ] Connect integration pour CDC
- [ ] Multi-datacenter replication

---

**Approved by:** Achraf EL ALLALI, WIAM ABDELLAOUI 
**Last reviewed:** Janvier 2025
