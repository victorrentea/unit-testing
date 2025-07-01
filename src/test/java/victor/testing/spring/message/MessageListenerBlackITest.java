package victor.testing.spring.message;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.message.MessageListener.SUPPLIER_CREATED_EVENT;

// blackbox test of the listener (no mocking)
public class MessageListenerBlackITest extends IntegrationTest {
  @Autowired
  SqsTemplate sqsTemplate;
  @Autowired
  SupplierRepo supplierRepo;
  @Value("${supplier.created.event}")
  String queueName;

  @BeforeEach
  @AfterEach
  final void cleanDB() { // manual cleanup required as tested code COMMITs an INSERT
    supplierRepo.deleteAll();
  }

  @Test
  void supplierIsCreated_polling() throws Exception {
    sqsTemplate.send(queueName, "supplier");

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
