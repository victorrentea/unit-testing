package victor.testing.spring.service;

import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@AutoConfigureWireMock(port = 9999) // start up a HTTP server listening to port 9999
// serving responses as read from /src/test/resources/mappings
//@TestPropertySource(properties = "safety.service.url.base=http://localhost:9999")
@ActiveProfiles("wiremock")
@Retention(RetentionPolicy.RUNTIME)
public @interface WithWiremock {
}
