package victor.testing.design.time;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.mockito.Mockito.mockStatic;

// TO use this, add to your test class:
// @RegisterExtension TimeExtension timeExtension = new TimeExtension("2019-09-29");
public class TimeExtension implements InvocationInterceptor {
	private final LocalDate fixed;

  public TimeExtension(LocalDate fixed) {
    this.fixed = fixed;
  }
  public TimeExtension(String fixed) {
    this.fixed = LocalDate.parse(fixed);
  }

  @Override
	public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		try (MockedStatic<LocalDate> mock = mockStatic(LocalDate.class)) {
			mock.when(LocalDate::now).thenReturn(fixed);
			invocation.proceed();
		}
	}
}