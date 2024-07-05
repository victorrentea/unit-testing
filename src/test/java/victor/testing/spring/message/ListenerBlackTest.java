package victor.testing.spring.message;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ListenerBlackTest extends IntegrationTest {
  public static final String SUPPLIER = "supplier" + UUID.randomUUID();
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
  @Disabled("flaky")
  void supplierIsCreated_sleep_flaky() throws InterruptedException {
    log.info("Sending message");
    kafkaTemplate.send("supplier-created-event", SUPPLIER);

    Thread.sleep(10); // works on my machine™️, but my colleague requires 200ms
    assertThat(supplierRepo.findByName(SUPPLIER).get())
        .describedAs("Supplier was inserted")
        .returns(SUPPLIER, Supplier::getName);
  }

  @Test
  void supplierIsCreated_polling() {
    kafkaTemplate.send("supplier-created-event", SUPPLIER);

    Awaitility.await()
        .pollInterval(Duration.ofMillis(10))
        .timeout(Duration.ofSeconds(1))
        .untilAsserted(() -> {
          try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(supplierRepo.findByName(SUPPLIER))
                .map(Supplier::getName)
                .describedAs("Supplier was inserted")
                .hasValue(SUPPLIER);
            softly.assertThat(receivedMessages.poll())
                .describedAs("Message was sent to BI")
                .isEqualTo("dragoste💖 " + SUPPLIER);
          }
        });
    // eroarea la acest test este in log DUPA AssertionFailure ( ca e multitjread)
  }

}
