package victor.testing.spring.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.listener.MessageListener.SUPPLIER_CREATED_ERROR;

@SpringBootTest
@ActiveProfiles({"db-mem", "embedded-kafka"})
@EmbeddedKafka(topics = {"supplier-created-event", "supplier-created-error"})
public class ListenerFullTest {
  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  KafkaTestConsumer testConsumer;

  @BeforeEach
  @AfterEach
  final void cleanDB() { // manual cleanup required as tested code COMMITs an INSERT
    supplierRepo.deleteAll();
  }

  @Test
  @Disabled("because it's flaky, fails randomly")
  void supplierIsCreated_sleep_flaky() throws InterruptedException {
    // trigger message
    kafkaTemplate.send("supplier-created-event", "supplier");

    Thread.sleep(70); // It works on my machine™️, said its author

    assertThat(supplierRepo.findByName("supplier"))
        .describedAs("Supplier was inserted")
        .isNotEmpty();
  }

  @Test
  void supplierIsCreated_polling() throws ExecutionException, InterruptedException {
    // trigger message
    kafkaTemplate.send("supplier-created-event", "supplier");

    Awaitility.await() // state of the art in polling
        .pollInterval(ofMillis(5)) // try every 5ms
        .timeout(ofSeconds(1)) // fail after 1s
        .untilAsserted(() ->
            assertThat(supplierRepo.findByName("supplier"))
                .describedAs("Supplier was inserted")
                .isNotEmpty());

  }
  @Test
  void supplierIsCreatedError_blockingReceive() throws ExecutionException, InterruptedException {
    supplierRepo.save(new Supplier().setName("supplier"));
    // trigger message
    kafkaTemplate.send("supplier-created-event", "supplier");

    ConsumerRecord<String, String> message = testConsumer.blockingReceive(ofSeconds(1)); // blocking
    assertThat(message.value()).isEqualTo("Supplier already exists: supplier");
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
    private CompletableFuture<ConsumerRecord<String, String>> receivedRecord = new CompletableFuture<>();

    @KafkaListener(topics = SUPPLIER_CREATED_ERROR)
    void receive(ConsumerRecord<String, String> consumerRecord) {
      log.info("received payload='{}'", consumerRecord.toString());
      receivedRecord.complete(consumerRecord);
    }

    public ConsumerRecord<String, String> blockingReceive(Duration timeout) throws ExecutionException, InterruptedException {
      return receivedRecord.orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS).get();
    }

    public void reset() {
      receivedRecord = new CompletableFuture<>();
    }
  }

}
