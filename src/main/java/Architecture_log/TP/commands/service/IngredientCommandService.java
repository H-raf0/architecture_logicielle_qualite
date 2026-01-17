package Architecture_log.TP.commands.service;

import Architecture_log.TP.commands.dto.CreateIngredientDTO;
import Architecture_log.TP.commands.dto.UpdateIngredientDTO;
import Architecture_log.TP.common.entity.Ingredient;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.IngredientRepository;
import Architecture_log.TP.common.repository.RecetteRepository;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer les commandes de création/modification/suppression d'ingrédients.
 *
 * Implémente le pattern CQRS pour les opérations d'ingrédients.
 * Assure que les ingrédients appartiennent à la recette spécifiée.
 */
@Service
public class IngredientCommandService {

  private final IngredientRepository ingredientRepository;
  private final RecetteRepository recetteRepository;

  /**
   * Constructeur du service d'ingrédients.
   *
   * @param ingredientRepository Repository pour accéder aux ingrédients
   * @param recetteRepository Repository pour accéder aux recettes
   */
  public IngredientCommandService(
    IngredientRepository ingredientRepository,
    RecetteRepository recetteRepository
  ) {
    this.ingredientRepository = ingredientRepository;
    this.recetteRepository = recetteRepository;
  }

  /**
   * Ajoute un nouvel ingrédient à une recette.
   *
   * @param idRecette ID de la recette propriétaire
   * @param dto Les données du nouvel ingrédient
   * @return L'ingrédient créé avec son ID
   * @throws RuntimeException Si la recette n'existe pas
   */
  public Ingredient addIngredientToRecette(
    Long idRecette,
    CreateIngredientDTO dto
  ) {
    Recette recette = recetteRepository
      .findById(idRecette)
      .orElseThrow(() -> new RuntimeException("Recette not found"));

    Ingredient ingredient = new Ingredient(dto.getNom(), recette);
    return ingredientRepository.save(ingredient);
  }

  /**
   * Met à jour un ingrédient existant.
   *
   * Vérifie que l'ingrédient appartient bien à la recette spécifiée
   * avant de procéder à la mise à jour.
   *
   * @param idRecette ID de la recette propriétaire
   * @param idIngredient ID de l'ingrédient à modifier
   * @param dto Les nouvelles données de l'ingrédient
   * @return L'ingrédient mis à jour
   * @throws RuntimeException Si l'ingrédient ou la recette n'existe pas,
   *                          ou si l'ingrédient n'appartient pas à la recette
   */
  public Ingredient updateIngredient(
    Long idRecette,
    Long idIngredient,
    UpdateIngredientDTO dto
  ) {
    Ingredient existingIngredient = ingredientRepository
      .findById(idIngredient)
      .orElseThrow(() -> new RuntimeException("Ingredient not found"));

    if (!existingIngredient.getRecette().getId().equals(idRecette)) {
      throw new RuntimeException("Ingredient does not belong to this recette");
    }

    existingIngredient.setNom(dto.getNom());
    return ingredientRepository.save(existingIngredient);
  }

  /**
   * Supprime un ingrédient d'une recette.
   *
   * Valide que l'ingrédient appartient bien à la recette avant suppression.
   *
   * @param idRecette ID de la recette propriétaire
   * @param idIngredient ID de l'ingrédient à supprimer
   * @throws RuntimeException Si l'ingrédient n'existe pas,
   *                          ou s'il n'appartient pas à la recette
   */
  public void deleteIngredient(Long idRecette, Long idIngredient) {
    Ingredient ingredient = ingredientRepository
      .findById(idIngredient)
      .orElseThrow(() -> new RuntimeException("Ingredient not found"));

    if (!ingredient.getRecette().getId().equals(idRecette)) {
      throw new RuntimeException("Ingredient does not belong to this recette");
    }

    ingredientRepository.delete(ingredient);
  }
}
