package Architecture_log.TP.service;

import Architecture_log.TP.commands.dto.RecetteCreatedEvent;
import Architecture_log.TP.commands.service.RecetteEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecetteEventPublisherTest {

  @Mock
  private KafkaTemplate<String, RecetteCreatedEvent> kafkaTemplate;

  private RecetteEventPublisher recetteEventPublisher;

  @BeforeEach
  void setUp() {
    recetteEventPublisher = new RecetteEventPublisher(kafkaTemplate);
  }

  @Test
  void shouldPublishRecetteCreatedEvent() {
    // Given
    RecetteCreatedEvent event = new RecetteCreatedEvent(1L, "Tarte aux pommes", LocalDateTime.now());
    CompletableFuture<SendResult<String, RecetteCreatedEvent>> future = CompletableFuture.completedFuture(
      new SendResult<>(null, null)
    );

    when(kafkaTemplate.send(anyString(), anyString(), any(RecetteCreatedEvent.class)))
      .thenReturn(future);

    // When
    recetteEventPublisher.publishRecetteCreated(event);

    // Then
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<RecetteCreatedEvent> eventCaptor = ArgumentCaptor.forClass(RecetteCreatedEvent.class);

    verify(kafkaTemplate, times(1)).send(
      topicCaptor.capture(),
      keyCaptor.capture(),
      eventCaptor.capture()
    );

    assertEquals("recette-created", topicCaptor.getValue());
    assertEquals("1", keyCaptor.getValue()); // ID de la recette comme clé
    RecetteCreatedEvent capturedEvent = eventCaptor.getValue();
    assertEquals(1L, capturedEvent.getId());
    assertEquals("Tarte aux pommes", capturedEvent.getNom());
    assertNotNull(capturedEvent.getCreatedAt());
  }

  @Test
  void shouldHandleKafkaError() {
    // Given
    RecetteCreatedEvent event = new RecetteCreatedEvent(2L, "Recette avec erreur", LocalDateTime.now());
    CompletableFuture<SendResult<String, RecetteCreatedEvent>> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException("Kafka connection error"));

    when(kafkaTemplate.send(anyString(), anyString(), any(RecetteCreatedEvent.class)))
      .thenReturn(future);

    // When - Ne devrait pas lever d'exception, mais logger l'erreur
    recetteEventPublisher.publishRecetteCreated(event);

    // Then - Vérifier que l'envoi a été tenté
    verify(kafkaTemplate, times(1)).send(anyString(), anyString(), any(RecetteCreatedEvent.class));
  }
}

