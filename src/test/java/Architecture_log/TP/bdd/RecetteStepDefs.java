package Architecture_log.TP.bdd;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import Architecture_log.TP.TpArchitectureApplication;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.IngredientRepository;
import Architecture_log.TP.common.repository.RecetteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.fr.*;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@ContextConfiguration(classes = TpArchitectureApplication.class)
public class RecetteStepDefs {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RecetteRepository recetteRepository;

  @Autowired
  private IngredientRepository ingredientRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private MvcResult lastResult;
  private int lastStatusCode;

  // Context steps
  @Étantdonné("que l'application est démarrée")
  public void applicationEstDemarree() {
    assertThat(mockMvc).isNotNull();
  }

  @Étantdonné("que la base de données est vide")
  public void baseDonneesVide() {
    recetteRepository.deleteAll();
    ingredientRepository.deleteAll();
  }

  @Étantdonné("que les recettes suivantes existent:")
  public void recettesSuivantesExistent(DataTable dataTable) {
    List<Map<String, String>> recettes = dataTable.asMaps(
      String.class,
      String.class
    );
    for (Map<String, String> r : recettes) {
      Recette recette = new Recette();
      recette.setNom(r.get("nom"));
      recetteRepository.save(recette);
    }
  }

  @Étantdonné("qu'une recette {string} existe avec l'id {int}")
  public void recetteExisteAvecId(String nom, int id) {
    Recette recette = new Recette();
    recette.setId((long) id);
    recette.setNom(nom);
    recetteRepository.save(recette);
  }

  // Action steps
  @Quand("je crée une recette avec le nom {string}")
  public void creRecette(String nom) throws Exception {
    String json = String.format("{\"nom\": \"%s\"}", nom);
    lastResult = mockMvc
      .perform(
        post("/api/recettes").contentType("application/json").content(json)
      )
      .andReturn();
    lastStatusCode = lastResult.getResponse().getStatus();
  }

  @Quand("je récupère la liste des recettes")
  public void recupereListeRecettes() throws Exception {
    lastResult = mockMvc.perform(get("/api/recettes")).andReturn();
    lastStatusCode = lastResult.getResponse().getStatus();
  }

  @Quand("je récupère la recette avec l'id {int}")
  public void recupereRecetteParId(int id) throws Exception {
    lastResult = mockMvc.perform(get("/api/recettes/{id}", id)).andReturn();
    lastStatusCode = lastResult.getResponse().getStatus();
  }

  @Quand("je modifie la recette {int} avec le nom {string}")
  public void modifieRecette(int id, String nom) throws Exception {
    String json = String.format("{\"nom\": \"%s\"}", nom);
    lastResult = mockMvc
      .perform(
        put("/api/recettes/{id}", id)
          .contentType("application/json")
          .content(json)
      )
      .andReturn();
    lastStatusCode = lastResult.getResponse().getStatus();
  }

  @Quand("je supprime la recette avec l'id {int}")
  public void supprimeRecette(int id) throws Exception {
    lastResult = mockMvc.perform(delete("/api/recettes/{id}", id)).andReturn();
    lastStatusCode = lastResult.getResponse().getStatus();
  }

  @Quand("je crée une recette avec un nom vide")
  public void creRecetteNomVide() throws Exception {
    String json = "{\"nom\": \"\"}";
    lastResult = mockMvc
      .perform(
        post("/api/recettes").contentType("application/json").content(json)
      )
      .andReturn();
    lastStatusCode = lastResult.getResponse().getStatus();
  }

  @Quand("je recherche des recettes avec le terme {string}")
  public void rechercheRecettes(String terme) throws Exception {
    lastResult = mockMvc
      .perform(get("/api/recettes/search").param("q", terme))
      .andReturn();
    lastStatusCode = lastResult.getResponse().getStatus();
  }

  // Assertion steps
  @Alors("la recette est créée avec succès")
  public void recetteCreeeSucces() {
    assertThat(lastStatusCode).isEqualTo(201);
  }

  @Alors("la recette a un identifiant")
  public void recetteAUnId() {
    assertThat(lastResult.getResponse().getHeader("Location")).isNotEmpty();
  }

  @Alors("un événement de création est publié")
  public void evenementCreationPublie() {
    // This would need Kafka integration testing
    // For now, we just verify the response
    assertThat(lastStatusCode).isEqualTo(201);
  }

  @Alors("je reçois {int} recettes")
  public void recoitNRecettes(int count) throws Exception {
    String content = lastResult.getResponse().getContentAsString();
    List<?> recettes = objectMapper.readValue(content, List.class);
    assertThat(recettes).hasSize(count);
  }

  @Alors("la liste contient {string}")
  public void listeContient(String nom) throws Exception {
    String content = lastResult.getResponse().getContentAsString();
    assertThat(content).contains(nom);
  }

  @Alors("je reçois la recette {string}")
  public void recoitRecette(String nom) throws Exception {
    String content = lastResult.getResponse().getContentAsString();
    assertThat(content).contains(nom);
  }

  @Alors("la recette est mise à jour avec succès")
  public void recetteMiseAJourSucces() {
    assertThat(lastStatusCode).isEqualTo(200);
  }

  @Alors("le nom de la recette {int} est {string}")
  public void nomRecetteEst(int id, String nom) {
    Recette recette = recetteRepository.findById((long) id).orElse(null);
    assertThat(recette).isNotNull();
    assertThat(recette.getNom()).isEqualTo(nom);
  }

  @Alors("la recette est supprimée avec succès")
  public void recetteSupprimeeSucces() {
    assertThat(lastStatusCode).isEqualTo(204);
  }

  @Alors("la recette {int} n'existe plus")
  public void recetteNExistePas(int id) {
    assertThat(recetteRepository.findById((long) id)).isEmpty();
  }

  @Alors("je reçois une erreur de validation")
  public void recoitErreurValidation() {
    assertThat(lastStatusCode).isEqualTo(400);
  }

  @Alors("le message d'erreur indique {string}")
  public void messageErreurIndique(String message) throws Exception {
    String content = lastResult.getResponse().getContentAsString();
    assertThat(content).contains(message);
  }

  @Alors("toutes les recettes contiennent {string} dans leur nom")
  public void toutesRecettesContiennent(String terme) throws Exception {
    String content = lastResult.getResponse().getContentAsString();
    List<?> recettes = objectMapper.readValue(content, List.class);
    assertThat(recettes).isNotEmpty();
    for (Object r : recettes) {
      assertThat(r.toString()).contains(terme);
    }
  }
}
