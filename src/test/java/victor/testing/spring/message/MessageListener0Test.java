package victor.testing.spring.message;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.mockito.Mockito.timeout;

class MessageListener0Test extends IntegrationTest {
  @Autowired
  KafkaTemplate kafkaTemplate;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void supplierIsCreated() throws Exception {
    kafkaTemplate.send(MessageListener.SUPPLIER_CREATED_EVENT, "supplier");

    Mockito.verify(bizLogic, timeout(100)).bizLogic("supplier");
  }
}

// HARD-CORE POINTS:
// - private message broker (on machine/in docker)
// - pending messages from previous @Test
// - multiple Spring apps in context test context cache competing to consume