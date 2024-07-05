package victor.testing.spring.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.message.MessageListener.Message1;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


public class ListenerInfraMarcelTest extends IntegrationTest {
  @Autowired
  KafkaTemplate<String, Message1> kafkaTemplate;

  @Test
  void ajungInMetoda() {
    kafkaTemplate.send("marcel", new Message1("a"));

    verify(messageListener, timeout(1000))
        .onMarcel(new Message1("a"));
  }

}
