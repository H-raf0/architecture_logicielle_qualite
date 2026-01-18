package Architecture_log.TP.bdd;

import static org.assertj.core.api.Assertions.*;

import Architecture_log.TP.TpArchitectureApplication;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
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
  private Exception lastException;

  // Context steps
  @Given("que l'application est démarrée")
  public void applicationEstDemarree() {
    assertThat(recetteRepository).isNotNull();
  }

  @Given("que la base de données est vide")
  public void baseDonneesVide() {
    recetteRepository.deleteAll();
  }

  // Action steps
  @When("je crée une recette avec le nom {string}")
  public void creRecette(String nom) {
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
  @Then("la recette est créée avec succès")
  public void recetteCreeeSucces() {
    assertThat(lastException).isNull();
    assertThat(lastCreatedRecette).isNotNull();
  }

  @Then("la recette a un identifiant")
  public void recetteAUnId() {
    assertThat(lastCreatedRecette).isNotNull();
    assertThat(lastCreatedRecette.getId()).isNotNull();
  }
}
