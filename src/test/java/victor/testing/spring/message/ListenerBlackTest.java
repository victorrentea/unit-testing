package victor.testing.spring.message;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

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
  void supplierIsCreatedOnMessageReceived() throws InterruptedException {
    kafkaTemplate.send("supplier-created-event", "supplier");

    Thread.sleep(150); // works on my machine
    assertThat(supplierRepo.findByName("supplier"))
        .describedAs("Supplier was inserted")
        .isNotNull();
  }

}
