package victor.testing.tools;

import lombok.SneakyThrows;
import org.mockito.internal.configuration.InjectingAnnotationEngine;

import java.lang.reflect.Field;
import java.util.Set;

public class MockitoExtension extends InjectingAnnotationEngine {
   @SneakyThrows
   @Override
   protected void onInjection(Object testClassInstance, Class<?> clazz, Set<Field> mockDependentFields, Set<Object> mocks) {
      System.err.println("HALO!!");
      for (Field declaredField : testClassInstance.getClass().getDeclaredFields()) {
         declaredField.setAccessible(true);
         System.out.println("In " + declaredField.getName() + " we have: " + declaredField.get(testClassInstance));
      }
      super.onInjection(testClassInstance, clazz, mockDependentFields, mocks);
   }
}
