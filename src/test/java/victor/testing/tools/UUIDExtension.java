package victor.testing.tools;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

/**
 * Example usage: add this as a field of your test class
 * {@code @RegisterExtension TimeExtension timeExtension = new TimeExtension("2019-09-29");}
 */
public class UUIDExtension implements InvocationInterceptor {
  private List<UUID> nextToReturn;

  public UUIDExtension(UUID... uuidsToReturnToTestedcode) {
    nextToReturn = new ArrayList<>(List.of(uuidsToReturnToTestedcode));
  }
  public UUIDExtension(String... uuidsToReturnToTestedcode) {
    this(Stream.of(uuidsToReturnToTestedcode).map(UUID::fromString).toArray(UUID[]::new));
  }

  public void add(UUID uuid) {
    nextToReturn.add(uuid);
  }
  public void add(String uuid) {
    nextToReturn.add(UUID.fromString(uuid));
  }

  @Override
  public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
    try (var mocked = mockStatic(UUID.class, CALLS_REAL_METHODS)) { // other methods=untouched
      mocked.when(UUID::randomUUID).thenAnswer(call -> {
        UUID next = nextToReturn.get(0);
        if (nextToReturn.size()>=2)
          nextToReturn.remove(0);
        return next;
      }); // -> allows change of date during @Test

      invocation.proceed();
    }
  }
}