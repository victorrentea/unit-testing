package victor.testing.spring.listener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.listener.MessageListener.SUPPLIER_CREATED_EVENT;

// blackbox test of the listener (no mocking)
public class ListenerBlackITest extends IntegrationTest {
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
  void supplierIsCreated_polling() throws ExecutionException, InterruptedException {
    // trigger message
    kafkaTemplate.send(SUPPLIER_CREATED_EVENT, "supplier");

    // avoid Thread.sleep(millis); > for how long?
    Awaitility.await() // state of the art in polling
        .pollInterval(ofMillis(50)) // try every 5ms
        .timeout(ofSeconds(5)) // fail after 1s
        .untilAsserted(() ->
            assertThat(supplierRepo.findByName("supplier"))
                .describedAs("Supplier was inserted")
                .isNotEmpty());

  }

}
