package Architecture_log.TP.service;

import Architecture_log.TP.entity.Ingredient;
import Architecture_log.TP.entity.Recette;
import Architecture_log.TP.repository.IngredientRepository;
import Architecture_log.TP.repository.RecetteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final RecetteRepository recetteRepository;

    public IngredientService(IngredientRepository ingredientRepository, RecetteRepository recetteRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recetteRepository = recetteRepository;
    }

    public List<Ingredient> getIngredientsByRecette(Long id) {
        return ingredientRepository.findByRecetteId(id);
    }

    public Ingredient addIngredientToRecette(Long idRecette, Ingredient ingredient) {

        Recette recette = recetteRepository.findById(idRecette)
                .orElseThrow(() -> new RuntimeException("Recette not found"));

        ingredient.setId(null);
        ingredient.setRecette(recette);

        return ingredientRepository.save(ingredient);
    }

    public Ingredient updateIngredient(Long idRecette, Long idIngredient, Ingredient ingredient) {

        Ingredient existingIngredient = ingredientRepository.findById(idIngredient)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        if (!existingIngredient.getRecette().getId().equals(idRecette)) {
            throw new RuntimeException("Ingredient does not belong to this recette");
        }

        existingIngredient.setNom(ingredient.getNom());

        return ingredientRepository.save(existingIngredient);
    }

    public void deleteIngredient(Long idRecette, Long idIngredient) {

        Ingredient ingredient = ingredientRepository.findById(idIngredient)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        if (!ingredient.getRecette().getId().equals(idRecette)) {
            throw new RuntimeException("Ingredient does not belong to this recette");
        }

        ingredientRepository.delete(ingredient);
    }

}
