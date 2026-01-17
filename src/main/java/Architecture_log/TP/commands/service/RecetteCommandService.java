package Architecture_log.TP.commands.service;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.dto.RecetteCreatedEvent;
import Architecture_log.TP.commands.dto.UpdateRecetteDTO;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import io.github.resilience4j.retry.annotation.Retry;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer les commandes de création/modification de recettes.
 *
 * Implémente le pattern CQRS (Command Query Responsibility Segregation).
 * Les modifications passent par ce service avec retry automatique.
 */
@Service
public class RecetteCommandService {

  private final RecetteRepository recetteRepository;
  private final RecetteEventPublisher recetteEventPublisher;

  /**
   * Constructeur du service de commandes de recettes.
   *
   * @param recetteRepository Repository pour accéder aux recettes
   * @param recetteEventPublisher Publisher pour publier les événements
   */
  public RecetteCommandService(
    RecetteRepository recetteRepository,
    RecetteEventPublisher recetteEventPublisher
  ) {
    this.recetteRepository = recetteRepository;
    this.recetteEventPublisher = recetteEventPublisher;
  }

  /**
   * Crée une nouvelle recette avec retry automatique en cas d'erreur.
   *
   * Si la première tentative échoue (ex: BD indisponible), le système
   * réessaye jusqu'à 3 fois avec une attente de 1 seconde entre les tentatives.
   *
   * Après la création, un événement "recette-created" est publié sur Kafka.
   *
   * @param dto Les données de la recette à créer
   * @return La recette créée avec son ID généré
   * @throws RuntimeException Si l'enregistrement échoue après 3 tentatives
   */
  @Retry(name = "recetteRetry")
  public Recette createRecette(CreateRecetteDTO dto) {
    Recette recette = new Recette(dto.getNom());
    Recette savedRecette = recetteRepository.save(recette);

    RecetteCreatedEvent event = new RecetteCreatedEvent(
      savedRecette.getId(),
      savedRecette.getNom(),
      LocalDateTime.now()
    );
    recetteEventPublisher.publishRecetteCreated(event);

    return savedRecette;
  }

  /**
   * Met à jour une recette existante.
   *
   * @param id L'ID de la recette à modifier
   * @param dto Les nouvelles données de la recette
   * @return La recette mise à jour
   * @throws RuntimeException Si la recette n'existe pas
   */
  public Recette updateRecette(Long id, UpdateRecetteDTO dto) {
    Recette recette = recetteRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("Recette not found"));
    recette.setNom(dto.getNom());
    return recetteRepository.save(recette);
  }

  /**
   * Supprime une recette.
   *
   * @param id L'ID de la recette à supprimer
   */
  public void deleteRecette(Long id) {
    recetteRepository.deleteById(id);
  }
}
