package victor.testing.tools;

import lombok.SneakyThrows;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.springframework.test.context.cache.ContextCache;
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate;

import java.lang.reflect.Field;

public class CountHowManySpringContextWereCreated implements TestExecutionListener {
    @SneakyThrows
    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        Field f = DefaultCacheAwareContextLoaderDelegate.class.getDeclaredField("defaultContextCache");
        f.setAccessible(true);
        ContextCache cache = (ContextCache) f.get(null);
        System.out.println("Cate contexte oare? " + cache.size());
    }
}
