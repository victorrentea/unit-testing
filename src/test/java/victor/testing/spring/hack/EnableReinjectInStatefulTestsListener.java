package victor.testing.spring.hack;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

public class EnableReinjectInStatefulTestsListener extends AbstractTestExecutionListener {
   private static final int ORDER = new DependencyInjectionTestExecutionListener().getOrder() - 1;

   @Override
   public int getOrder() {
      return ORDER;
   }

   @Override
   public void beforeTestMethod(TestContext testContext) {
      // XXX: This is not optimal when the test lifecycle is per-class. Seems like there is no
      // nice way to get the test lifecycle.
      testContext.setAttribute(
          DependencyInjectionTestExecutionListener.REINJECT_DEPENDENCIES_ATTRIBUTE, Boolean.TRUE);
   }

}
