package Architecture_log.TP.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Architecture_log.TP.commands.api.IngredientCommandController;

import Architecture_log.TP.commands.service.IngredientCommandService;
import Architecture_log.TP.common.entity.Ingredient;
import Architecture_log.TP.queries.api.IngredientQueryController;
import Architecture_log.TP.queries.dto.IngredientDTO;
import Architecture_log.TP.queries.service.IngredientQueryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IngredientControllerTest {

  private IngredientCommandService ingredientCommandService;
  private IngredientQueryService ingredientQueryService;
  private IngredientCommandController commandController;
  private IngredientQueryController queryController;

  @BeforeEach
  void setup() {
    ingredientCommandService = mock(IngredientCommandService.class);
    ingredientQueryService = mock(IngredientQueryService.class);
    commandController = new IngredientCommandController(
      ingredientCommandService
    );
    queryController = new IngredientQueryController(ingredientQueryService);
  }

  private Ingredient buildIngredient(Long id, String nom) {
    Ingredient i = new Ingredient();
    i.setId(id);
    i.setNom(nom);
    return i;
  }

  private IngredientDTO buildIngredientDTO(
    Long id,
    String nom,
    Long recetteId
  ) {
    IngredientDTO dto = new IngredientDTO();
    dto.setId(id);
    dto.setNom(nom);
    dto.setRecetteId(recetteId);
    return dto;
  }

  @Test
  void shouldReturnIngredientsByRecette() {
    IngredientDTO i1 = buildIngredientDTO(1L, "Farine", 1L);
    IngredientDTO i2 = buildIngredientDTO(2L, "Sucre", 1L);

    when(ingredientQueryService.getIngredientsByRecette(1L)).thenReturn(
      List.of(i1, i2)
    );

    List<IngredientDTO> res = queryController.getIngredientsByRecette(1L);

    assertNotNull(res);
    assertEquals(2, res.size());
    assertEquals("Farine", res.get(0).getNom());
    verify(ingredientQueryService, times(1)).getIngredientsByRecette(1L);
  }

  @Test
  void shouldAddIngredient() {
    Architecture_log.TP.commands.dto.CreateIngredientDTO input =
      new Architecture_log.TP.commands.dto.CreateIngredientDTO(
        "Nouvel ingredient"
      );
    Ingredient saved = buildIngredient(10L, "Nouvel ingredient");

    when(ingredientCommandService.addIngredientToRecette(1L, input)).thenReturn(
      saved
    );

    Ingredient res = commandController.addIngredient(1L, input);

    assertNotNull(res);
    assertEquals(10L, res.getId());
    assertEquals("Nouvel ingredient", res.getNom());
    verify(ingredientCommandService, times(1)).addIngredientToRecette(
      1L,
      input
    );
  }
}
