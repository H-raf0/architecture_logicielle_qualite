package Architecture_log.TP.queries.service;

import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import Architecture_log.TP.queries.dto.RecetteDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer les requêtes de lecture des recettes.
 *
 * Implémente le pattern CQRS (Command Query Responsibility Segregation).
 * Ce service est responsable uniquement des opérations de lecture (queries).
 * Les modifications sont gérées par RecetteCommandService.
 */
@Service
public class RecetteQueryService {

  private final RecetteRepository recetteRepository;

  /**
   * Constructeur du service de requêtes de recettes.
   *
   * @param recetteRepository Repository pour accéder aux recettes en BD
   */
  public RecetteQueryService(RecetteRepository recetteRepository) {
    this.recetteRepository = recetteRepository;
  }

  /**
   * Récupère toutes les recettes existantes.
   *
   * @return Liste de toutes les recettes disponibles (DTO)
   */
  public List<RecetteDTO> getAllRecettes() {
    return recetteRepository
      .findAll()
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  /**
   * Récupère une recette par son ID.
   *
   * @param id ID unique de la recette
   * @return La recette demandée (DTO)
   * @throws RuntimeException Si aucune recette avec cet ID n'existe
   */
  public RecetteDTO getRecetteById(Long id) {
    Recette recette = recetteRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("Recette not found"));
    return toDTO(recette);
  }

  /**
   * Convertit une entité Recette en DTO.
   *
   * @param recette L'entité Recette à convertir
   * @return Le DTO correspondant
   */
  private RecetteDTO toDTO(Recette recette) {
    return new RecetteDTO(recette.getId(), recette.getNom());
  }
}
