package victor.testing.spring.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

class MessageListener0Test extends IntegrationTest {
  @Autowired
  KafkaTemplate kafkaTemplate;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void supplierIsCreated() throws Exception {
    kafkaTemplate.send(MessageListener.SUPPLIER_CREATED_EVENT, "supplier");

    // TODO 1 assert the supplier is inserted correctly
  }
  // TODO 2 +1 @Test to assert the supplier is NOT inserted if it already exists

}

// HARD-CORE POINTS:
// - private message broker (on machine/in docker)
// - pending messages from previous @Test
// - multiple Spring apps in context test context cache competing to consume