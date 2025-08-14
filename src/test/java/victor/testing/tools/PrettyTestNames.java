package victor.testing.tools;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;
import java.util.List;


// use via @DisplayNameGeneration on a test class
public class PrettyTestNames extends DisplayNameGenerator.ReplaceUnderscores {
    public PrettyTestNames() {
    }

    public String generateDisplayNameForClass(Class<?> testClass) {
        return replaceCapitals(super.generateDisplayNameForClass(testClass));
    }

    @Override
    public String generateDisplayNameForNestedClass(List<Class<?>> enclosingInstanceTypes, Class<?> nestedClass) {
        return replaceCapitals(super.generateDisplayNameForNestedClass(enclosingInstanceTypes, nestedClass));
    }

    @Override
    public String generateDisplayNameForMethod(List<Class<?>> enclosingInstanceTypes, Class<?> testClass, Method testMethod) {
        return replaceCapitals(super.generateDisplayNameForMethod(enclosingInstanceTypes, testClass, testMethod));
    }

    private String replaceCapitals(String name) {
        return name.replaceAll("([A-Z])", " $1")
                .replaceAll("_"," > ")
                .replaceAll("\\s+"," ")
                .toLowerCase();
    }
}