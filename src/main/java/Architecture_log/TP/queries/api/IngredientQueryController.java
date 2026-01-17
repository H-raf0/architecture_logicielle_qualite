package Architecture_log.TP.queries.api;

import Architecture_log.TP.queries.dto.IngredientDTO;
import Architecture_log.TP.queries.service.IngredientQueryService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller pour les opérations de lecture d'ingrédients (CQRS Query).
 * 
 * Gère les requêtes:
 * - GET /recettes/{idRecette}/ingredients : Récupérer tous les ingrédients d'une recette
 * 
 * @see IngredientQueryService pour la logique métier
 */
@RestController
@RequestMapping("/recettes/{idRecette}/ingredients")
public class IngredientQueryController {

  private final IngredientQueryService ingredientQueryService;

  /**
   * Constructeur du contrôleur.
   * 
   * @param ingredientQueryService Service pour les opérations de lecture
   */
  public IngredientQueryController(
    IngredientQueryService ingredientQueryService
  ) {
    this.ingredientQueryService = ingredientQueryService;
  }

  /**
   * Récupère tous les ingrédients d'une recette.
   * 
   * @param idRecette ID de la recette propriétaire
   * @return Liste des ingrédients associés à la recette
   */
  @GetMapping
  public List<IngredientDTO> getIngredientsByRecette(
    @PathVariable Long idRecette
  ) {
    return ingredientQueryService.getIngredientsByRecette(idRecette);
  }
}
