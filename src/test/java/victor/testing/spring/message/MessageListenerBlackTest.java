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
@EmbeddedKafka(topics = "${input.topic}")
public class MessageListenerBlackTest extends IntegrationTest {
  private static final String SUPPLIER_NAME = "supplier";
  @SpyBean
  MessageListener messageListener;
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @Value("${input.topic}")
  String topic;
  @Autowired
  SupplierRepo supplierRepo;

  @BeforeEach
  @AfterEach
  final void before() { // manual cleanup required as tested code COMMITs an INSERT
    supplierRepo.deleteAll();
  }

  @Test
  void messageProcessed() {
    kafkaTemplate.send(topic, SUPPLIER_NAME);

    Awaitility.await().timeout(ofSeconds(2)) // throw after 2 seconds
        .pollInterval(ofMillis(10)) // retry every 100 millis
        .untilAsserted(() -> assertThat(supplierRepo.findByName(SUPPLIER_NAME)).isNotNull());
  }

}
