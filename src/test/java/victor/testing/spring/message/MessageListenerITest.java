package victor.testing.spring.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.message.MessageListener.SUPPLIER_CREATED_EVENT;

public class MessageListenerITest extends IntegrationTest {
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  SupplierRepo supplierRepo;

  String SUPPLIER_NAME = "supplier" + System.currentTimeMillis();

  @Test
  void supplierIsCreated_polling() {
    kafkaTemplate.send(SUPPLIER_CREATED_EVENT, SUPPLIER_NAME);

    Awaitility.await() // state of the art in polling
        .pollInterval(ofMillis(5)) // try every 5ms
        .timeout(ofSeconds(1)) // fail after max 1s
        .untilAsserted(() -> assertThat(supplierRepo.findByName(SUPPLIER_NAME))
            .describedAs("Supplier was inserted")
            .isNotEmpty());

    // TODO what if you had to check that the row WAS NOT inserted?
    //   > you can't poll anymore: you will have to sleep(x) for an amount of time (how long?)
  }

}
