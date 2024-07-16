package victor.testing.spring.scheduled;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static victor.testing.spring.scheduled.EmailToSend.Status.SUCCESS;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0) // random port
public class ScheduledCallTest {
  public static final EmailToSend EMAIL = new EmailToSend()
      .setRecipientEmail("to@example.com")
      .setSubject("Sub")
      .setBody("Bod");
  @Autowired
  EmailToSendRepo repo;
  @Autowired
  WireMockServer wireMock;
  @Autowired
  EmailSenderJob job;

  @Test
  void directCallOfScheduledMethod() {
    wireMock.stubFor(post(urlMatching("/send-email.*")).willReturn(aResponse()));
    Long id = repo.save(EMAIL).getId();

    job.sendAllPendingEmails(); // direct call of the @Scheduled method

    // no need to wait for another thread to complete
    assertThat(repo.findById(id).orElseThrow().getStatus()).isEqualTo(SUCCESS);
  }

}
