package victor.testing;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

public class AllTestsMustBeAnnotatedWithDirtiesContextRule {

//  @Test
  void allTestClassesShouldBeAnnotatedWithDirtiesContext() {
    JavaClasses importedClasses = new ClassFileImporter()
        .importPackages("victor.testing");

    ArchRule rule = ArchRuleDefinition.noClasses()
        .that().haveSimpleNameEndingWith("Test")
        .should().beAnnotatedWith(DirtiesContext.class);

    rule.check(importedClasses);
  }
}