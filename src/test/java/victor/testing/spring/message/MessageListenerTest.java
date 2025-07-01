package victor.testing.spring.message;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

public class MessageListenerTest extends IntegrationTest {
  @Autowired
  SqsTemplate sqsTemplate;
  @Autowired
  SupplierRepo supplierRepo;
  @Value("${supplier.created.event}")
  String queueName;

  @Test
  void supplierIsCreated() throws Exception {
    sqsTemplate.send(queueName, "supplier");

    Awaitility.await().timeout(ofSeconds(5L)).untilAsserted(() -> {
      assertThat(supplierRepo.existsByName("supplier")).isTrue();
      assertThat(supplierRepo.count()).isEqualTo(1L);
    });

    // sending the same name again should not insert a new supplier
    sqsTemplate.send(queueName, "supplier");

    Awaitility.await().timeout(ofSeconds(5L)).untilAsserted(() -> {
      assertThat(supplierRepo.existsByName("supplier")).isTrue();
      assertThat(supplierRepo.count()).isEqualTo(1L);
    });

    // TODO 1 assert the supplier is inserted correctly
    // TODO 2 assert the supplier is NOT inserted if it already exists
  }
}
