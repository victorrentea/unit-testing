package victor.testing.spring.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.BaseDatabaseTest;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.repo.SupplierRepo;

import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("embedded-kafka")
@EmbeddedKafka(topics = "${incoming.topic}")
public class KafkaListenerTest extends BaseDatabaseTest {
  @SpyBean
  KafkaListener kafkaListener;
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @Value("${incoming.topic}")
  String topic;

  @Test
  void messageReceived_poll() {
    doNothing().when(kafkaListener).onMessage(any());

    kafkaTemplate.send(topic, "halo");

    Awaitility.await().timeout(ofSeconds(2)).untilAsserted(() ->
        verify(kafkaListener)
            .onMessage(argThat(record -> "halo".equals(record.value()))));
  }

  @Test
  void messageReceived_mockitoBlock() {
    doNothing().when(kafkaListener).onMessage(any());

    kafkaTemplate.send(topic, "halo");

    verify(kafkaListener, timeout(2000))
        .onMessage(argThat(record -> "halo".equals(record.value())));
  }

  @Autowired
  SupplierRepo supplierRepo;

  @Test
  @Transactional // works due to direct method call
  void messageHandlerLogic() {
    // ugly library internal class TODO abstract away
    kafkaListener.onMessage(new ConsumerRecord<>("", 0, 0, "", "supplier name"));

    assertThat(supplierRepo.findAll())
        .hasSize(1)
        .first()
        .extracting(Supplier::getName)
        .isEqualTo("supplier name");
  }
}
