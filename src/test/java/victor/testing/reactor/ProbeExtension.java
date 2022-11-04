package victor.testing.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import reactor.core.publisher.Mono;
import reactor.test.publisher.PublisherProbe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProbeExtension implements InvocationInterceptor {
    private final Map<PublisherProbe<?>, StackTraceElement> probes = new HashMap<>();

    public <T> Mono<T> once(Mono<T> mono) {
        PublisherProbe<T> probe = PublisherProbe.of(mono);
        StackTraceElement place = new RuntimeException().getStackTrace()[1];
        probes.put(probe, place);
        return probe.mono();
    }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        invocation.proceed();

        for (PublisherProbe<?> probe : probes.keySet()) {
            StackTraceElement place = probes.get(probe);
            assertThat(probe.subscribeCount())
                    .describedAs("Mono probed at " + place + " was subscribed multiple times")
                    .isEqualTo(1);
        }
    }
}
