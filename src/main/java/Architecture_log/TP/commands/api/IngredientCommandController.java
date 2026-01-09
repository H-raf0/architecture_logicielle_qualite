package Architecture_log.TP.commands.api;

import Architecture_log.TP.commands.dto.CreateIngredientDTO;
import Architecture_log.TP.commands.dto.UpdateIngredientDTO;
import Architecture_log.TP.commands.service.IngredientCommandService;
import Architecture_log.TP.common.entity.Ingredient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recettes/{idRecette}/ingredients")
public class IngredientCommandController {

  private final IngredientCommandService ingredientCommandService;

  public IngredientCommandController(
    IngredientCommandService ingredientCommandService
  ) {
    this.ingredientCommandService = ingredientCommandService;
  }

  @PostMapping
  public Ingredient addIngredient(
    @PathVariable Long idRecette,
    @RequestBody CreateIngredientDTO dto
  ) {
    return ingredientCommandService.addIngredientToRecette(idRecette, dto);
  }

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

  @DeleteMapping("/{idIngredient}")
  public void deleteIngredient(
    @PathVariable Long idRecette,
    @PathVariable Long idIngredient
  ) {
    ingredientCommandService.deleteIngredient(idRecette, idIngredient);
  }
}
