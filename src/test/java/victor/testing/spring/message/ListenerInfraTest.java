package victor.testing.spring.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.IntegrationTest;

import static org.mockito.Mockito.*;


public class ListenerInfraTest extends IntegrationTest {
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  private long t1;

  @Test
  void listenerIsCalled_whenMessageIsSentViaKafka() {
    long t0 = System.currentTimeMillis();
    doAnswer(invocation -> {
      t1 = System.currentTimeMillis();
      return invocation.callRealMethod();
    }).when(messageListener).onMessage(anyString());
    kafkaTemplate.send("supplier-created-event", "supplier");

    verify(messageListener, timeout(1000))
        .onMessage("supplier");
    System.out.println("Took " + (t1 - t0) + "ms to deliber the message to the listener");
  }

}
