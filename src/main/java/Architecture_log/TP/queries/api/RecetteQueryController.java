package Architecture_log.TP.queries.api;

import Architecture_log.TP.queries.dto.RecetteDTO;
import Architecture_log.TP.queries.service.RecetteQueryService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller pour les opérations de lecture de recettes (CQRS Query).
 *
 * Gère les requêtes:
 * - GET /recettes : Récupérer toutes les recettes
 * - GET /recettes/{id} : Récupérer une recette par ID
 *
 * @see RecetteQueryService pour la logique métier
 */
@RestController
@RequestMapping("/recettes")
public class RecetteQueryController {

  private final RecetteQueryService recetteQueryService;

  /**
   * Constructeur du contrôleur.
   *
   * @param recetteQueryService Service pour les opérations de lecture
   */
  public RecetteQueryController(RecetteQueryService recetteQueryService) {
    this.recetteQueryService = recetteQueryService;
  }

  /**
   * Récupère toutes les recettes.
   *
   * @return Liste complète des recettes
   */
  @GetMapping
  public List<RecetteDTO> getAllRecettes() {
    return recetteQueryService.getAllRecettes();
  }

  /**
   * Récupère une recette par son ID.
   *
   * @param id ID de la recette à récupérer
   * @return La recette demandée
   */
  @GetMapping("/{id}")
  public RecetteDTO getRecetteById(@PathVariable Long id) {
    return recetteQueryService.getRecetteById(id);
  }
}
