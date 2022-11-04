package victor.testing.tools;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.VerificationException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.github.tomakehurst.wiremock.verification.NearMiss;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import reactor.core.publisher.Mono;
import reactor.test.publisher.PublisherProbe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProbeExtension implements InvocationInterceptor {
   private final Map<PublisherProbe<?>, StackTraceElement> probes = new HashMap<>();

   public <T> Mono<T> probeOnce(Mono<T> mono) {
      PublisherProbe<T> pp = PublisherProbe.of(mono);
      StackTraceElement place = new RuntimeException().getStackTrace()[1];
      probes.put(pp, place);
      return pp.mono();
   }
   @Override
   public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
      try {
         invocation.proceed();
      } finally {
         for (PublisherProbe<?> probe : probes.keySet()) {
            StackTraceElement place = probes.get(probe);
            assertThat(probe.subscribeCount())
                    .describedAs("Mono probed at " + place + " was subscribed multiple times")
                    .isEqualTo(1);
         }
      }
   }
}
