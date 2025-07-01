package victor.testing.spring.message;

import io.awspring.cloud.sqs.operations.SqsTemplate;
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

    // TODO 1 assert the supplier is inserted correctly
    // TODO 2 assert the supplier is NOT inserted if it already exists
  }

}
