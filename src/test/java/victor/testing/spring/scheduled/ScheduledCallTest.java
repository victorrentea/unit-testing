package victor.testing.spring.scheduled;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.wiremock.spring.EnableWireMock;
import victor.testing.spring.IntegrationTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static victor.testing.spring.scheduled.EmailToSend.Status.SUCCESS;

@ActiveProfiles("test")
@EnableWireMock
public class ScheduledCallTest extends IntegrationTest {
  public static final EmailToSend EMAIL = new EmailToSend()
      .setRecipientEmail("to@example.com")
      .setSubject("Sub")
      .setBody("Bod");
  @Autowired
  EmailToSendRepo repo;
  @Autowired
  EmailSenderJob job;

  @Test
  void directCallOfScheduledMethod() {
    WireMock.stubFor(post(urlMatching("/send-email.*")).willReturn(aResponse()));
    Long id = repo.save(EMAIL).getId();

    job.sendAllPendingEmails(); // direct call of the @Scheduled method

    // no need to wait for another thread to complete
    assertThat(repo.findById(id).orElseThrow().getStatus()).isEqualTo(SUCCESS);
  }

}
