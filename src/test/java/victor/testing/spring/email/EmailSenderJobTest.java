package victor.testing.spring.email;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.BaseDatabaseTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static victor.testing.spring.email.EmailToSend.Status.SUCCESS;

@ActiveProfiles("wiremock")
@AutoConfigureWireMock(port = 0) // random port
public class EmailSenderJobTest extends BaseDatabaseTest {
  @Autowired
  EmailToSendRepo repo;
  @Autowired
  WireMockServer wireMock;

  EmailToSend email = new EmailToSend()
      .setRecipientEmail("to@example.com")
      .setSubject("Sub")
      .setBody("Bod");

  @Test
  void explore() {
    wireMock.stubFor(post(urlMatching("/send-email.*"))
        .willReturn(aResponse()
//            .withFixedDelay(10) // may cause a race bug in tests
        ));
    Long id = repo.save(email).getId();

    Awaitility.await().timeout(ofSeconds(2)).untilAsserted(() ->
        assertThat(repo.findById(id).orElseThrow().getStatus()).isEqualTo(SUCCESS));
  }

}
