package Architecture_log.TP.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.service.RecetteCommandService;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Test d'intégration pour vérifier que Resilience4J Retry fonctionne correctement.
 *
 * Ce test utilise le contexte Spring complet avec AOP activé, ce qui permet
 * de vérifier que l'annotation @Retry intercepte réellement les appels de méthode
 * et réessaye en cas d'erreur.
 */
@SpringBootTest
class RecetteCommandServiceIntegrationTest {

  @Autowired
  private RecetteCommandService recetteCommandService;

  @MockitoBean
  private RecetteRepository recetteRepository;

  /**
   * Vérifie que le retry Resilience4J se déclenche exactement 3 fois en cas d'erreur persistante.
   *
   * Configuration attendue:
   * - maxAttempts = 3
   * - waitDuration = 1000ms entre les tentatives
   * - retryExceptions = DataAccessException, SQLException
   */
  @Test
  void shouldRetryThreeTimesOnPersistentDatabaseError() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO("Recette avec retry");

    // Simuler une erreur persistante à CHAQUE tentative
    when(recetteRepository.save(any(Recette.class))).thenThrow(
      new QueryTimeoutException("Database is temporarily unavailable")
    );

    // When & Then
    // L'exception doit être levée après épuisement des tentatives
    assertThrows(QueryTimeoutException.class, () -> {
      recetteCommandService.createRecette(dto);
    });

    // VÉRIFICATION CLÉE : Le repository doit être appelé 3 fois
    // - Tentative 1 : échoue
    // - Tentative 2 : échoue (après 1s d'attente)
    // - Tentative 3 : échoue (après 1s d'attente)
    // → Exception remonte au client
    verify(recetteRepository, times(3)).save(any(Recette.class));
  }

  /**
   * Vérifie que le retry réussit si une tentative réussit avant l'épuisement des retries.
   *
   * Scénario: La première tentative échoue, mais la deuxième réussit.
   */
  @Test
  void shouldSucceedAfterRetryWithRecovery() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO(
      "Recette récupérée après retry"
    );
    Recette savedRecette = new Recette("Recette récupérée après retry");
    savedRecette.setId(2L);

    // Simuler une erreur qui se corrige à la 2e tentative
    when(recetteRepository.save(any(Recette.class)))
      .thenThrow(new QueryTimeoutException("First attempt fails"))
      .thenReturn(savedRecette); // ← Succès à la 2e tentative

    // When
    Recette result = recetteCommandService.createRecette(dto);

    // Then
    assertNotNull(result);
    assertEquals("Recette récupérée après retry", result.getNom());
    assertEquals(2L, result.getId());

    // VÉRIFICATION : Le repository a été appelé exactement 2 fois
    // - Tentative 1 : échoue
    // - Tentative 2 : réussit ✅
    verify(recetteRepository, times(2)).save(any(Recette.class));
  }

  /**
   * Vérifie que les exceptions non-retryables ne sont pas réessayées.
   *
   * Selon la configuration: retryExceptions = DataAccessException, SQLException
   * Une exception RuntimeException générique ne devrait pas être retryée.
   */
  @Test
  void shouldNotRetryOnNonRetryableException() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO("Recette non-retryable");

    // Simuler une exception qui ne figure pas dans la liste retryExceptions
    when(recetteRepository.save(any(Recette.class))).thenThrow(
      new IllegalArgumentException("Invalid recipe data")
    );

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
      recetteCommandService.createRecette(dto);
    });

    // VÉRIFICATION : Le repository est appelé UNE SEULE FOIS
    // (pas de retry car l'exception n'est pas dans la liste retryExceptions)
    verify(recetteRepository, times(1)).save(any(Recette.class));
  }
}
