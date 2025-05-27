package victor.testing.tools;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.annotation.Retention;
import java.lang.reflect.Method;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME) // stops javac from removing it at compilation
@DisplayNameGeneration(PrettyTestNames.PrettyTestNamesGenerator.class)
public @interface PrettyTestNames {
  class PrettyTestNamesGenerator extends DisplayNameGenerator.Standard {
    public String generateDisplayNameForClass(Class<?> testClass) {
      return replaceCapitals(super.generateDisplayNameForClass(testClass));
    }

    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
      return replaceCapitals(super.generateDisplayNameForNestedClass(nestedClass));
    }

    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
      return replaceCapitals(testMethod.getName());
    }

    private String replaceCapitals(String name) {
      return name.replaceAll("([A-Z])", " $1")
          .replaceAll("_", " > ")
          .replaceAll("\\s+", " ")
          .toLowerCase();
    }
  }
}
