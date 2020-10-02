package victor.testing.spring.tools;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.VerificationException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.github.tomakehurst.wiremock.verification.NearMiss;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@Slf4j
public class WireMockExtension extends WireMockServer implements InvocationInterceptor {

   private final boolean failOnUnmatchedRequests;

   public WireMockExtension(Options options) {
      this(options, true);
   }

   public WireMockExtension(Options options, boolean failOnUnmatchedRequests) {
      super(options);
      this.failOnUnmatchedRequests = failOnUnmatchedRequests;
   }

   public WireMockExtension(int port) {
      this(wireMockConfig().port(port));
   }

   public WireMockExtension(int port, Integer httpsPort) {
      this(wireMockConfig().port(port).httpsPort(httpsPort));
   }

   public WireMockExtension() {
      this(wireMockConfig());
   }

   @Override
   public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
      long t0 = System.currentTimeMillis();
      start();
      log.debug("Started WireMockServer in {} ms", System.currentTimeMillis() - t0);
      WireMock.configureFor("localhost", port());
      try {
         before();
         invocation.proceed();
         checkForUnmatchedRequests();
      } finally {
         after();
         stop();
      }
   }

   private void checkForUnmatchedRequests() {
      if (failOnUnmatchedRequests) {
         List<LoggedRequest> unmatchedRequests = findAllUnmatchedRequests();
         if (!unmatchedRequests.isEmpty()) {
            List<NearMiss> nearMisses = findNearMissesForAllUnmatchedRequests();
            if (nearMisses.isEmpty()) {
               throw VerificationException.forUnmatchedRequests(unmatchedRequests);
            } else {
               throw VerificationException.forUnmatchedNearMisses(nearMisses);
            }
         }
      }
   }

   protected void before() {
      // NOOP
   }

   protected void after() {
      // NOOP
   }
}
