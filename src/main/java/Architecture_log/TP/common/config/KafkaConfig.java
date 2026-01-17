package Architecture_log.TP.common.config;

import Architecture_log.TP.commands.dto.RecetteCreatedEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * Configuration de Kafka pour la publication d'événements.
 *
 * Configure le producteur Kafka pour envoyer les événements
 * "recette-created" vers le topic Kafka approprié.
 */
@Configuration
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
  private String bootstrapServers;

  /**
   * Crée une ProducerFactory pour les événements de création de recettes.
   *
   * Configure les paramètres du producteur Kafka:
   * - Serveurs de bootstrap (adresse du cluster Kafka)
   * - Sérialisation des clés et valeurs
   * - Mapping des types JSON
   *
   * @return ProducerFactory configurée pour {@link RecetteCreatedEvent}
   */
  @Bean
  public ProducerFactory<String, RecetteCreatedEvent> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configProps.put(
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      StringSerializer.class
    );
    configProps.put(
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer"
    );
    configProps.put(
      "spring.json.type.mapping",
      "recetteCreatedEvent:Architecture_log.TP.commands.dto.RecetteCreatedEvent"
    );
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  /**
   * Crée un KafkaTemplate pour envoyer des événements de création de recettes.
   *
   * Le KafkaTemplate simplifie l'envoi de messages vers Kafka
   * en gérant la sérialisation et l'envoi.
   *
   * @return KafkaTemplate configuré pour {@link RecetteCreatedEvent}
   */
  @Bean
  public KafkaTemplate<String, RecetteCreatedEvent> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
