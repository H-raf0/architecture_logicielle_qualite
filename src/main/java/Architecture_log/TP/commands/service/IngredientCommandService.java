package Architecture_log.TP.commands.service;

import Architecture_log.TP.commands.dto.CreateIngredientDTO;
import Architecture_log.TP.commands.dto.UpdateIngredientDTO;
import Architecture_log.TP.common.entity.Ingredient;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.IngredientRepository;
import Architecture_log.TP.common.repository.RecetteRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientCommandService {

  private final IngredientRepository ingredientRepository;
  private final RecetteRepository recetteRepository;

  public IngredientCommandService(
    IngredientRepository ingredientRepository,
    RecetteRepository recetteRepository
  ) {
    this.ingredientRepository = ingredientRepository;
    this.recetteRepository = recetteRepository;
  }

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

