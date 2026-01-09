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
  static final ArchRule controllers = classes()
    .that()
    .haveSimpleNameEndingWith("Controller")
    .should()
    .resideInAPackage("..controller..");

  @ArchTest
  static final ArchRule services = classes()
    .that()
    .haveSimpleNameEndingWith("Service")
    .should()
    .resideInAPackage("..service..");

  @ArchTest
  static final ArchRule repositories = classes()
    .that()
    .haveSimpleNameEndingWith("Repository")
    .should()
    .resideInAPackage("..repository..");

  @ArchTest
  static final ArchRule controllers_should_only_depend_on_services = classes()
    .that()
    .resideInAPackage("..controller..")
    .should()
    .onlyDependOnClassesThat()
    .resideInAnyPackage(
      "java..",
      "org.springframework..",
      "..service..",
      "..entity.."
    );

  @ArchTest
  static final ArchRule services_should_not_depend_on_controllers = noClasses()
    .that()
    .resideInAPackage("..service..")
    .should()
    .dependOnClassesThat()
    .resideInAPackage("..controller..");

  @ArchTest
  static final ArchRule repositories_only_accessed_by_services = classes()
    .that()
    .resideInAPackage("..repository..")
    .should()
    .onlyBeAccessed()
    .byAnyPackage("..service..");
}
