package victor.testing.tools;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.StartupInfoLogger;
import org.springframework.test.context.cache.ContextCache;
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.System.currentTimeMillis;
import static org.assertj.core.api.Assertions.assertThat;


// Installation:
// - in src/test/resources/META-INF/services/org.junit.platform.launcher.TestExecutionListener:
//    victor.testing.tools.MonitorSpringTestStartupPerformance

public class MonitorSpringTestStartupPerformance implements TestExecutionListener {
  private static final Logger log = LoggerFactory.getLogger(MonitorSpringTestStartupPerformance.class);
  public static final int EXPECTED_NUMBER_OF_TIMES_SPRING_STARTS = 1;

  private static long junitStartTime = currentTimeMillis();

  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    // planA
    int numberOfSpringContextsStarted = StartupInfoLogger.startupTimeLogs.size();
    Duration totalSpringStartupTime = StartupInfoLogger.startupTimeLogs.stream()
        .map(StartupInfoLogger.StartupTimeLog::timeTakenToStartup)
        .reduce(Duration.ZERO, Duration::plus);

    // planB
//    int numberOfSpringContextsStarted = extractSpringContextCacheMissCount();

    log.info("🏁🏁🏁 All tests took {} seconds, and started {} Spring contexts in {} seconds",
        (currentTimeMillis() - junitStartTime) / 1000f,
        numberOfSpringContextsStarted,
        totalSpringStartupTime.toSeconds());

    // deep hacking into Spring
    assertThat(StartupInfoLogger.startupTimeLogs)
        .describedAs("Number of times spring started")
        .hasSize(EXPECTED_NUMBER_OF_TIMES_SPRING_STARTS);

    // less hacking
//    assertThat(numberOfSpringContextsStarted).isEqualTo(EXPECTED_NUMBER_OF_TIMES_SPRING_STARTS);

  }

  private int extractSpringContextCacheMissCount() {
    try {
      Field f = DefaultCacheAwareContextLoaderDelegate.class.getDeclaredField("defaultContextCache");
      f.setAccessible(true);
      ContextCache cache = (ContextCache) f.get(null);
      return cache.getMissCount();
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      log.warn("Listener failed: " + e);
      return -1;
    }
  }
}
