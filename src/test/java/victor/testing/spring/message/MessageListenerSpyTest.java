package victor.testing.spring.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.product.repo.SupplierRepo;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("embedded-kafka")
@EmbeddedKafka(topics = "${input.topic}") // or a Kafka in a Testcontainer docker
public class MessageListenerSpyTest extends IntegrationTest {
  private static final String SUPPLIER_NAME = "supplier";
  @SpyBean // spy / partial mock ????
  MessageListener listener;
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @Value("${input.topic}")
  String topic;
  @Autowired
  SupplierRepo supplierRepo;

  @Test // for infrastructure & configuration
  void listenerMethodIsCalled() {
    doNothing().when(listener).onMessage(any()); // stop the actual processing

    // when
    kafkaTemplate.send(topic, SUPPLIER_NAME);

    verify(listener, timeout(2000)) // blocks test thread for max 2 s
        .onMessage(argThat(record -> SUPPLIER_NAME.equals(record.value())));
  }

  @Test // for message processing logic
  @Transactional // works since tested code stays in the same thread
  void listenerMethodLogic() {
    // ugly library class, todo can we abstract it away?
    var record = new ConsumerRecord<>("", 0, 0, "", SUPPLIER_NAME);

    listener.onMessage(record);

    assertThat(supplierRepo.findByName(SUPPLIER_NAME)).isNotNull();
  }
}
