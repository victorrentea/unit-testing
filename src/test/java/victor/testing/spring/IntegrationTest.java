package victor.testing.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.NestedTestConfiguration;
import org.springframework.test.context.NestedTestConfiguration.EnclosingConfiguration;
import victor.testing.spring.service.ProductCreatedEvent;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@SpringBootTest
@Import(IntegrationTest.KafkaTestConfig.class)
@ActiveProfiles("test")
@EmbeddedKafka(topics = {
    "supplier-created-event",
    "supplier-created-error",
    PRODUCT_CREATED_TOPIC
})
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0) // Start a HTTP server on a random port serving canned JSONs
public class IntegrationTest {
  @Autowired
  protected ProductCreatedEventTestListener productCreatedEventTestListener;

  @BeforeEach
  void setup() {
    productCreatedEventTestListener.reset();
  }

  @TestConfiguration
  public static class KafkaTestConfig {
    @Bean
    public ProductCreatedEventTestListener testConsumer() {
      return new ProductCreatedEventTestListener();
    }
  }

  @Slf4j
  public static class ProductCreatedEventTestListener {
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
