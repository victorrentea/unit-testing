package victor.testing.spring;

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

import static java.lang.Integer.MIN_VALUE;
import static java.lang.System.currentTimeMillis;


// Installation:
// - in src/test/resources/META-INF/spring.factories:
//    org.springframework.test.context.TestExecutionListener=victor.testing.spring.MonitorSpringTestStartupPerformance
// - in src/test/resources/META-INF/services/org.junit.platform.launcher.TestExecutionListener:
//    victor.testing.spring.MonitorSpringTestStartupPerformance

@Component // place this in the packages of your app under your @SpringBootApplication to be picked up by Spring
public class MonitorSpringTestStartupPerformance implements TestExecutionListener,
    org.springframework.test.context.TestExecutionListener, Ordered {
  private static final Logger log = LoggerFactory.getLogger(MonitorSpringTestStartupPerformance.class);

  private static long junitStartTime = currentTimeMillis();
  private static Long springStartTime;
  private static final AtomicLong totalSpringStartupTime = new AtomicLong(0);

  // Spring stuff -----------------------------

  @Override
  public void beforeTestClass(TestContext testContext) throws Exception {
    System.out.println();
    springStartTime = currentTimeMillis();
  }

  @Override
  public int getOrder() {
    return MIN_VALUE;
  }

  // JUnit stuff -----------------------------

  @EventListener(ApplicationStartedEvent.class)
  public void endInit() {
//    System.out.println("STARTED");
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
      int springContextsStarted = cache.getMissCount();
      log.info("🏁🏁🏁 All tests took {} seconds, and started {} Spring contexts in {} seconds",
          (currentTimeMillis() - junitStartTime) / 1000f,
          springContextsStarted,
          totalSpringStartupTime.get() / 1000f);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      log.warn("Listener failed: " + e);
    }
  }

  public void check() {
    System.out.println("HALO!");
  }
}
