package Architecture_log.TP.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

/**
 * Configuration de test qui démarre un broker Kafka embarqué.
 * Cela permet d’exécuter des tests d’intégration sans nécessiter Docker
 * ni une installation manuelle de Kafka.
 */
@TestConfiguration
@EmbeddedKafka(
  partitions = 1,
  brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" }
)
@ActiveProfiles("test")
public class KafkaTestConfig {}
