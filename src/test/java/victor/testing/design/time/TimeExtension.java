package victor.testing.design.time;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

/**
 * Example usage: add this as a field of your test class
 * {@code @RegisterExtension TimeExtension timeExtension = new TimeExtension("2019-09-29");}
 */
public class TimeExtension implements InvocationInterceptor {
  private LocalDate fixedDate;

  public TimeExtension(LocalDate fixedDate) {
    this.fixedDate = fixedDate;
  }

  public TimeExtension(String fixedDateIsoStr) {
    this.fixedDate = LocalDate.parse(fixedDateIsoStr);
  }

  public void setCurrentDate(LocalDate fixedDate) {
    this.fixedDate = fixedDate;
  }

  @Override
  public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
    try (var mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) { // other methods=untouched
      mocked.when(LocalDate::now).thenAnswer(call -> fixedDate); // -> allows change of date during @Test

      invocation.proceed();
    }
  }
}