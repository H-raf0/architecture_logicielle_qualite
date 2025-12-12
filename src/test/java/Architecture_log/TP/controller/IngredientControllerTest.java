package Architecture_log.TP.controller;

//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Architecture_log.TP.entity.Ingredient;
import Architecture_log.TP.service.IngredientService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IngredientControllerTest {

  private IngredientService ingredientService;
  private IngredientController controller;

  @BeforeEach
  void setup() {
    ingredientService = mock(IngredientService.class);
    controller = new IngredientController(ingredientService);
  }
  /*
    // Helper pour construire une Recette
    private Ingredient buildIngredient(Long id, String nom) {
        Ingredient r = new Ingredient();
        r.setId(id);
        r.setNom(nom);
        return r;
    }

    @Test
    void shouldReturnAllRecettes() {
        Ingredient r1 = buildRecette(1L, "Tarte aux pommes");
        Ingredient r2 = buildRecette(2L, "Quiche Lorraine");

        when(recetteService.getAllRecettes()).thenReturn(List.of(r1, r2));

        List<Ingredient> res = controller.getAllRecettes();

        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals("Tarte aux pommes", res.get(0).getNom());
        verify(recetteService, times(1)).getAllRecettes();
    }
    /*
    @Test
    void shouldReturnRecetteById() {
        Recette r = buildRecette(5L, "Soupe");
        when(recetteService.getRecetteById(5L)).thenReturn(r);

        Recette res = controller.getRecetteById(5L);

        assertNotNull(res);
        assertEquals(5L, res.getId());
        assertEquals("Soupe", res.getNom());
        verify(recetteService, times(1)).getRecetteById(5L);
    }
    * /

    @Test
    void shouldCreateRecette() {
        Ingredient input = buildRecette(null, "Nouvelle recette");
        Ingredient saved = buildRecette(10L, "Nouvelle recette");

        when(recetteService.createRecette(input)).thenReturn(saved);

        Ingredient res = controller.createRecette(input);

        assertNotNull(res);
        assertEquals(10L, res.getId());
        assertEquals("Nouvelle recette", res.getNom());
        verify(recetteService, times(1)).createRecette(input);
    }

    @Test
    void shouldUpdateRecette() {
        Ingredient input = buildRecette(null, "Modifiée");
        Ingredient updated = buildRecette(3L, "Modifiée");

        when(recetteService.updateRecette(3L, input)).thenReturn(updated);

        Ingredient res = controller.updateRecette(3L, input);

        assertNotNull(res);
        assertEquals(3L, res.getId());
        assertEquals("Modifiée", res.getNom());
        verify(recetteService, times(1)).updateRecette(3L, input);
    }

    @Test
    void shouldDeleteRecette() {
        // deleteRecette est void, on vérifie l'appel
        doNothing().when(recetteService).deleteRecette(7L);

        controller.deleteRecette(7L);

        verify(recetteService, times(1)).deleteRecette(7L);
    }
    */
}
