package victor.testing.tools;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.cache.ContextCache;
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.System.currentTimeMillis;

@Component
public class MonitorSpringTestStartupPerformance implements TestExecutionListener,
        org.springframework.test.context.TestExecutionListener, Ordered {
  private static final Logger log = LoggerFactory.getLogger(MonitorSpringTestStartupPerformance.class);

  private static long junitStartTime = currentTimeMillis();
  private static Long springStartTime;
  private static final AtomicLong totalSpringStartupTime = new AtomicLong(0);

  // Spring stuff -----------------------------

  @Override
  public void beforeTestClass(TestContext testContext) throws Exception {
    springStartTime = currentTimeMillis();
  }

  @Override
  public int getOrder() {
    return MAX_VALUE;
  }

  // JUnit stuff -----------------------------

  @EventListener
  public void endInit(ApplicationStartedEvent event) {
    System.out.println("STARTED");
    if (springStartTime != null) {
      long t1 = currentTimeMillis();
      Long t0 = springStartTime;
      totalSpringStartupTime.addAndGet(t1 - t0);
      springStartTime = null;
    }
  }

  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    try {
      Field f = DefaultCacheAwareContextLoaderDelegate.class.getDeclaredField("defaultContextCache");
      f.setAccessible(true);
      ContextCache cache = (ContextCache) f.get(null);
      log.info("🏁🏁🏁 All tests took {} seconds, and used {} Spring contexts started in {} seconds",
              (currentTimeMillis() - junitStartTime) / 1000f,
              cache.getMissCount(),
              totalSpringStartupTime.get() / 1000f);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      log.warn("Listener failed: " + e);
    }
  }
}
