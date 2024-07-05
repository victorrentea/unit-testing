package victor.testing.spring.message;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import java.util.ArrayDeque;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
//@TestPropertySource(properties = "kafka.enabled=true")
public class ListenerLogicTest extends IntegrationTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  private MessageListener messageListener;

  @BeforeEach
  @AfterEach
  final void cleanDB() { // manual cleanup required as tested code COMMITs an INSERT
    supplierRepo.deleteAll();
  }

  @Test
  void listenerLogic() throws InterruptedException {
    // apel de metoda clasic
    String supplier = "supplier" + UUID.randomUUID();
    messageListener.onMessage(supplier);

    assertThat(supplierRepo.findByName(supplier))
        .describedAs("Supplier was inserted")
        .isNotNull();

  }



}
