package Architecture_log.TP.controller;

import Architecture_log.TP.entity.Ingredient;
import Architecture_log.TP.entity.Recette;
import Architecture_log.TP.service.IngredientService;
import Architecture_log.TP.service.RecetteService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recettes/{idRecette}/ingredients")
public class IngredientController {

  private final IngredientService ingredientService;

  public IngredientController(IngredientService ingredientService) {
    this.ingredientService = ingredientService;
  }

  @GetMapping
  public List<Ingredient> getIngredientsByRecette(
    @PathVariable Long idRecette
  ) {
    return ingredientService.getIngredientsByRecette(idRecette);
  }

  @PostMapping
  public Ingredient addIngredient(
    @PathVariable Long idRecette,
    @RequestBody Ingredient ingredient
  ) {
    return ingredientService.addIngredientToRecette(idRecette, ingredient);
  }

  @PutMapping("/{idIngredient}")
  public Ingredient updateIngredient(
    @PathVariable Long idRecette,
    @PathVariable Long idIngredient,
    @RequestBody Ingredient ingredient
  ) {
    return ingredientService.updateIngredient(
      idRecette,
      idIngredient,
      ingredient
    );
  }

  @DeleteMapping("/{idIngredient}")
  public void deleteIngredient(
    @PathVariable Long idRecette,
    @PathVariable Long idIngredient
  ) {
    ingredientService.deleteIngredient(idRecette, idIngredient);
  }
}
