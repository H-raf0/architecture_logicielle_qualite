package Architecture_log.TP.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Tests de respect des règles d'architecture du projet.
 *
 * Valide que:
 * - Les CommandControllers sont dans le package commands.api
 * - Les QueryControllers sont dans le package queries.api
 * - Les CommandServices sont dans le package commands.service
 * - Les QueryServices sont dans le package queries.service
 * - Les Repositories sont dans le package common.repository
 * - Les dépendances entre couches respectent le pattern CQRS
 *
 * Erreur: Si une classe ne respecte pas sa règle, le test échoue.
 *
 * @see https://www.archunit.org/
 * @see <a href="https://en.wikipedia.org/wiki/Command_and_query_responsibility_segregation">CQRS pattern</a>
 */
@AnalyzeClasses(
  packages = "Architecture_log.TP",
  importOptions = { ImportOption.DoNotIncludeTests.class }
)
class ArchitectureTest {

  /**
   * Règle: Tous les CommandControllers doivent être dans le package commands.api
   */
  @ArchTest
  static final ArchRule command_controllers = classes()
    .that()
    .haveSimpleNameEndingWith("CommandController")
    .should()
    .resideInAPackage("..commands.api..");

  /**
   * Règle: Tous les QueryControllers doivent être dans le package queries.api
   */
  @ArchTest
  static final ArchRule query_controllers = classes()
    .that()
    .haveSimpleNameEndingWith("QueryController")
    .should()
    .resideInAPackage("..queries.api..");

  /**
   * Règle: Tous les CommandServices doivent être dans le package commands.service
   */
  @ArchTest
  static final ArchRule command_services = classes()
    .that()
    .haveSimpleNameEndingWith("CommandService")
    .should()
    .resideInAPackage("..commands.service..");

  /**
   * Règle: Tous les QueryServices doivent être dans le package queries.service
   */
  @ArchTest
  static final ArchRule query_services = classes()
    .that()
    .haveSimpleNameEndingWith("QueryService")
    .should()
    .resideInAPackage("..queries.service..");

  /**
   * Règle: Tous les Repositories doivent être dans le package common.repository
   */
  @ArchTest
  static final ArchRule repositories = classes()
    .that()
    .haveSimpleNameEndingWith("Repository")
    .should()
    .resideInAPackage("..common.repository..");

  /**
   * Règle: Les Entities valides doivent être Recette et Ingredient
   */
  @ArchTest
  static final ArchRule entities = classes()
    .that()
    .resideInAPackage("..common.entity..")
    .should()
    .haveSimpleName("Recette")
    .orShould()
    .haveSimpleName("Ingredient");

  /**
   * Règle: CommandControllers ne dépendent que des CommandServices, DTOs et Entities
   *
   * Garantit le respect de la séparation des couches dans le pattern CQRS.
   */
  @ArchTest
  static final ArchRule command_controllers_should_only_depend_on_command_services =
    classes()
      .that()
      .resideInAPackage("..commands.api..")
      .should()
      .onlyDependOnClassesThat()
      .resideInAnyPackage(
        "java..",
        "org.springframework..",
        "..commands.service..",
        "..commands.dto..",
        "..common.entity.."
      )
      .orShould()
      .haveSimpleName("CreateRecetteDTO")
      .orShould()
      .haveSimpleName("UpdateRecetteDTO")
      .orShould()
      .haveSimpleName("CreateIngredientDTO")
      .orShould()
      .haveSimpleName("UpdateIngredientDTO");

  /**
   * Règle: QueryControllers ne dépendent que des QueryServices et DTOs
   *
   * Garantit que les lectures ne passent que par la couche query.
   */
  @ArchTest
  static final ArchRule query_controllers_should_only_depend_on_query_services =
    classes()
      .that()
      .resideInAPackage("..queries.api..")
      .should()
      .onlyDependOnClassesThat()
      .resideInAnyPackage(
        "java..",
        "org.springframework..",
        "..queries.service..",
        "..queries.dto.."
      )
      .orShould()
      .haveSimpleName("RecetteDTO")
      .orShould()
      .haveSimpleName("IngredientDTO");

  /**
   * Règle: Les Repositories ne sont accessibles que par les Services
   *
   * Garantit que seuls les services peuvent accéder à la persistence.
   */
  @ArchTest
  static final ArchRule repositories_only_accessed_by_services = classes()
    .that()
    .resideInAPackage("..common.repository..")
    .should()
    .onlyBeAccessed()
    .byAnyPackage("..commands.service..", "..queries.service..");
}
