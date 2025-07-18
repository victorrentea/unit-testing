package victor.testing.spring.scheduled;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.IntegrationTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static victor.testing.spring.scheduled.EmailToSend.Status.SUCCESS;

@Disabled("prints weird training errors in test output.enable on demand")
@TestPropertySource(properties = {
    "email.sender.cron=*/1 * * * * *", // = every second
    "scheduling.enabled=true"
})
public class ScheduledAwaitTest extends IntegrationTest {
  public static final EmailToSend EMAIL = new EmailToSend()
      .setRecipientEmail("to@example.com")
      .setSubject("Sub")
      .setBody("Bod");
  @Autowired
  EmailToSendRepo repo;
  @Autowired
  WireMockServer wireMock;

  @Test
  void insertAndWaitForScheduledToProcessItem() {
    wireMock.stubFor(post(urlMatching("/send-email.*")).willReturn(aResponse()));

    Long id = repo.save(EMAIL).getId(); // insert the data that will trigger the @Scheduled

    Awaitility.await().timeout(ofSeconds(5)) //Poke at this number
        .untilAsserted(() ->
            assertThat(repo.findById(id).orElseThrow().getStatus()).isEqualTo(SUCCESS));
  }

}
