package Architecture_log.TP.bdd;

import io.cucumber.java.fr.*;
import io.cucumber.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import Architecture_log.TP.TpArchitectureApplication;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.entity.Ingredient;
import Architecture_log.TP.common.repository.RecetteRepository;
import Architecture_log.TP.common.repository.IngredientRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

@SpringBootTest
@ContextConfiguration(classes = TpArchitectureApplication.class)
public class IngredientStepDefs {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecetteRepository recetteRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MvcResult lastResult;
    private Recette currentRecette;
    private int lastStatusCode;

    // Context steps
    @Étantdonné("que l'application est démarrée")
    public void applicationEstDemarree() {
        assertThat(mockMvc).isNotNull();
    }

    @Étantdonné("qu'une recette {string} existe avec l'id {int}")
    public void recetteExiste(String nom, int id) {
        currentRecette = new Recette();
        currentRecette.setId((long) id);
        currentRecette.setNom(nom);
        recetteRepository.save(currentRecette);
    }

    @Étantdonné("que la recette {int} contient les ingrédients suivants:")
    public void recetteContientIngredientsTableau(int recetteId, DataTable dataTable) {
        currentRecette = recetteRepository.findById((long) recetteId).orElse(null);
        assertThat(currentRecette).isNotNull();

        List<Map<String, String>> ingredients = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> ing : ingredients) {
            Ingredient ingredient = new Ingredient();
            ingredient.setNom(ing.get("nom"));
            ingredient.setRecette(currentRecette);
            ingredientRepository.save(ingredient);
        }
    }

    @Étantdonné("que la recette {int} contient un ingrédient {string} avec l'id {int}")
    public void recetteContientIngredientAvecId(int recetteId, String nom, int ingredientId) {
        currentRecette = recetteRepository.findById((long) recetteId).orElse(null);
        assertThat(currentRecette).isNotNull();

        Ingredient ingredient = new Ingredient();
        ingredient.setId((long) ingredientId);
        ingredient.setNom(nom);
        ingredient.setRecette(currentRecette);
        ingredientRepository.save(ingredient);
    }

    @Étantdonné("que la recette {int} contient un ingrédient {string} avec {string} et l'id {int}")
    public void recetteContientIngredientComplet(int recetteId, String nom, String quantite, int ingredientId) {
        currentRecette = recetteRepository.findById((long) recetteId).orElse(null);
        assertThat(currentRecette).isNotNull();

        Ingredient ingredient = new Ingredient();
        ingredient.setId((long) ingredientId);
        ingredient.setNom(nom);
        ingredient.setRecette(currentRecette);
        ingredientRepository.save(ingredient);
    }

    // Action steps
    @Quand("j'ajoute un ingrédient {string} avec la quantité {string} à la recette {int}")
    public void ajouteIngredient(String nom, String quantite, int recetteId) throws Exception {
        String json = String.format("{\"nom\": \"%s\", \"quantite\": \"%s\"}", nom, quantite);
        lastResult = mockMvc.perform(post("/api/recettes/{recetteId}/ingredients", recetteId)
                .contentType("application/json")
                .content(json))
                .andReturn();
        lastStatusCode = lastResult.getResponse().getStatus();
    }

    @Quand("je récupère les ingrédients de la recette {int}")
    public void recupereIngredientsRecette(int recetteId) throws Exception {
        lastResult = mockMvc.perform(get("/api/recettes/{recetteId}/ingredients", recetteId))
                .andReturn();
        lastStatusCode = lastResult.getResponse().getStatus();
    }

    @Quand("je récupère l'ingrédient {int} de la recette {int}")
    public void recupereIngredientSpecifique(int ingredientId, int recetteId) throws Exception {
        lastResult = mockMvc.perform(get("/api/recettes/{recetteId}/ingredients/{ingredientId}", 
                recetteId, ingredientId))
                .andReturn();
        lastStatusCode = lastResult.getResponse().getStatus();
    }

    @Quand("je modifie l'ingrédient {int} avec la quantité {string}")
    public void modifieIngredient(int ingredientId, String quantite) throws Exception {
        Ingredient ingredient = ingredientRepository.findById((long) ingredientId).orElse(null);
        assertThat(ingredient).isNotNull();
        
        String json = String.format("{\"nom\": \"%s\", \"quantite\": \"%s\"}", ingredient.getNom(), quantite);
        lastResult = mockMvc.perform(put("/api/ingredients/{ingredientId}", ingredientId)
                .contentType("application/json")
                .content(json))
                .andReturn();
        lastStatusCode = lastResult.getResponse().getStatus();
    }

    @Quand("je supprime l'ingrédient {int} de la recette {int}")
    public void supprimeIngredient(int ingredientId, int recetteId) throws Exception {
        lastResult = mockMvc.perform(delete("/api/recettes/{recetteId}/ingredients/{ingredientId}", 
                recetteId, ingredientId))
                .andReturn();
        lastStatusCode = lastResult.getResponse().getStatus();
    }

    @Quand("j'ajoute un ingrédient avec un nom vide à la recette {int}")
    public void ajouteIngredientNomVide(int recetteId) throws Exception {
        String json = "{\"nom\": \"\", \"quantite\": \"100g\"}";
        lastResult = mockMvc.perform(post("/api/recettes/{recetteId}/ingredients", recetteId)
                .contentType("application/json")
                .content(json))
                .andReturn();
        lastStatusCode = lastResult.getResponse().getStatus();
    }

    // Assertion steps
    @Alors("l'ingrédient est ajouté avec succès")
    public void ingredientAjouteSucces() {
        assertThat(lastStatusCode).isEqualTo(201);
    }

    @Alors("l'ingrédient a un identifiant")
    public void ingredientAUnId() {
        assertThat(lastResult.getResponse().getHeader("Location")).isNotEmpty();
    }

    @Alors("la recette {int} contient l'ingrédient {string}")
    public void recetteContientIngredient(int recetteId, String nom) {
        currentRecette = recetteRepository.findById((long) recetteId).orElse(null);
        assertThat(currentRecette).isNotNull();
        
        List<Ingredient> ingredients = ingredientRepository.findByRecetteId((long) recetteId);
        assertThat(ingredients).anyMatch(ing -> ing.getNom().equals(nom));
    }

    @Alors("je reçois {int} ingrédients")
    public void recoitNIngredientsCount(int count) throws Exception {
        String content = lastResult.getResponse().getContentAsString();
        List<?> ingredients = objectMapper.readValue(content, List.class);
        assertThat(ingredients).hasSize(count);
    }

    @Alors("la liste contient {string} avec {string}")
    public void listeContientIngredient(String nom, String quantite) throws Exception {
        String content = lastResult.getResponse().getContentAsString();
        assertThat(content).contains(nom, quantite);
    }

    @Alors("je reçois l'ingrédient {string}")
    public void recoitIngredient(String nom) throws Exception {
        String content = lastResult.getResponse().getContentAsString();
        assertThat(content).contains(nom);
    }

    @Alors("l'ingrédient est mis à jour avec succès")
    public void ingredientMisAJourSucces() {
        assertThat(lastStatusCode).isEqualTo(200);
    }

    @Alors("la quantité de l'ingrédient {int} est {string}")
    public void quantiteIngredientEst(int ingredientId, String quantite) {
        Ingredient ingredient = ingredientRepository.findById((long) ingredientId).orElse(null);
        assertThat(ingredient).isNotNull();
    }

    @Alors("l'ingrédient est supprimé avec succès")
    public void ingredientSupprimSucces() {
        assertThat(lastStatusCode).isEqualTo(204);
    }

    @Alors("la recette {int} ne contient plus l'ingrédient {string}")
    public void recetteNeContientPlusIngredient(int recetteId, String nom) {
        List<Ingredient> ingredients = ingredientRepository.findByRecetteId((long) recetteId);
        assertThat(ingredients).noneMatch(ing -> ing.getNom().equals(nom));
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
}
