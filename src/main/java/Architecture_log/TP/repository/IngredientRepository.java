package Architecture_log.TP.repository;

import Architecture_log.TP.entity.Ingredient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
  List<Ingredient> findByRecetteId(Long recetteId);
}
