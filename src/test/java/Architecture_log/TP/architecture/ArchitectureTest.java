package Architecture_log.TP.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
  packages = "Architecture_log.TP",
  importOptions = { ImportOption.DoNotIncludeTests.class }
)
class ArchitectureTest {

  @ArchTest
  static final ArchRule command_controllers = classes()
    .that()
    .haveSimpleNameEndingWith("CommandController")
    .should()
    .resideInAPackage("..commands.api..");

  @ArchTest
  static final ArchRule query_controllers = classes()
    .that()
    .haveSimpleNameEndingWith("QueryController")
    .should()
    .resideInAPackage("..queries.api..");

  @ArchTest
  static final ArchRule command_services = classes()
    .that()
    .haveSimpleNameEndingWith("CommandService")
    .should()
    .resideInAPackage("..commands.service..");

  @ArchTest
  static final ArchRule query_services = classes()
    .that()
    .haveSimpleNameEndingWith("QueryService")
    .should()
    .resideInAPackage("..queries.service..");

  @ArchTest
  static final ArchRule repositories = classes()
    .that()
    .haveSimpleNameEndingWith("Repository")
    .should()
    .resideInAPackage("..common.repository..");

  @ArchTest
  static final ArchRule entities = classes()
    .that()
    .resideInAPackage("..common.entity..")
    .should()
    .haveSimpleName("Recette")
    .orShould()
    .haveSimpleName("Ingredient");

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

  @ArchTest
  static final ArchRule repositories_only_accessed_by_services = classes()
    .that()
    .resideInAPackage("..common.repository..")
    .should()
    .onlyBeAccessed()
    .byAnyPackage("..commands.service..", "..queries.service..");
}
