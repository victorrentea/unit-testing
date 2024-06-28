package victor.testing.spring.message;

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
import victor.testing.spring.repo.SupplierRepo;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("embedded-kafka")
@EmbeddedKafka(topics = "supplier-created-event")
public class ListenerBlackTest extends IntegrationTest {
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
    kafkaTemplate.send("supplier-created-event", "supplier");

    Thread.sleep(150); // works on my machine™️, but my colleague requires 200ms
    assertThat(supplierRepo.findByName("supplier"))
        .describedAs("Supplier was inserted")
        .isNotEmpty();
  }

  @Test
  void supplierIsCreated_polling() {
    kafkaTemplate.send("supplier-created-event", "supplier");

    // Awaitility is a library that allows you to express expectations in a more concise way.
    // modul recomandat de a scrie verificari pe efect asincrone cu POLLING

    // tot as vei putea testa si @Scheduled
    Awaitility.await()
        .timeout(Duration.ofSeconds(1))
        .pollInterval(Duration.ofMillis(10))
        .untilAsserted(() ->
            assertThat(supplierRepo.findByName("supplier"))
                .describedAs("Supplier was inserted")
                .isNotEmpty());
  }

}
