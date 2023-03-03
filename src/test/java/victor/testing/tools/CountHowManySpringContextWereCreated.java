package victor.testing.tools;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.cache.ContextCache;
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate;

import java.lang.reflect.Field;

import static java.lang.System.currentTimeMillis;

public class CountHowManySpringContextWereCreated implements TestExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(CountHowManySpringContextWereCreated.class);
    private long t0;

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        t0 = currentTimeMillis();
    }

    @SneakyThrows
    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        try {
            Field f = DefaultCacheAwareContextLoaderDelegate.class.getDeclaredField("defaultContextCache");
            f.setAccessible(true);
            ContextCache cache = (ContextCache) f.get(null);
            long t1 = currentTimeMillis();
            log.info("🏁 Executed tests in {} seconds. Spring created {} contexts",(t1-t0)/1000f, cache.size());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.warn("Listener failed: " + e);
        }
    }
}
