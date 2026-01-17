package Architecture_log.TP.commands.service;

import Architecture_log.TP.commands.dto.RecetteCreatedEvent;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

/**
 * Service responsable de la publication des événements liés aux recettes.
 *
 * Utilise un {@link KafkaTemplate} pour envoyer des {@link RecetteCreatedEvent}
 * vers le topic Kafka configuré.
 */
@Service
public class RecetteEventPublisher {

  /** Nom du topic Kafka utilisé pour les événements de création de recette. */
  private static final String TOPIC_NAME = "recette-created";

  private static final Logger logger = LoggerFactory.getLogger(
    RecetteEventPublisher.class
  );

  private final KafkaTemplate<String, RecetteCreatedEvent> kafkaTemplate;

  /**
   * Crée un publisher d'événements de recette.
   *
   * @param kafkaTemplate template Kafka injecté pour l'envoi des messages
   */
  public RecetteEventPublisher(
    KafkaTemplate<String, RecetteCreatedEvent> kafkaTemplate
  ) {
    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * Publie un événement {@link RecetteCreatedEvent} sur le topic Kafka.
   *
   * L'envoi est asynchrone : les erreurs éventuelles sont loggées et
   * transmises via le futur retourné par Kafka.
   *
   * @param event événement décrivant la recette créée
   */
  public void publishRecetteCreated(RecetteCreatedEvent event) {
    try {
      CompletableFuture<SendResult<String, RecetteCreatedEvent>> future =
        kafkaTemplate.send(TOPIC_NAME, event.getId().toString(), event);

      future.whenComplete((result, exception) -> {
        if (exception == null) {
          logger.info(
            "Recette created event published successfully: {} to topic: {}",
            event.getId(),
            TOPIC_NAME
          );
        } else {
          logger.error(
            "Failed to publish recette created event: {}",
            event.getId(),
            exception
          );
        }
      });
    } catch (Exception e) {
      logger.error(
        "Error publishing recette created event: {}",
        event.getId(),
        e
      );
      throw e;
    }
  }
}
