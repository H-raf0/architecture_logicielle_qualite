package Architecture_log.TP.integration;

import static org.junit.jupiter.api.Assertions.*;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.service.RecetteCommandService;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

/**
 * Test d'intégration pour la création de recette.
 * Note: Ce test nécessite que Kafka soit démarré pour fonctionner complètement.
 * Désactivé par défaut car il nécessite un broker Kafka en cours d'exécution.
 * Pour l'activer, démarrez Kafka et supprimez l'annotation @Disabled.
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(
  properties = {
    "resilience4j.retry.instances.recetteRetry.maxAttempts=3",
    "resilience4j.retry.instances.recetteRetry.waitDuration=1000",
  }
)
@Disabled(
  "Nécessite un broker Kafka en cours d'exécution. Activez-le en démarrant Kafka localement."
)
class RecetteIntegrationTest {

  @Autowired
  private RecetteCommandService recetteCommandService;

  @Autowired
  private RecetteRepository recetteRepository;

  @Test
  void shouldCreateRecette() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO("Test Integration");

    // When
    Recette result = recetteCommandService.createRecette(dto);

    // Then
    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals("Test Integration", result.getNom());

    // Vérifier que la recette a été sauvegardée en base
    assertTrue(recetteRepository.findById(result.getId()).isPresent());
  }
}
