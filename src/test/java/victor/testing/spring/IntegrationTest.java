package victor.testing.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.StartupInfoLogger;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.cache.ContextCache;
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import victor.testing.spring.listener.MessageListener;
import victor.testing.spring.service.ProductCreatedEvent;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.listener.MessageListener.SUPPLIER_CREATED_EVENT;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@SpringBootTest
@ActiveProfiles("test")

@Import(IntegrationTest.KafkaTestConfig.class)
@EmbeddedKafka(topics = {SUPPLIER_CREATED_EVENT, PRODUCT_CREATED_TOPIC})
@AutoConfigureMockMvc // avoids swapping threads in Tomcat's tread pool so @Transactional still works
@AutoConfigureWireMock(port = 0) // Start a HTTP server on a random port serving canned JSONs
public class IntegrationTest {
  @Autowired
  protected ProductCreatedEventTestListener productCreatedEventTestListener;
@SpyBean // the real bean is decorated by a mock proxy that can record invocations
protected MessageListener messageListener;

  @TestConfiguration
  public static class KafkaTestConfig {
    @Bean
    public ProductCreatedEventTestListener productCreatedEventTestListener() {
      return new ProductCreatedEventTestListener();
    }
  }

  @Slf4j
  public static class ProductCreatedEventTestListener {
    private LinkedBlockingQueue<Tuple2<ConsumerRecord<String, ProductCreatedEvent>,String>> receivedRecords = new LinkedBlockingQueue<>();

    @KafkaListener(topics = PRODUCT_CREATED_TOPIC)
    void receive(ConsumerRecord<String, ProductCreatedEvent> consumerRecord/*,
                 @Header("tenant-id") String tenantId*/) {
      log.info("received payload='{}'", consumerRecord.toString());
      receivedRecords.add(Tuples.of(consumerRecord, "tenantId"));
    }

    // drop all until I find one that matches the selector or timeout occurs
    // Challenge: uniquely identify the message expected (eg: use an UUID)
    public ConsumerRecord<String, ProductCreatedEvent> blockingReceive(
        Duration timeout,
        Predicate<Tuple2<ConsumerRecord<String, ProductCreatedEvent>, String>> selector) throws ExecutionException, InterruptedException {
      LocalDateTime deadline = LocalDateTime.now().plus(timeout);
      while (true) {
        Duration timeLeft = Duration.between(LocalDateTime.now(), deadline);
        var record = receivedRecords.poll(timeLeft.toMillis(), TimeUnit.MILLISECONDS);
        if (record == null) {
          throw new RuntimeException("Timeout while waiting for message");
        }
        log.info("Got message : " + record);
        if (selector.test(record)) {
          return record.getT1();
        }
      }
    }
  }

  @AfterAll
  public static void checkHowManyTimesSpringStarted() {
    // DO NOT CHANGE THIS CONSNTA> CALL ME 0720021524785 or you get fired
    int EXPECTED_NUMBER_OF_TIMES_SPRING_STARTS = 2;
    assertThat(StartupInfoLogger.startupTimeLogs)
        .describedAs("Number of times spring started (performance)")
        .hasSizeLessThanOrEqualTo(EXPECTED_NUMBER_OF_TIMES_SPRING_STARTS);
  }

}
