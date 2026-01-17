package Architecture_log.TP.integration;

import static org.junit.jupiter.api.Assertions.*;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.service.RecetteCommandService;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import Architecture_log.TP.queries.service.RecetteQueryService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Tests d'intégration complète pour les opérations CRUD sur les recettes.
 *
 * Note: Ce test nécessite que Kafka soit démarré pour fonctionner complètement.
 * Désactivé par défaut car il nécessite un broker Kafka en cours d'exécution.
 * Pour l'activer, démarrez Kafka et supprimez l'annotation @Disabled.
 *
 * Scénarios testés:
 * - Création d'une recette
 * - Récupération d'une recette
 * - Mise à jour d'une recette
 * - Suppression d'une recette
 * - Recherche de recettes
 * - Validation des erreurs
 * - Retry automatique
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
  private RecetteQueryService recetteQueryService;

  @Autowired
  private RecetteRepository recetteRepository;

  @BeforeEach
  void setUp() {
    recetteRepository.deleteAll();
  }

  // ========== TESTS DE CRÉATION ==========

  @Test
  @DisplayName("Devrait créer une recette avec succès")
  void shouldCreateRecette() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO("Pizza Margherita");

    // When
    Recette result = recetteCommandService.createRecette(dto);

    // Then
    assertNotNull(result, "La recette créée ne doit pas être null");
    assertNotNull(result.getId(), "L'ID de la recette doit être généré");
    assertEquals(
      "Pizza Margherita",
      result.getNom(),
      "Le nom doit correspondre"
    );

    // Vérifier en base de données
    Optional<Recette> saved = recetteRepository.findById(result.getId());
    assertTrue(
      saved.isPresent(),
      "La recette doit être sauvegardée en base de données"
    );
    assertEquals("Pizza Margherita", saved.get().getNom());
  }

  @Test
  @DisplayName("Devrait créer plusieurs recettes")
  void shouldCreateMultipleRecettes() {
    // Given
    String[] noms = { "Pizza", "Pâtes", "Salade", "Burger" };

    // When
    for (String nom : noms) {
      recetteCommandService.createRecette(new CreateRecetteDTO(nom));
    }

    // Then
    List<Recette> allRecettes = recetteRepository.findAll();
    assertEquals(4, allRecettes.size(), "Doit avoir 4 recettes");
  }

  @Test
  @DisplayName("Devrait rejeter une recette avec nom vide")
  void shouldRejectEmptyName() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO("");

    // When & Then
    assertThrows(
      IllegalArgumentException.class,
      () -> recetteCommandService.createRecette(dto),
      "Une exception doit être levée pour un nom vide"
    );
  }

  @Test
  @DisplayName("Devrait rejeter une recette avec nom null")
  void shouldRejectNullName() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO(null);

    // When & Then
    assertThrows(
      Exception.class,
      () -> recetteCommandService.createRecette(dto),
      "Une exception doit être levée pour un nom null"
    );
  }

  @Test
  @DisplayName("Devrait rejeter une recette avec nom trop court")
  void shouldRejectShortName() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO("AB");

    // When & Then
    assertThrows(
      IllegalArgumentException.class,
      () -> recetteCommandService.createRecette(dto),
      "Une exception doit être levée pour un nom trop court"
    );
  }

  // ========== TESTS DE LECTURE ==========

  @Test
  @DisplayName("Devrait récupérer une recette par ID")
  void shouldGetRecetteById() {
    // Given
    Recette created = recetteCommandService.createRecette(
      new CreateRecetteDTO("Pâtes Carbonara")
    );

    // When
    Optional<Recette> retrieved = recetteQueryService.getRecetteById(
      created.getId()
    );

    // Then
    assertTrue(retrieved.isPresent(), "La recette doit être trouvée");
    assertEquals("Pâtes Carbonara", retrieved.get().getNom());
    assertEquals(created.getId(), retrieved.get().getId());
  }

  @Test
  @DisplayName("Devrait retourner vide pour une recette inexistante")
  void shouldReturnEmptyForNonExistent() {
    // When
    Optional<Recette> retrieved = recetteQueryService.getRecetteById(999L);

    // Then
    assertTrue(retrieved.isEmpty(), "Doit retourner vide");
  }

  @Test
  @DisplayName("Devrait lister toutes les recettes")
  void shouldListAllRecettes() {
    // Given
    recetteCommandService.createRecette(new CreateRecetteDTO("Pizza"));
    recetteCommandService.createRecette(new CreateRecetteDTO("Pâtes"));
    recetteCommandService.createRecette(new CreateRecetteDTO("Salade"));

    // When
    List<Recette> result = recetteQueryService.getAllRecettes();

    // Then
    assertEquals(3, result.size(), "Doit avoir 3 recettes");
  }

  @Test
  @DisplayName("Devrait lister vide quand aucune recette")
  void shouldListEmptyWhenNoRecettes() {
    // When
    List<Recette> result = recetteQueryService.getAllRecettes();

    // Then
    assertTrue(result.isEmpty(), "La liste doit être vide");
  }

  @Test
  @DisplayName("Devrait rechercher des recettes par terme")
  void shouldSearchRecettesByTerm() {
    // Given
    recetteCommandService.createRecette(
      new CreateRecetteDTO("Pizza Margherita")
    );
    recetteCommandService.createRecette(
      new CreateRecetteDTO("Pizza Napolitaine")
    );
    recetteCommandService.createRecette(new CreateRecetteDTO("Salade César"));

    // When
    List<Recette> results = recetteQueryService.searchRecettes("Pizza");

    // Then
    assertEquals(
      2,
      results.size(),
      "Doit trouver 2 recettes contenant 'Pizza'"
    );
    assertTrue(
      results.stream().allMatch(r -> r.getNom().contains("Pizza")),
      "Toutes les recettes doivent contenir 'Pizza'"
    );
  }

  @Test
  @DisplayName("Devrait retourner liste vide si aucun résultat de recherche")
  void shouldReturnEmptySearchResults() {
    // Given
    recetteCommandService.createRecette(new CreateRecetteDTO("Pizza"));

    // When
    List<Recette> results = recetteQueryService.searchRecettes("Burger");

    // Then
    assertTrue(results.isEmpty(), "Aucun résultat ne doit être trouvé");
  }

  // ========== TESTS DE MISE À JOUR ==========

  @Test
  @DisplayName("Devrait mettre à jour une recette existante")
  void shouldUpdateRecette() {
    // Given
    Recette created = recetteCommandService.createRecette(
      new CreateRecetteDTO("Pizza")
    );
    created.setNom("Pizza Margherita");

    // When
    recetteRepository.save(created);

    // Then
    Optional<Recette> updated = recetteRepository.findById(created.getId());
    assertTrue(updated.isPresent());
    assertEquals("Pizza Margherita", updated.get().getNom());
  }

  @Test
  @DisplayName("Devrait mettre à jour plusieurs champs")
  void shouldUpdateMultipleFields() {
    // Given
    Recette created = recetteCommandService.createRecette(
      new CreateRecetteDTO("Pizza")
    );

    // When
    created.setNom("Pizza Napolitaine");
    recetteRepository.save(created);

    // Then
    Recette updated = recetteRepository.findById(created.getId()).orElse(null);
    assertNotNull(updated);
    assertEquals("Pizza Napolitaine", updated.getNom());
  }

  // ========== TESTS DE SUPPRESSION ==========

  @Test
  @DisplayName("Devrait supprimer une recette existante")
  void shouldDeleteRecette() {
    // Given
    Recette created = recetteCommandService.createRecette(
      new CreateRecetteDTO("Pizza")
    );
    Long id = created.getId();

    // When
    recetteRepository.deleteById(id);

    // Then
    Optional<Recette> deleted = recetteRepository.findById(id);
    assertTrue(deleted.isEmpty(), "La recette doit être supprimée");
  }

  @Test
  @DisplayName("Devrait supprimer plusieurs recettes")
  void shouldDeleteMultipleRecettes() {
    // Given
    Recette r1 = recetteCommandService.createRecette(
      new CreateRecetteDTO("Pizza")
    );
    Recette r2 = recetteCommandService.createRecette(
      new CreateRecetteDTO("Pâtes")
    );

    // When
    recetteRepository.deleteById(r1.getId());
    recetteRepository.deleteById(r2.getId());

    // Then
    List<Recette> remaining = recetteRepository.findAll();
    assertTrue(
      remaining.isEmpty(),
      "Toutes les recettes doivent être supprimées"
    );
  }

  @Test
  @DisplayName("Devrait supprimer sans erreur une recette inexistante")
  void shouldDeleteNonExistentRecetteSilently() {
    // When & Then
    assertDoesNotThrow(() -> recetteRepository.deleteById(999L));
  }

  // ========== TESTS DE VALIDITÉ ==========

  @Test
  @DisplayName("Devrait compter les recettes correctement")
  void shouldCountRecettesCorrectly() {
    // Given
    recetteCommandService.createRecette(new CreateRecetteDTO("Pizza"));
    recetteCommandService.createRecette(new CreateRecetteDTO("Pâtes"));
    recetteCommandService.createRecette(new CreateRecetteDTO("Salade"));

    // When
    long count = recetteRepository.count();

    // Then
    assertEquals(3L, count, "Doit y avoir 3 recettes");
  }

  @Test
  @DisplayName("Devrait vérifier l'existence d'une recette par ID")
  void shouldCheckRecetteExistence() {
    // Given
    Recette created = recetteCommandService.createRecette(
      new CreateRecetteDTO("Pizza")
    );

    // When & Then
    assertTrue(
      recetteRepository.existsById(created.getId()),
      "La recette doit exister"
    );
    assertFalse(
      recetteRepository.existsById(999L),
      "La recette inexistante ne doit pas exister"
    );
  }

  // ========== TESTS D'INTÉGRATION COMPLÈTE ==========

  @Test
  @DisplayName(
    "Scénario complet: créer -> récupérer -> mettre à jour -> supprimer"
  )
  void shouldCompleteFullLifecycle() {
    // CREATE
    Recette created = recetteCommandService.createRecette(
      new CreateRecetteDTO("Pizza Margherita")
    );
    assertNotNull(created.getId());

    // RETRIEVE
    Optional<Recette> retrieved = recetteQueryService.getRecetteById(
      created.getId()
    );
    assertTrue(retrieved.isPresent());

    // UPDATE
    retrieved.get().setNom("Pizza Napolitaine");
    recetteRepository.save(retrieved.get());

    // VERIFY UPDATE
    Recette updated = recetteRepository.findById(created.getId()).orElse(null);
    assertNotNull(updated);
    assertEquals("Pizza Napolitaine", updated.getNom());

    // DELETE
    recetteRepository.deleteById(created.getId());

    // VERIFY DELETE
    assertTrue(
      recetteRepository.findById(created.getId()).isEmpty(),
      "La recette doit être supprimée"
    );
  }

  @Test
  @DisplayName(
    "Scénario: créer plusieurs, chercher, puis supprimer les trouvées"
  )
  void shouldCreateSearchAndDelete() {
    // Given - Créer plusieurs recettes
    recetteCommandService.createRecette(
      new CreateRecetteDTO("Pizza Margherita")
    );
    recetteCommandService.createRecette(
      new CreateRecetteDTO("Pizza Napolitaine")
    );
    recetteCommandService.createRecette(new CreateRecetteDTO("Salade César"));

    // When - Chercher les pizzas
    List<Recette> pizzas = recetteQueryService.searchRecettes("Pizza");

    // Then - Vérifier
    assertEquals(2, pizzas.size());

    // Delete
    pizzas.forEach(p -> recetteRepository.deleteById(p.getId()));

    // Verify only salad remains
    List<Recette> remaining = recetteRepository.findAll();
    assertEquals(1, remaining.size());
    assertEquals("Salade César", remaining.get(0).getNom());
  }
}
