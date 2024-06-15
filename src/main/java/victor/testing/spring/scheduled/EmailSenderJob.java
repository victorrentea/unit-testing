package victor.testing.spring.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailSenderJob {
  private final EmailToSendRepo repo;
  @Value("${email.sender.url}")
  private final String emailServerUrl;

  @Scheduled(cron = "${email.sender.cron}")
  @Transactional
  public void sendAllPendingEmails() {
    List<EmailToSend> emailsToSend = repo.findAllByStatus(EmailToSend.Status.TO_SEND);
    log.info("Email job START");
    for (EmailToSend emailToSend : emailsToSend) {
      try {
        sendToServer(emailToSend);
        emailToSend.setStatus(EmailToSend.Status.SUCCESS);
        log.info("Email job END OK");
      } catch (Exception e) {
        log.error("Send email call failed: " + e, e);
        emailToSend.setStatus(EmailToSend.Status.ERROR);
        log.info("Email job END KO");
      }
      repo.save(emailToSend);
    }
  }

  private void sendToServer(EmailToSend emailToSend) {
    new RestTemplate().postForObject(emailServerUrl + "/send-email?to={}&subject={}",
        emailToSend.getBody(),
        Void.class,
        emailToSend.getRecipientEmail(),
        emailToSend.getSubject());
  }
}
