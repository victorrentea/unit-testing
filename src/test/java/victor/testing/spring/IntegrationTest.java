package victor.testing.spring;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.IntegrationTest.ChestiiSpringInPlusPtTeste;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.message.MessageListener;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.tools.TestcontainersUtils;

import java.util.ArrayDeque;
import java.util.concurrent.ArrayBlockingQueue;

@Slf4j // todo make protected
@SpringBootTest
@Import(ChestiiSpringInPlusPtTeste.class)
@ActiveProfiles({"test","embedded-kafka"})
@EmbeddedKafka(topics = {"supplier-created-event", "pt-bi-cu-dragoste"})
public abstract class IntegrationTest {
  @Autowired
  protected SupplierRepo supplierRepo;
  @Autowired
  protected ProductRepo productRepo;
  @SpyBean
  protected MessageListener messageListener;

  @AfterEach
  @BeforeEach
  public void tearDown() {
    // in loc de asta, poti intr-un script
// + 10 altele

    productRepo.deleteAll();
    supplierRepo.deleteAll();
        supplierRepo.save(new Supplier().setCode("S"));
//    userRepo.deleteAll; // #sieu x 20
  }

  protected static ArrayDeque<String> receivedMessages = new ArrayDeque<>(100);
  @TestConfiguration
  public static class ChestiiSpringInPlusPtTeste {
    @Bean
    public TestListener testListener() {
      return new TestListener();
    }
    public static class TestListener {
      @SneakyThrows
      @KafkaListener(topics = "pt-bi-cu-dragoste")
      public void onMessage(String message) {
        log.info("Received BI message: " + message);
        receivedMessages.add(message);
      }
    }
  }
}
