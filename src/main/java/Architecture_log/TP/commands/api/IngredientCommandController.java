package Architecture_log.TP.commands.api;

import Architecture_log.TP.commands.dto.CreateIngredientDTO;
import Architecture_log.TP.commands.dto.UpdateIngredientDTO;
import Architecture_log.TP.commands.service.IngredientCommandService;
import Architecture_log.TP.common.entity.Ingredient;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller pour les opérations de modification d'ingrédients (CQRS Command).
 *
 * Gère les requêtes:
 * - POST /recettes/{idRecette}/ingredients : Ajouter un ingrédient
 * - PUT /recettes/{idRecette}/ingredients/{idIngredient} : Modifier un ingrédient
 * - DELETE /recettes/{idRecette}/ingredients/{idIngredient} : Supprimer un ingrédient
 *
 * @see IngredientCommandService pour la logique métier
 */
@RestController
@RequestMapping("/recettes/{idRecette}/ingredients")
public class IngredientCommandController {

  private final IngredientCommandService ingredientCommandService;

  /**
   * Constructeur du contrôleur.
   *
   * @param ingredientCommandService Service pour les opérations d'écriture
   */
  public IngredientCommandController(
    IngredientCommandService ingredientCommandService
  ) {
    this.ingredientCommandService = ingredientCommandService;
  }

  /**
   * Ajoute un nouvel ingrédient à une recette.
   *
   * @param idRecette ID de la recette propriétaire
   * @param dto Données du nouvel ingrédient
   * @return L'ingrédient créé avec son ID
   */
  @PostMapping
  public Ingredient addIngredient(
    @PathVariable Long idRecette,
    @RequestBody CreateIngredientDTO dto
  ) {
    return ingredientCommandService.addIngredientToRecette(idRecette, dto);
  }

  /**
   * Met à jour un ingrédient existant.
   *
   * @param idRecette ID de la recette propriétaire
   * @param idIngredient ID de l'ingrédient à modifier
   * @param dto Nouvelles données de l'ingrédient
   * @return L'ingrédient mis à jour
   */
  @PutMapping("/{idIngredient}")
  public Ingredient updateIngredient(
    @PathVariable Long idRecette,
    @PathVariable Long idIngredient,
    @RequestBody UpdateIngredientDTO dto
  ) {
    return ingredientCommandService.updateIngredient(
      idRecette,
      idIngredient,
      dto
    );
  }

  /**
   * Supprime un ingrédient d'une recette.
   *
   * @param idRecette ID de la recette propriétaire
   * @param idIngredient ID de l'ingrédient à supprimer
   */
  @DeleteMapping("/{idIngredient}")
  public void deleteIngredient(
    @PathVariable Long idRecette,
    @PathVariable Long idIngredient
  ) {
    ingredientCommandService.deleteIngredient(idRecette, idIngredient);
  }
}
