package Architecture_log.TP.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Architecture_log.TP.commands.api.RecetteCommandController;
import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.dto.UpdateRecetteDTO;
import Architecture_log.TP.commands.service.RecetteCommandService;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.queries.api.RecetteQueryController;
import Architecture_log.TP.queries.dto.RecetteDTO;
import Architecture_log.TP.queries.service.RecetteQueryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecetteControllerTest {

  private RecetteCommandService recetteCommandService;
  private RecetteQueryService recetteQueryService;
  private RecetteCommandController commandController;
  private RecetteQueryController queryController;

  @BeforeEach
  void setup() {
    recetteCommandService = mock(RecetteCommandService.class);
    recetteQueryService = mock(RecetteQueryService.class);
    commandController = new RecetteCommandController(recetteCommandService);
    queryController = new RecetteQueryController(recetteQueryService);
  }

  // Helper pour construire une Recette
  private Recette buildRecette(Long id, String nom) {
    Recette r = new Recette();
    r.setId(id);
    r.setNom(nom);
    return r;
  }

  private Architecture_log.TP.queries.dto.RecetteDTO buildRecetteDTO(
    Long id,
    String nom
  ) {
    Architecture_log.TP.queries.dto.RecetteDTO dto =
      new Architecture_log.TP.queries.dto.RecetteDTO();
    dto.setId(id);
    dto.setNom(nom);
    return dto;
  }

  @Test
  void shouldReturnAllRecettes() {
    RecetteDTO r1 = buildRecetteDTO(1L, "Tarte aux pommes");
    RecetteDTO r2 = buildRecetteDTO(2L, "Quiche Lorraine");

    when(recetteQueryService.getAllRecettes()).thenReturn(List.of(r1, r2));

    List<Architecture_log.TP.queries.dto.RecetteDTO> res =
      queryController.getAllRecettes();

    assertNotNull(res);
    assertEquals(2, res.size());
    assertEquals("Tarte aux pommes", res.get(0).getNom());
    verify(recetteQueryService, times(1)).getAllRecettes();
  }

  @Test
  void shouldCreateRecette() {
    Architecture_log.TP.commands.dto.CreateRecetteDTO input =
      new Architecture_log.TP.commands.dto.CreateRecetteDTO("Nouvelle recette");
    Recette saved = buildRecette(10L, "Nouvelle recette");

    when(recetteCommandService.createRecette(input)).thenReturn(saved);

    Recette res = commandController.createRecette(input);

    assertNotNull(res);
    assertEquals(10L, res.getId());
    assertEquals("Nouvelle recette", res.getNom());
    verify(recetteCommandService, times(1)).createRecette(input);
  }

  @Test
  void shouldUpdateRecette() {
    Architecture_log.TP.commands.dto.UpdateRecetteDTO input =
      new Architecture_log.TP.commands.dto.UpdateRecetteDTO("Modifiée");
    Recette updated = buildRecette(3L, "Modifiée");

    when(recetteCommandService.updateRecette(3L, input)).thenReturn(updated);

    Recette res = commandController.updateRecette(3L, input);

    assertNotNull(res);
    assertEquals(3L, res.getId());
    assertEquals("Modifiée", res.getNom());
    verify(recetteCommandService, times(1)).updateRecette(3L, input);
  }

  @Test
  void shouldDeleteRecette() {
    // deleteRecette est void, on vérifie l'appel
    doNothing().when(recetteCommandService).deleteRecette(7L);

    commandController.deleteRecette(7L);

    verify(recetteCommandService, times(1)).deleteRecette(7L);
  }
}
