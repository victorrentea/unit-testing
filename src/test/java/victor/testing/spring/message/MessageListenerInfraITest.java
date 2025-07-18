package victor.testing.spring.message;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import victor.testing.spring.IntegrationTest;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static victor.testing.spring.message.MessageListener.SUPPLIER_CREATED_EVENT;

@Disabled("slows down tests due to @SpyBean")
public class MessageListenerInfraITest extends IntegrationTest {
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @MockitoSpyBean // the real bean is decorated by a mock proxy that can record invocations
  MessageListener messageListener;

  @Test
  void listenerIsCalled_whenMessageIsSentViaKafka() {
    kafkaTemplate.send(SUPPLIER_CREATED_EVENT, "supplier");

    verify(messageListener, timeout(1000))
        .onMessage("supplier");
  }
}
