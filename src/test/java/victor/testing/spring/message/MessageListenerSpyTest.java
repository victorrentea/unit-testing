package victor.testing.spring.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.product.repo.SupplierRepo;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("embedded-kafka")
@EmbeddedKafka(topics = "supplier-created-event")
public class MessageListenerSpyTest extends IntegrationTest {
  private static final String SUPPLIER_NAME = "supplier";
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  SupplierRepo supplierRepo;
  @SpyBean
  MessageListener listener;

  @Test // for infrastructure
  void listenerIsCalled_whenMessageReceived() {
    doNothing().when(listener).onMessage(any()); // stop the actual processing

    kafkaTemplate.send("supplier-created-event", SUPPLIER_NAME);

    verify(listener, timeout(2000)).onMessage(SUPPLIER_NAME);
  }

  @Test // for message processing logic, imagine 10 tests
  void listenerLogic() {
    listener.onMessage(SUPPLIER_NAME);

    assertThat(supplierRepo.findByName(SUPPLIER_NAME)).isNotNull();
  }
}
