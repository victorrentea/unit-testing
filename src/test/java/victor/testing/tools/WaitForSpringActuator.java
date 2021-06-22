package victor.testing.tools;

import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public class WaitForSpringActuator implements BeforeAllCallback {

    private final String baseUrl;

    public WaitForSpringActuator(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        log.info("Waiting for {} to come UP...", baseUrl);
        Awaitility.await().pollInterval(1,TimeUnit.SECONDS).atMost(1, TimeUnit.MINUTES).until(() -> isApplicationUp(baseUrl + "/actuator"));
        log.info("{} is UP", baseUrl);
    }

    public static boolean isApplicationUp(String urlString) {
        try {
            log.debug("Ping {}", urlString);

            ResponseEntity<String> response = new RestTemplate().getForEntity(urlString, String.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception ex) {
            // ignore
            return false;
        }
    }
}
