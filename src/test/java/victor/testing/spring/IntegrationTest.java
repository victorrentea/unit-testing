package victor.testing.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.junit.jupiter.api.AfterAll;
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
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.listener.MessageListener;
import victor.testing.spring.service.ProductCreatedEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
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
  protected ProductCreatedEventTestListener testListener;
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
    private LinkedBlockingQueue<ConsumerRecord<String, ProductCreatedEvent>> receivedRecords = new LinkedBlockingQueue<>();

    @KafkaListener(topics = PRODUCT_CREATED_TOPIC)
    void receive(ConsumerRecord<String, ProductCreatedEvent> record) {
      log.debug("Test listener received message: {}", record);
      receivedRecords.add(record);
    }

    public ConsumerRecord<String, ProductCreatedEvent> blockingReceiveForHeader(
        String headerKey, String headerValue, Duration timeout) throws ExecutionException, InterruptedException, TimeoutException {

      return blockingReceive(record -> filterByHeader(headerKey, headerValue, record), timeout);
    }

    private boolean filterByHeader(String headerKey, String headerValue, ConsumerRecord<String, ProductCreatedEvent> record) {
      Header header = record.headers().lastHeader(headerKey);
      if (header == null) return false;
      return headerValue.equals(new String(header.value()));
    }

    // drop all until I find one that matches the selector or timeout occurs
    // Challenge: uniquely identify the message expected (eg: use an UUID)
    public ConsumerRecord<String, ProductCreatedEvent> blockingReceive(
        Predicate<ConsumerRecord<String, ProductCreatedEvent>> messageSelector, Duration timeout) throws ExecutionException, InterruptedException, TimeoutException {
      LocalDateTime deadline = LocalDateTime.now().plus(timeout);
      while (true) {
        Duration timeLeft = Duration.between(LocalDateTime.now(), deadline);
        var record = receivedRecords.poll(timeLeft.toMillis(), MILLISECONDS);
        if (record == null) {
          throw new TimeoutException("Timeout while waiting for message");
        }
        if (messageSelector.test(record)) {
          log.info("Received message matched test: {}", record);
          return record;
        } else {
          log.info("Discarding message not matching test: {}", record);
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
