package victor.testing.spring.listener;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import victor.testing.spring.IntegrationTest;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static victor.testing.spring.listener.MessageListener.SUPPLIER_CREATED_EVENT;

//@Disabled("slows down tests due to @SpyBean")
public class ListenerInfraITest extends IntegrationTest {
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @Test
  void listenerIsCalled_whenMessageIsSentViaKafka() {
    kafkaTemplate.send(SUPPLIER_CREATED_EVENT, "supplier");
//    ProducerConfig.INTERCEPTOR_CLASSES_CONFIG
    verify(messageListener, timeout(1000))
        .onMessage(argThat(message ->
            message.equals("supplier")));
//        .onMessage(argThat(message ->
//            message.getHeader("tenantId").equals("XX"));
  }
}
