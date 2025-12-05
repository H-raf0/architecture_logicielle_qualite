package Architecture_log.TP.controller;

import Architecture_log.TP.entity.RecetteTest;
import Architecture_log.TP.service.RecetteServiceTest;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recettes")
public class RecetteController {

  private final RecetteServiceTest recetteService;

  public RecetteController(RecetteServiceTest recetteService) {
    this.recetteService = recetteService;
  }

  @GetMapping
  public List<RecetteTest> getRecettes() {
    return recetteService.getAllRecettes();
  }

  @PostMapping
  public RecetteTest createRecette(@RequestBody RecetteTest recette) {
    return recetteService.createRecette(recette);
  }

  @PutMapping("/{id}")
  public RecetteTest updateRecette(
    @PathVariable Long id,
    @RequestBody RecetteTest recette
  ) {
    return recetteService.updateRecette(id, recette);
  }

  @DeleteMapping("/{id}")
  public void deleteRecette(@PathVariable Long id) {
    recetteService.deleteRecette(id);
  }
}
