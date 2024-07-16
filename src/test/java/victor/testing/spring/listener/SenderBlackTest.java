package victor.testing.spring.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductCreatedEvent;
import victor.testing.spring.service.ProductService;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.when;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@SpringBootTest
@ActiveProfiles({"db-mem", "embedded-kafka"})
@EmbeddedKafka(topics = PRODUCT_CREATED_TOPIC)
public class SenderBlackTest {
  @MockBean
  SafetyApiAdapter mockSafetyApiAdapter;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;
  @Autowired
  KafkaTestConsumer testConsumer;

  @BeforeEach
  public void resetConsumer() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
    testConsumer.reset();
  }

  @Test
  void createSendsMessage() throws InterruptedException, ExecutionException {
    supplierRepo.save(new Supplier().setCode("S"));
    when(mockSafetyApiAdapter.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safe", "S", HOME);

    // WHEN
    Long productId = productService.createProduct(dto);

    // ⚠️FLAKY: can fail for timeout exceeded
    ConsumerRecord<String, ProductCreatedEvent> message = testConsumer.blockingReceive(ofSeconds(5)); // blocking
    assertThat(message.value().productId()).isEqualTo(productId);
    assertThat(message.value().observedAt()).isCloseTo(now(), byLessThan(5, SECONDS));
  }

  @TestConfiguration
  public static class KafkaTestConfig {
    @Bean
    KafkaTestConsumer testConsumer() {
      return new KafkaTestConsumer();
    }
  }


  @Slf4j
  private static class KafkaTestConsumer {
    private CompletableFuture<ConsumerRecord<String, ProductCreatedEvent>> receivedRecord = new CompletableFuture<>();

    @KafkaListener(topics = PRODUCT_CREATED_TOPIC)
    void receive(ConsumerRecord<String, ProductCreatedEvent> consumerRecord) {
      log.info("received payload='{}'", consumerRecord.toString());
      receivedRecord.complete(consumerRecord);
    }

    public ConsumerRecord<String, ProductCreatedEvent> blockingReceive(Duration timeout) throws ExecutionException, InterruptedException {
      return receivedRecord.orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS).get();
    }

    public void reset() {
      receivedRecord = new CompletableFuture<>();
    }
  }

}
