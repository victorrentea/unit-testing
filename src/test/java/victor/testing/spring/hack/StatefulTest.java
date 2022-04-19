package victor.testing.spring.hack;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

@TestExecutionListeners(listeners = EnableReinjectInStatefulTestsListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
@ExtendWith(BeanDestroyerExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(classes = StatefulSingleton.class)
public class StatefulTest {
   @Autowired
   StatefulSingleton s;

   @Autowired
   private StatefulSingletonClient statefulSingletonClient;

   @Test
   void test1() {
      Assertions.assertThat(statefulSingletonClient.method()).isEqualTo(1);
   }
   @Test
   void test2() {
      Assertions.assertThat(statefulSingletonClient.method()).isEqualTo(1);
   }

   @Autowired
   private StatefulSingleton statefulSingleton;

   @Test
   void testa1() {
      Assertions.assertThat(statefulSingleton.getAndInc()).isEqualTo(1);
   }
   @Test
   void testa2() {
      Assertions.assertThat(statefulSingleton.getAndInc()).isEqualTo(1);
   }
}
