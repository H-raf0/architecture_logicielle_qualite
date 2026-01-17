package Architecture_log.TP.queries.service;

import Architecture_log.TP.common.entity.Ingredient;
import Architecture_log.TP.common.repository.IngredientRepository;
import Architecture_log.TP.queries.dto.IngredientDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer les requêtes de lecture des ingrédients.
 * 
 * Implémente le pattern CQRS pour les opérations d'ingrédients.
 * Ce service assure l'accès en lecture aux ingrédients filtrés par recette.
 * Les modifications sont gérées par IngredientCommandService.
 */
@Service
public class IngredientQueryService {

  private final IngredientRepository ingredientRepository;

  /**
   * Constructeur du service de requêtes d'ingrédients.
   * 
   * @param ingredientRepository Repository pour accéder aux ingrédients en BD
   */
  public IngredientQueryService(IngredientRepository ingredientRepository) {
    this.ingredientRepository = ingredientRepository;
  }

  /**
   * Récupère tous les ingrédients d'une recette.
   * 
   * @param idRecette ID de la recette propriétaire
   * @return Liste de tous les ingrédients associés à la recette (DTO)
   */
  public List<IngredientDTO> getIngredientsByRecette(Long idRecette) {
    return ingredientRepository
      .findByRecetteId(idRecette)
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  /**
   * Convertit une entité Ingredient en DTO.
   * 
   * @param ingredient L'entité Ingredient à convertir
   * @return Le DTO correspondant
   */
  private IngredientDTO toDTO(Ingredient ingredient) {
    return new IngredientDTO(
      ingredient.getId(),
      ingredient.getNom(),
      ingredient.getRecette().getId()
    );
  }
}
