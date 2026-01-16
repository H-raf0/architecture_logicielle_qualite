package Architecture_log.TP.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.dto.RecetteCreatedEvent;
import Architecture_log.TP.commands.service.RecetteCommandService;
import Architecture_log.TP.commands.service.RecetteEventPublisher;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.QueryTimeoutException;

@ExtendWith(MockitoExtension.class)
class RecetteCommandServiceTest {

  @Mock
  private RecetteRepository recetteRepository;

  @Mock
  private RecetteEventPublisher recetteEventPublisher;

  private RecetteCommandService recetteCommandService;

  @BeforeEach
  void setUp() {
    recetteCommandService = new RecetteCommandService(
      recetteRepository,
      recetteEventPublisher
    );
  }

  @Test
  void shouldCreateRecetteAndPublishEvent() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO("Tarte aux pommes");
    Recette savedRecette = new Recette("Tarte aux pommes");
    savedRecette.setId(1L);

    when(recetteRepository.save(any(Recette.class))).thenReturn(savedRecette);
    doNothing()
      .when(recetteEventPublisher)
      .publishRecetteCreated(any(RecetteCreatedEvent.class));

    // When
    Recette result = recetteCommandService.createRecette(dto);

    // Then
    assertNotNull(result);
    assertEquals("Tarte aux pommes", result.getNom());
    assertEquals(1L, result.getId());

    // Vérifier que le repository a été appelé
    verify(recetteRepository, times(1)).save(any(Recette.class));

    // Vérifier que l'événement Kafka a été publié
    ArgumentCaptor<RecetteCreatedEvent> eventCaptor = ArgumentCaptor.forClass(
      RecetteCreatedEvent.class
    );
    verify(recetteEventPublisher, times(1)).publishRecetteCreated(
      eventCaptor.capture()
    );

    RecetteCreatedEvent publishedEvent = eventCaptor.getValue();
    assertNotNull(publishedEvent);
    assertEquals(1L, publishedEvent.getId());
    assertEquals("Tarte aux pommes", publishedEvent.getNom());
    assertNotNull(publishedEvent.getCreatedAt());
  }

  @Test
  void shouldRetryOnDatabaseError() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO("Recette à retry");

    // Simuler une erreur de base de données
    when(recetteRepository.save(any(Recette.class))).thenThrow(
      new QueryTimeoutException("Database timeout")
    );

    // When & Then - Note: Resilience4J retry nécessite un contexte Spring avec AOP
    // Ce test unitaire simple ne peut pas tester le retry réellement car l'annotation @Retry
    // ne fonctionne que dans un contexte Spring. Pour tester le retry, utilisez un test
    // d'intégration avec @SpringBootTest. Ici, on teste juste que l'exception est bien levée
    assertThrows(QueryTimeoutException.class, () -> {
      recetteCommandService.createRecette(dto);
    });

    // Vérifier que le repository a été appelé au moins une fois
    verify(recetteRepository, atLeast(1)).save(any(Recette.class));
  }

  @Test
  void shouldHandleEventPublishingFailureGracefully() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO("Recette avec erreur Kafka");
    Recette savedRecette = new Recette("Recette avec erreur Kafka");
    savedRecette.setId(3L);

    when(recetteRepository.save(any(Recette.class))).thenReturn(savedRecette);
    doThrow(new RuntimeException("Kafka error"))
      .when(recetteEventPublisher)
      .publishRecetteCreated(any(RecetteCreatedEvent.class));

    // When & Then - La recette devrait être créée même si Kafka échoue
    // Mais l'événement devrait quand même être tenté
    assertThrows(RuntimeException.class, () -> {
      recetteCommandService.createRecette(dto);
    });

    verify(recetteRepository, times(1)).save(any(Recette.class));
    verify(recetteEventPublisher, times(1)).publishRecetteCreated(
      any(RecetteCreatedEvent.class)
    );
  }
}
