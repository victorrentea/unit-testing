package victor.testing.spring.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.BaseDatabaseTest;
import victor.testing.spring.product.repo.SupplierRepo;

import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@TestPropertySource(properties = {
    "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.consumer.auto-offset-reset=earliest" // avoid losing messages sent before listener connects
})
@EmbeddedKafka(topics = "${incoming.topic}")
public class KafkaListenerTest extends BaseDatabaseTest {
  @SpyBean
  MessageListener messageListener;
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @Value("${incoming.topic}")
  String topic;

  @Test
  void kafkaMessageIsReceived() throws InterruptedException, ExecutionException {
    doNothing().when(messageListener).onMessage(any());

    kafkaTemplate.send(topic, "halo");

    Awaitility.await().timeout(ofSeconds(2))
        .untilAsserted(() ->
            verify(messageListener).onMessage(argThat(record -> "halo".equals(record.value()))));
  }

  @Autowired
  SupplierRepo supplierRepo;

  @Test
  @Transactional
  void messageHandlerLogic() throws InterruptedException, ExecutionException {
    messageListener.onMessage(new ConsumerRecord<>("", 0, 0, "", "supplier name"));

    assertThat(supplierRepo.findAll()).hasSize(1);
    assertThat(supplierRepo.findAll().get(0).getName()).isEqualTo("supplier name");
  }
}
