package Architecture_log.TP.queries.service;

import Architecture_log.TP.common.entity.Ingredient;
import Architecture_log.TP.common.repository.IngredientRepository;
import Architecture_log.TP.queries.dto.IngredientDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class IngredientQueryService {

  private final IngredientRepository ingredientRepository;

  public IngredientQueryService(IngredientRepository ingredientRepository) {
    this.ingredientRepository = ingredientRepository;
  }

  public List<IngredientDTO> getIngredientsByRecette(Long idRecette) {
    return ingredientRepository
      .findByRecetteId(idRecette)
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  private IngredientDTO toDTO(Ingredient ingredient) {
    return new IngredientDTO(
      ingredient.getId(),
      ingredient.getNom(),
      ingredient.getRecette().getId()
    );
  }
}
