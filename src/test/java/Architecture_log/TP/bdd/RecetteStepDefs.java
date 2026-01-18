package Architecture_log.TP.bdd;

import static org.assertj.core.api.Assertions.*;

import Architecture_log.TP.TpArchitectureApplication;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
@ContextConfiguration(classes = TpArchitectureApplication.class)
public class RecetteStepDefs {

  @Autowired
  private RecetteRepository recetteRepository;

  private Recette lastCreatedRecette;
  private Recette lastRetrievedRecette;
  private List<Recette> lastRecettesList;
  private Exception lastException;

  // Context steps
  @Given("the application is started")
  public void applicationIsStarted() {
    assertThat(recetteRepository).isNotNull();
  }

  @Given("the database is empty")
  public void databaseIsEmpty() {
    recetteRepository.deleteAll();
  }

  // Action steps
  @When("I create a recipe with name {string}")
  public void iCreateRecipeWithName(String nom) {
    try {
      lastCreatedRecette = new Recette();
      lastCreatedRecette.setNom(nom);
      lastCreatedRecette = recetteRepository.save(lastCreatedRecette);
      lastException = null;
    } catch (Exception e) {
      lastException = e;
    }
  }

  // Assertion steps
  @Then("the recipe is created successfully")
  public void recipeCreatedSuccessfully() {
    assertThat(lastException).isNull();
    assertThat(lastCreatedRecette).isNotNull();
  }

  @Then("the recipe has an id")
  public void recipeHasAnId() {
    assertThat(lastCreatedRecette).isNotNull();
    assertThat(lastCreatedRecette.getId()).isNotNull();
  }

  // Additional step definitions for new scenarios
  @Given("a recipe {string} exists")
  public void aRecipeExists(String nom) {
    lastCreatedRecette = new Recette();
    lastCreatedRecette.setNom(nom);
    lastCreatedRecette = recetteRepository.save(lastCreatedRecette);
  }

  @When("I retrieve the recipe by id")
  public void iRetrieveRecipeById() {
    try {
      lastRetrievedRecette = recetteRepository
        .findById(lastCreatedRecette.getId())
        .orElse(null);
      lastException = null;
    } catch (Exception e) {
      lastException = e;
    }
  }

  @Then("the recipe is returned successfully")
  public void recipeReturnedSuccessfully() {
    assertThat(lastException).isNull();
    assertThat(lastRetrievedRecette).isNotNull();
  }

  @Then("the recipe name is {string}")
  public void recipeNameIs(String nom) {
    // Use lastRetrievedRecette if it exists, otherwise use lastCreatedRecette
    Recette recetteToCheck = lastRetrievedRecette != null
      ? lastRetrievedRecette
      : lastCreatedRecette;
    assertThat(recetteToCheck).isNotNull();
    assertThat(recetteToCheck.getNom()).isEqualTo(nom);
  }

  @When("I update the recipe name to {string}")
  public void iUpdateRecipeNameTo(String nouveauNom) {
    try {
      lastCreatedRecette.setNom(nouveauNom);
      lastCreatedRecette = recetteRepository.save(lastCreatedRecette);
      lastException = null;
    } catch (Exception e) {
      lastException = e;
    }
  }

  @Then("the recipe is updated successfully")
  public void recipeUpdatedSuccessfully() {
    assertThat(lastException).isNull();
    assertThat(lastCreatedRecette).isNotNull();
  }

  @When("I delete the recipe")
  public void iDeleteRecipe() {
    try {
      recetteRepository.deleteById(lastCreatedRecette.getId());
      lastException = null;
    } catch (Exception e) {
      lastException = e;
    }
  }

  @Then("the recipe is deleted successfully")
  public void recipeDeletedSuccessfully() {
    assertThat(lastException).isNull();
    assertThat(
      recetteRepository.findById(lastCreatedRecette.getId())
    ).isEmpty();
  }

  @When("I list all recipes")
  public void iListAllRecipes() {
    try {
      lastRecettesList = recetteRepository.findAll();
      lastException = null;
    } catch (Exception e) {
      lastException = e;
    }
  }

  @Then("I receive {int} recipes")
  public void iReceiveRecipes(int nombre) {
    assertThat(lastException).isNull();
    assertThat(lastRecettesList).isNotNull();
    assertThat(lastRecettesList).hasSize(nombre);
  }
}
