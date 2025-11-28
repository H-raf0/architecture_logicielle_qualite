package Architecture_log.TP.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import Architecture_log.TP.entity.Recette;
import Architecture_log.TP.service.RecetteService;

@RestController
@RequestMapping("/recettes")
public class RecetteController {

    private final RecetteService recetteService;

    public RecetteController(RecetteService recetteService) {
        this.recetteService = recetteService;
    }

    @GetMapping
    public List<Recette> getRecettes() {
        return recetteService.getAllRecettes();
    }

    @PostMapping
    public Recette createRecette(@RequestBody Recette recette) {
        return recetteService.createRecette(recette);
    }

    @PutMapping("/{id}")
    public Recette updateRecette(@PathVariable Long id, @RequestBody Recette recette) {
        return recetteService.updateRecette(id, recette);
    }

    @DeleteMapping("/{id}")
    public void deleteRecette(@PathVariable Long id) {
        recetteService.deleteRecette(id);
    }
}
