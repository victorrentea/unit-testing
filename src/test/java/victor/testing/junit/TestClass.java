package victor.testing.junit;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.junit.TimeTracked.Extension;
import victor.testing.tools.CaptureSystemOutput;
import victor.testing.tools.CaptureSystemOutput.OutputCapture;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static org.assertj.core.api.Assertions.assertThat;


@AcceptanceTest
public class TestClass {
  SomeHeavyResource resource;
  @Test
  @CaptureSystemOutput
  void test1(OutputCapture outputCapture) {
    darkAndEvilLegacyTestThatITriedToUnitTestToNotBreakingWhileRefactoring();
    assertThat(outputCapture.toString()).contains("Fast");
    resource.click();
  }

  private static void darkAndEvilLegacyTestThatITriedToUnitTestToNotBreakingWhileRefactoring() {
    System.out.println("Fast");
  }

  @Test
  void test2() throws InterruptedException {
    System.out.println("Slow");
    Thread.sleep(100);
  }
}
@AcceptanceTest
class T2 {
  SomeHeavyResource resource;
  @Test
  void experiment() {
    System.out.println("Fast");
    resource.click();
  }
}

@TimeTracked
//@SpringBootTest@ActiveProfiles
@ExtendWith(MockitoExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {METHOD, TYPE})
@interface AcceptanceTest {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {METHOD, TYPE})
@ExtendWith(Extension.class)
@interface TimeTracked {
  class Extension implements InvocationInterceptor, BeforeEachCallback{

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
      long t0 = System.currentTimeMillis();
      invocation.proceed();
      long t1 = System.currentTimeMillis();
      System.out.println( invocationContext.getExecutable().getName() + " took " + (t1 - t0) + " ms");
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
      Store store = context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL);
      SomeHeavyResource resource = store.getOrComputeIfAbsent("r", key -> new SomeHeavyResource(), SomeHeavyResource.class);
      injectResource(context, resource);
    }

    private static void injectResource(ExtensionContext context, SomeHeavyResource resource) throws IllegalAccessException {
      Object testClassInstance = context.getTestInstance().orElseThrow();
      for (Field f : testClassInstance.getClass().getDeclaredFields()) {
        if (f.getType().equals(SomeHeavyResource.class)) {
          f.setAccessible(true);
          f.set(testClassInstance, resource);
        }
      }
    }
  }
}

class SomeHeavyResource { //selenium, wiremock, testcontainer
  @SneakyThrows
  public SomeHeavyResource() {
    System.out.println("Creating expensive");
    Thread.sleep(500);
  }

  public void click() {
    System.out.println("Clicking");
  }
}
