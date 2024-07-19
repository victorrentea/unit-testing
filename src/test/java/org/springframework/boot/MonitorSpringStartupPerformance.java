package org.springframework.boot;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.cache.ContextCache;
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static java.util.Collections.synchronizedList;
import static org.assertj.core.api.Assertions.assertThat;


// To install, add the qualified name of this class to
// src/test/resources/META-INF/services/org.junit.platform.launcher.TestExecutionListener
public class MonitorSpringStartupPerformance implements TestExecutionListener {
  // NEW STUFF
  public static final List<StartupTimeLog> startupTimeLogs = synchronizedList(new ArrayList<>());
  private static final Logger log = LoggerFactory.getLogger(MonitorSpringStartupPerformance.class);

  private static long junitStartTime = currentTimeMillis();

  public static void reportStartupTime(String testClassName, Duration timeTakenToStartup) {
    startupTimeLogs.add(new StartupTimeLog(testClassName, timeTakenToStartup));
  }

  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    // planA: class shadowing
    int numberOfSpringContextsStarted = startupTimeLogs.size();
    Duration totalSpringStartupTime = startupTimeLogs.stream()
        .map(StartupTimeLog::timeTakenToStartup)
        .reduce(Duration.ZERO, Duration::plus);

    log.info("🏁🏁🏁 All tests took {} seconds, and started {} Spring contexts in {} seconds",
        (currentTimeMillis() - junitStartTime) / 1000f,
        numberOfSpringContextsStarted,
        totalSpringStartupTime.toSeconds());

    // planB: reflect private field in Spring Context Cache
//    int numberOfSpringContextsStarted = extractSpringContextCacheMissCount();
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

  // NEW STUFF
  public record StartupTimeLog(String applicationName, Duration timeTakenToStartup) {
    @Override
    public String toString() {
      return applicationName + " started in " + timeTakenToStartup.toSeconds() + " seconds";
    }
  }
}
