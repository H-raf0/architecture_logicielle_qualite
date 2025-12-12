package Architecture_log.TP.controller;

import Architecture_log.TP.entity.Ingredient;
import Architecture_log.TP.entity.Recette;
import Architecture_log.TP.service.IngredientService;
import Architecture_log.TP.service.RecetteService;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }
    /*
    @GetMapping
    public List<Recette> getAllIngredientService() {
        return ingredientService.getAllIngredient();
    }

    @PostMapping
    public Recette createRecette(@RequestBody Recette recette) {
        return ingredientService.createIngredient(recette);
    }

    @PutMapping("/{id}")
    public Recette updateRecette(
            @PathVariable Long id,
            @RequestBody Recette recette) {
        return ingredientService.updateIngredient(id, recette);
    }

    @DeleteMapping("/{id}")
    public void deleteRecette(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
    }
    */

}
