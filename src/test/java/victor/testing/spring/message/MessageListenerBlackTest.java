package victor.testing.spring.message;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.product.repo.SupplierRepo;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("embedded-kafka")
@EmbeddedKafka(topics = "supplier-created-event")
public class MessageListenerBlackTest extends IntegrationTest {
  private static final String SUPPLIER_NAME = "supplier";
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  SupplierRepo supplierRepo;

  @BeforeEach
  @AfterEach
  final void cleanDB() { // manual cleanup required as tested code COMMITs an INSERT
    supplierRepo.deleteAll();
  }

  @Test
  void supplierIsCreatedOnMessageReceived() throws InterruptedException {
    kafkaTemplate.send("supplier-created-event", SUPPLIER_NAME);

    Thread.sleep(100);
    assertThat(supplierRepo.findByName(SUPPLIER_NAME)).isNotNull();
//    Awaitility.await().timeout(ofSeconds(2)) // throw after 2 seconds
//        .pollInterval(ofMillis(100)) // retry every 100 millis
//        .untilAsserted(() -> assertThat(supplierRepo.findByName(SUPPLIER_NAME)).isNotNull());
  }
  // TODO Awaitility
  // TODO separate infra test

}
