package victor.testing.design.fixturecreep.strictstubs;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.mockito.Mockito.mockitoSession;

/**
 * starts the mockito session STRICT_STUBS before instantiating the test class,
 * so you can use .mock() directly in the field declaration (not in @BeforeEach)
 */
public class StrictMockitoExtension implements TestInstanceFactory, AfterEachCallback {
  private final static Namespace MOCKITO = create("org.mockito.victor");

  public Object createTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext extensionContext)
          throws TestInstantiationException {
    MockitoSession mockitoSession = mockitoSession().strictness(Strictness.STRICT_STUBS).startMocking();
    extensionContext.getStore(MOCKITO).put("SESSION", mockitoSession);
    return doCreateTestInstance(factoryContext);
  }

  @Override
  public void afterEach(ExtensionContext context) {
    MockitoSession mockitoSession = context.getStore(MOCKITO).get("SESSION", MockitoSession.class);
    mockitoSession.finishMocking();
  }

  @NotNull
  private static Object doCreateTestInstance(TestInstanceFactoryContext factoryContext) {
    try {
      Optional<Object> outerInstance = factoryContext.getOuterInstance();
      if (outerInstance.isPresent()) {
        return factoryContext.getTestClass().getConstructor(outerInstance.get().getClass()).newInstance(outerInstance.get());
      } else {
        return factoryContext.getTestClass().getConstructor().newInstance();
      }
    } catch (Exception e) {
      throw new TestInstantiationException(e.getMessage(), e);
    }
  }
}
