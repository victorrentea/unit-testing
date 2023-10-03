package victor.testing.spring.tools;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;


// use via @DisplayNameGeneration(HumanReadableTestNames.class) on a testclass
public class HumanReadableTestNames extends DisplayNameGenerator.Standard {
    public HumanReadableTestNames() {
    }

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
        name = name.replaceAll("([A-Z])", " $1")
                .replaceAll("_"," > ")
                .replaceAll("\\s+"," ")
                .toLowerCase();
        return name;
    }
}