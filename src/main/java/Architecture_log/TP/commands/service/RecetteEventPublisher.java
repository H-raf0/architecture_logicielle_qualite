package Architecture_log.TP.commands.service;

import Architecture_log.TP.commands.dto.RecetteCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class RecetteEventPublisher {

  private static final String TOPIC_NAME = "recette-created";
  private static final Logger logger = LoggerFactory.getLogger(RecetteEventPublisher.class);

  private final KafkaTemplate<String, RecetteCreatedEvent> kafkaTemplate;

  public RecetteEventPublisher(
    KafkaTemplate<String, RecetteCreatedEvent> kafkaTemplate
  ) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publishRecetteCreated(RecetteCreatedEvent event) {
    try {
      CompletableFuture<SendResult<String, RecetteCreatedEvent>> future =
        kafkaTemplate.send(TOPIC_NAME, event.getId().toString(), event);

      future.whenComplete(
        (result, exception) -> {
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
        }
      );
    } catch (Exception e) {
      logger.error("Error publishing recette created event: {}", event.getId(), e);
      throw e;
    }
  }
}

