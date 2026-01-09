package Architecture_log.TP.entity;

import static org.junit.jupiter.api.Assertions.*;

import Architecture_log.TP.common.entity.Ingredient;
import Architecture_log.TP.common.entity.Recette;
import org.junit.jupiter.api.Test;

public class IngredientTest {

  @Test
  void shouldCreateIngredient() {
    Recette recette = new Recette("Test Recette");
    Ingredient ingredient = new Ingredient("Test Ingredient", recette);
    assertNotNull(ingredient);
    assertEquals("Test Ingredient", ingredient.getNom());
    assertEquals(recette, ingredient.getRecette());
  }

  @Test
  void shouldSetAndGetId() {
    Ingredient ingredient = new Ingredient();
    ingredient.setId(1L);
    assertEquals(1L, ingredient.getId());
  }

  @Test
  void shouldSetAndGetNom() {
    Ingredient ingredient = new Ingredient();
    ingredient.setNom("New Name");
    assertEquals("New Name", ingredient.getNom());
  }
}
