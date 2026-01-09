package Architecture_log.TP.common.repository;

import Architecture_log.TP.common.entity.Ingredient;
import java.util.List;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
  List<Ingredient> findByRecetteId(Long recetteId);
}
