package victor.testing.spring.message;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageListenerTest extends IntegrationTest {
  @Autowired
  SqsTemplate sqsTemplate;
  @Autowired
  SupplierRepo supplierRepo;
  @Value("${supplier.created.event}")
  String queueName;

  @BeforeEach
  void setup() {
    supplierRepo.deleteAll();
  }

  // TODO 1 assert the supplier is inserted correctly
  // TODO 2 assert the supplier is NOT inserted if it already exists

  @Test
  void supplierIsCreated() throws Exception {

    String supplierName = "test-supplier";
    sqsTemplate.send(queueName, supplierName);
    Awaitility.await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
              assertThat(supplierRepo.existsByName(supplierName)).isTrue();
            });

  }
  @Test
  void supplierIsNotCreatedIfAlreadyExists() {
    String supplierName = "existing-supplier";
    supplierRepo.save(new Supplier().setName(supplierName));

    sqsTemplate.send(queueName, supplierName);

    Awaitility.await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
              long count = supplierRepo.findAll().stream()
                      .filter(s -> s.getName().equals(supplierName))
                      .count();
              assertThat(count).isEqualTo(1);
            });
  }
}
