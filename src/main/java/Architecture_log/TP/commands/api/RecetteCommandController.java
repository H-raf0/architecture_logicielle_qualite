package Architecture_log.TP.commands.api;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.dto.UpdateRecetteDTO;
import Architecture_log.TP.commands.service.RecetteCommandService;
import Architecture_log.TP.common.entity.Recette;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recettes")
public class RecetteCommandController {

  private final RecetteCommandService recetteCommandService;

  public RecetteCommandController(RecetteCommandService recetteCommandService) {
    this.recetteCommandService = recetteCommandService;
  }

  @PostMapping
  public Recette createRecette(@RequestBody CreateRecetteDTO dto) {
    return recetteCommandService.createRecette(dto);
  }

  @PutMapping("/{id}")
  public Recette updateRecette(
    @PathVariable Long id,
    @RequestBody UpdateRecetteDTO dto
  ) {
    return recetteCommandService.updateRecette(id, dto);
  }

  @DeleteMapping("/{id}")
  public void deleteRecette(@PathVariable Long id) {
    recetteCommandService.deleteRecette(id);
  }
}
