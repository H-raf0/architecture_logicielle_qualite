package Architecture_log.TP.commands.api;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.dto.UpdateRecetteDTO;
import Architecture_log.TP.commands.service.RecetteCommandService;
import Architecture_log.TP.common.entity.Recette;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller pour les opérations de modification de recettes (CQRS Command).
 *
 * Gère les requêtes:
 * - POST /recettes : Créer une recette
 * - PUT /recettes/{id} : Modifier une recette
 * - DELETE /recettes/{id} : Supprimer une recette
 *
 * @see RecetteCommandService pour la logique métier
 */
@RestController
@RequestMapping("/recettes")
public class RecetteCommandController {

  private final RecetteCommandService recetteCommandService;

  /**
   * Constructeur du contrôleur.
   *
   * @param recetteCommandService Service pour les opérations d'écriture
   */
  public RecetteCommandController(RecetteCommandService recetteCommandService) {
    this.recetteCommandService = recetteCommandService;
  }

  /**
   * Crée une nouvelle recette.
   *
   * @param dto Données de la recette à créer (nom)
   * @return La recette créée avec son ID généré
   */
  @PostMapping
  public Recette createRecette(@RequestBody CreateRecetteDTO dto) {
    return recetteCommandService.createRecette(dto);
  }

  /**
   * Met à jour une recette existante.
   *
   * @param id ID de la recette à modifier
   * @param dto Nouvelles données de la recette
   * @return La recette mise à jour
   */
  @PutMapping("/{id}")
  public Recette updateRecette(
    @PathVariable Long id,
    @RequestBody UpdateRecetteDTO dto
  ) {
    return recetteCommandService.updateRecette(id, dto);
  }

  /**
   * Supprime une recette.
   *
   * @param id ID de la recette à supprimer
   */
  @DeleteMapping("/{id}")
  public void deleteRecette(@PathVariable Long id) {
    recetteCommandService.deleteRecette(id);
  }
}
