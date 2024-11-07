package victor.testing.junit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import victor.testing.junit.TimerExtension.TimeTracked;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

//@TimeTracked
@Two
public class TestClass {
  @Test
  void test1() {
    System.out.println("Fast");
  }

  @Test
  void test2() throws InterruptedException {
    System.out.println("Slow");
    Thread.sleep(1000);
  }
}

@Retention(RetentionPolicy.RUNTIME)
@TimeTracked
@interface Two {}

class TimerExtension implements InvocationInterceptor {
  @Retention(RetentionPolicy.RUNTIME)
  @ExtendWith(TimerExtension.class)
  @Target({ElementType.METHOD, ElementType.TYPE})
  @interface TimeTracked {
  }

  @Override
  public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
    long t0 = System.currentTimeMillis();
    invocation.proceed();
    long t1 = System.currentTimeMillis();
    System.out.println("Method " + invocationContext.getExecutable().getName() + " took " + (t1 - t0) + " ms");
  }
}
