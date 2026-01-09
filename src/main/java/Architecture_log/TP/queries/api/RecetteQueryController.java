package Architecture_log.TP.queries.api;

import Architecture_log.TP.queries.dto.RecetteDTO;
import Architecture_log.TP.queries.service.RecetteQueryService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recettes")
public class RecetteQueryController {

  private final RecetteQueryService recetteQueryService;

  public RecetteQueryController(RecetteQueryService recetteQueryService) {
    this.recetteQueryService = recetteQueryService;
  }

  @GetMapping
  public List<RecetteDTO> getAllRecettes() {
    return recetteQueryService.getAllRecettes();
  }

  @GetMapping("/{id}")
  public RecetteDTO getRecetteById(@PathVariable Long id) {
    return recetteQueryService.getRecetteById(id);
  }
}

