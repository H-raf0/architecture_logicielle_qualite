package Architecture_log.TP.queries.api;

import Architecture_log.TP.queries.dto.IngredientDTO;
import Architecture_log.TP.queries.service.IngredientQueryService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recettes/{idRecette}/ingredients")
public class IngredientQueryController {

  private final IngredientQueryService ingredientQueryService;

  public IngredientQueryController(
    IngredientQueryService ingredientQueryService
  ) {
    this.ingredientQueryService = ingredientQueryService;
  }

  @GetMapping
  public List<IngredientDTO> getIngredientsByRecette(
    @PathVariable Long idRecette
  ) {
    return ingredientQueryService.getIngredientsByRecette(idRecette);
  }
}

