package victor.testing.spring.message;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.BaseIntegrationTest;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductService;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.byLessThan;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;

@TestPropertySource(properties = {
    "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.consumer.group-id=test",
    "spring.kafka.consumer.auto-offset-reset=earliest" // avoid losing messages sent before listener connects
})
@EmbeddedKafka(topics = "product-created")
public class SenderBlackTest extends BaseIntegrationTest {
  @MockBean
  SafetyApiClient mockSafetyApiClient;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;
  @Autowired
  KafkaTestConsumer testConsumer;

  @BeforeEach
  public void resetListener() {
    testConsumer.resetLatch();
  }

  @Test
  void createSendsMessage() throws InterruptedException, ExecutionException {
    // GIVEN
    Long supplierId = supplierRepo.save(new Supplier().setCode("S")).getId();
    when(mockSafetyApiClient.isSafe("safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "safe", "S", HOME);

    // WHEN
    productService.createProduct(dto);

    // THEN
    assertThat(testConsumer.receive(Duration.ofSeconds(1)).value()).isEqualTo("NAME");
  }

  @TestConfiguration
  public static class KafkaTestConfig {
    @Bean
    KafkaTestConsumer testConsumer() {
      return new KafkaTestConsumer();
    }
  }


  @Slf4j
  public static class KafkaTestConsumer {
    private CompletableFuture<ConsumerRecord<String, String>> receivedRecord = new CompletableFuture<>();

    @KafkaListener(topics = ProductService.PRODUCT_CREATED_TOPIC)
    public void receive(ConsumerRecord<String, String> consumerRecord) {
      log.info("received payload='{}'", consumerRecord.toString());
      receivedRecord.complete(consumerRecord);
    }

    public ConsumerRecord<String, String> receive(Duration timeout) throws ExecutionException, InterruptedException {
      return receivedRecord.orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS).get();
    }

    public void resetLatch() {
      receivedRecord = new CompletableFuture<>();
    }
  }

}
