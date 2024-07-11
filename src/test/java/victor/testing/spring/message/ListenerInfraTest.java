package victor.testing.spring.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.BaseIntegrationTest;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ActiveProfiles("embedded-kafka")
@EmbeddedKafka(topics = "supplier-created-event")
public class ListenerInfraTest extends BaseIntegrationTest {
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @SpyBean
  MessageListener messageListener;

  @Test
  void listenerIsCalled_whenMessageIsSentViaKafka() {
    kafkaTemplate.send("supplier-created-event", "supplier");

    verify(messageListener, timeout(1000))
        .onMessage("supplier");
  }

}
