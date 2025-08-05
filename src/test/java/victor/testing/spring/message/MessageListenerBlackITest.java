package victor.testing.spring.message;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

// blackbox test of the listener (no mocking)
class MessageListenerBlackITest extends IntegrationTest {
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
  void supplierIsCreated_polling() throws Exception {
    kafkaTemplate.send(SUPPLIER_CREATED_EVENT, "supplier");

    Awaitility.await() // state of the art in polling
        .pollInterval(ofMillis(50)) // try every 50ms
        .timeout(ofSeconds(1)) // fail after 1s
        .untilAsserted(() ->
            assertThat(supplierRepo.findByName("supplier"))
                .describedAs("Supplier was inserted")
                .isNotEmpty());
    // TODO check that the row WAS NOT inserted?
    //   > you will have to sleep(x) for a static amount of time
    //   (you can't poll anymore)
  }

}
