package victor.testing.spring.listener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles({"db-mem", "embedded-kafka"})
@EmbeddedKafka(topics = {"supplier-created-event", "supplier-created-error"})
public class ListenerInfraTest {
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @SpyBean // the real bean is decorated by a mock proxy that can record invocations
  MessageListener messageListener;

  @Test
  void listenerIsCalled_whenMessageIsSentViaKafka() {
    kafkaTemplate.send("supplier-created-event", "supplier");

    verify(messageListener, timeout(1000))
        .onMessage("supplier");
  }
}
