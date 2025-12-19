package Architecture_log.TP.repository;

import Architecture_log.TP.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>  {

    List<Ingredient> findByRecetteId(Long recetteId);
}
