package victor.testing.spring.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.infra.SafetyClient.SafetyResponse;
import victor.testing.spring.repo.SupplierRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ActiveProfiles("embedded-kafka")
@EmbeddedKafka(topics = "supplier-created-event")
public class ListenerInfraTest extends IntegrationTest {
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @SpyBean // imbraca beanul real cu un mock Mockito
  MessageListener messageListener;
  @Autowired
  SupplierRepo supplierRepo;

  @Test
  void listenerIsCalled_whenMessageIsSentViaKafka() {
    kafkaTemplate.send("supplier-created-event", "supplier");

    // nu merge pt ca efectul asteptat e produs pe ALT THREAD
    // decat cel de unit test. si inca nu e gata efectul
//    assertThat(supplierRepo.findByName("supplier").get()).isNotNull();

    verify(messageListener, timeout(1000))
        .onMessage("supplier");

  }

}
