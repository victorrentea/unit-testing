package victor.testing.design.onion;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class OnionArchTest {
  private final JavaClasses allProjectClasses = new ClassFileImporter()
      .importPackages("victor.testing.design.onion");

  @Test
  public void domain_independent_of_infrastructure() {
    noClasses().that().resideInAPackage("..domain..")
        .should().dependOnClassesThat().resideInAPackage("..infra..")
        .check(allProjectClasses);
  }
}
