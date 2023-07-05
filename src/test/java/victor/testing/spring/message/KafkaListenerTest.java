package victor.testing.spring.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.BaseDatabaseTest;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.repo.SupplierRepo;

import java.util.concurrent.CountDownLatch;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("embedded-kafka")
@EmbeddedKafka(topics = "${incoming.topic}")
public class KafkaListenerTest extends BaseDatabaseTest {
  public static final String SUPPLIER_NAME = "halo";
  @SpyBean
  MyKafkaListener kafkaListener;
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @Value("${incoming.topic}")
  String topic;

  // FULL BLACK BOX TEST din coada-n baza
  @Test
  void messageReceived_pollingCaSaInseratRandu_FULL_TEST() { // incerc eu repetat
    kafkaTemplate.send(topic, SUPPLIER_NAME);

    Awaitility.await()
        .timeout(ofSeconds(2))
        .untilAsserted(() ->
            assertThat(supplierRepo.findAll()).hasSize(1)
                .first()
                .extracting(Supplier::getName)
                .isEqualTo(SUPPLIER_NAME)
        );
  }

  //------- Separate MQ integration vs Logic -> buna cand ai logica complexa
  // Stage1: doar ca a mers bine Kafka/Rabbit,

  // Alternative:
  // - CountDownLatch  //a la 2000-2010
  // - awaitility.untilAsserted(Mockito.verify)
  @Test
  void messageReceived_methodCalled_mockitoBlock() { // preferabil
    doNothing().when(kafkaListener).onMessage(any());

    kafkaTemplate.send(topic, SUPPLIER_NAME);

    verify(kafkaListener, timeout(2000))
        .onMessage(argThat(record -> SUPPLIER_NAME.equals(record.value())));
  }

  // Stage2: logica efectiva din Listener
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  @Transactional
    // works due to direct method call
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
