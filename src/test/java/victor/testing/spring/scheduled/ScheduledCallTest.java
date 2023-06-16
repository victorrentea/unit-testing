package victor.testing.spring.scheduled;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.BaseDatabaseTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static victor.testing.spring.scheduled.EmailToSend.Status.SUCCESS;

@ActiveProfiles("wiremock")
@AutoConfigureWireMock(port = 0) // random port
@TestPropertySource(properties = "email.sender.cron=-") // cron="-" means 'DISABLE'
public class JobCallTest extends BaseDatabaseTest {
  @Autowired
  EmailToSendRepo repo;
  @Autowired
  WireMockServer wireMock;
  @Autowired
  EmailSenderJob job;

  @Test
  void directCallOfScheduledMethod() {
    wireMock.stubFor(post(urlMatching("/send-email.*"))
        .willReturn(aResponse()));
    EmailToSend email = new EmailToSend()
        .setRecipientEmail("to@example.com")
        .setSubject("Sub")
        .setBody("Bod");
    Long id = repo.save(email).getId();

    job.sendAllPendingEmails(); // direct call of the @Scheduled method

    // no need to wait for another thread to complete
    assertThat(repo.findById(id).orElseThrow().getStatus()).isEqualTo(SUCCESS);
  }

}
