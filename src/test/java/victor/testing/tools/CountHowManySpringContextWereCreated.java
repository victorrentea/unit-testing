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

public class CountHowManySpringContextWereCreated implements TestExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(CountHowManySpringContextWereCreated.class);
    @SneakyThrows
    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        try {
            Field f = DefaultCacheAwareContextLoaderDelegate.class.getDeclaredField("defaultContextCache");
            f.setAccessible(true);
            ContextCache cache = (ContextCache) f.get(null);
            log.info("For the tests ran above, Spring created {} contexts" , cache.size());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.warn("Listener failed: " + e);
        }
    }
}
