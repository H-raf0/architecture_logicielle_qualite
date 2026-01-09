package Architecture_log.TP.entity;

import static org.junit.jupiter.api.Assertions.*;

import Architecture_log.TP.common.entity.Recette;
import org.junit.jupiter.api.Test;

public class RecetteTest {

  @Test
  void shouldCreateRecette() {
    Recette recette = new Recette("Test Recette");
    assertNotNull(recette);
    assertEquals("Test Recette", recette.getNom());
  }

  @Test
  void shouldSetAndGetId() {
    Recette recette = new Recette();
    recette.setId(1L);
    assertEquals(1L, recette.getId());
  }

  @Test
  void shouldSetAndGetNom() {
    Recette recette = new Recette();
    recette.setNom("New Name");
    assertEquals("New Name", recette.getNom());
  }
}
