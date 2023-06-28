package victor.testing.spring.scheduled;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestListener;
import com.github.tomakehurst.wiremock.http.Response;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.BaseDatabaseTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static victor.testing.spring.scheduled.EmailToSend.Status.SUCCESS;

@ActiveProfiles("wiremock")
@TestPropertySource(properties = "email.sender.cron=*/1 * * * * *") // every second
@AutoConfigureWireMock(port = 0) // random port
public class ScheduledAwaitTest extends BaseDatabaseTest {
  @Autowired
  EmailToSendRepo repo;
  @Autowired
  WireMockServer wireMock;

  @Test
  void insertAndWaitForScheduledToProcessItem() {
    wireMock.stubFor(post(urlMatching("/send-email.*"))
        .willReturn(aResponse()));
    EmailToSend email = new EmailToSend()
        .setRecipientEmail("to@example.com")
        .setSubject("Sub")
        .setBody("Bod");

    Long id = repo.save(email).getId(); // insert the testData that will trigger the

    Awaitility.await().timeout(ofSeconds(2))
        .untilAsserted(() ->
            assertThat(repo.findById(id).orElseThrow().getStatus()).isEqualTo(SUCCESS));
  }

}
