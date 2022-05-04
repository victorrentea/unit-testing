package victor.testing.tools;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mockito.internal.configuration.InjectingAnnotationEngine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Set;

// also see src/test/resources/mockito-extensions
@Slf4j
public class InjectRealObjectsMockitoExtension extends InjectingAnnotationEngine {
   /** Includes a real object in the injected dependencies by Mockito */
   @Retention(RetentionPolicy.RUNTIME)
   @Target(ElementType.FIELD)
   public @interface Real {
   }
   @SneakyThrows
   @Override
   protected void onInjection(Object testClassInstance, Class<?> clazz, Set<Field> mockDependentFields, Set<Object> mocks) {
      for (Field declaredField : testClassInstance.getClass().getDeclaredFields()) {
         declaredField.setAccessible(true);
         if (!declaredField.isAnnotationPresent(Real.class)) {
            continue;
         }
         Object fieldValue = declaredField.get(testClassInstance);
         if (fieldValue == null) {
            log.warn("@Real fields should be manually initialized to be used by @InjectMocks");
            continue;
         }
         log.debug("Found @Real dependency : " + fieldValue);
         mocks.add(fieldValue);
      }
      super.onInjection(testClassInstance, clazz, mockDependentFields, mocks);
   }
}
