package victor.testing.spring.listener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.listener.MessageListener.SUPPLIER_CREATED_EVENT;

// blackbox test of the listener (no mocking)
public class ListenerBlackNaiveITest extends IntegrationTest {
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
  void test() throws InterruptedException {
    kafkaTemplate.send(SUPPLIER_CREATED_EVENT, null);
    kafkaTemplate.send(SUPPLIER_CREATED_EVENT, null); //daca sare bug in listener
    // testul asta nu vede exceptia = #hate pentru codul async

//    Thread.sleep(250); // FLAKY TEST = "mai ruleaza o data ca poate trece"
//    assertThat(supplierRepo.findByName("supplier"))
//        .describedAs("Supplier was inserted")
//        .isNotEmpty();

    Awaitility.await() // state of the art in polling
        .pollInterval(ofMillis(5)) // try every 5ms
        .timeout(ofSeconds(1)) // fail after 1s
        .untilAsserted(() ->
            assertThat(supplierRepo.findByName("supplier"))
                .describedAs("Supplier was inserted")
                .isNotEmpty());


  }

}
