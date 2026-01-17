package Architecture_log.TP.common.repository;

import Architecture_log.TP.common.entity.Ingredient;
import java.util.List;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository pour accéder aux ingrédients en base de données.
 *
 * Fournit les méthodes CRUD standard via {@link JpaRepository}
 * ainsi que des méthodes personnalisées pour les requêtes métier.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
  /**
   * Récupère tous les ingrédients d'une recette.
   *
   * @param recetteId ID de la recette propriétaire
   * @return Liste des ingrédients appartenant à cette recette
   */
  List<Ingredient> findByRecetteId(Long recetteId);
}
