package victor.testing.spring.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.LocalDateTime;

/**
 * Prints time elapsed since start after each class run. Only works if tests are not forked (single threaded).
 */
@Slf4j
public class MeasureTotalTestTimeListener implements TestExecutionListener {

   @Retention(RetentionPolicy.RUNTIME)
   @TestExecutionListeners(listeners = MeasureTotalTestTimeListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
   public @interface MeasureRealTime {
   }

   private static Long t0;
   @Override
   public void beforeTestClass(TestContext testContext) throws Exception {
      if (t0 == null) {
         log.debug("First test class run started at " + LocalDateTime.now());
         t0 = System.currentTimeMillis();
      }
   }

   @Override
   public void afterTestClass(TestContext testContext) throws Exception {
      log.info("Elapsed since first test class run start = {} ms", System.currentTimeMillis() - t0);
   }
}
