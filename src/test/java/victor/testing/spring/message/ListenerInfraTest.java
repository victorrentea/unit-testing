package victor.testing.spring.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.IntegrationTest;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


public class ListenerInfraTest extends IntegrationTest {
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;

  @Test
  void listenerIsCalled_whenMessageIsSentViaKafka() {
    kafkaTemplate.send("supplier-created-event", "supplier");

    verify(messageListener, timeout(1000))
        .onMessage("supplier");
  }

}
