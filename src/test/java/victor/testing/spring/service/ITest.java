package victor.testing.spring.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.time.LocalDateTime.now;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static victor.testing.spring.listener.MessageListener.SUPPLIER_CREATED_EVENT;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)//cea mai proasta idee dar e Vineri
// o idee proasta pt ca adauga +10..60 secunde / @Test #4 nu o face
@ActiveProfiles("test")
@Import(ITest.KafkaTestConfig.class)
@EmbeddedKafka(topics = {SUPPLIER_CREATED_EVENT, PRODUCT_CREATED_TOPIC})
@SpringBootTest // porneste app spring in memoria JUnit
//@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = BEFORE_TEST_METHOD) #3
@Transactional // daca pui in teste, face rollback la sfarsitul fiecarui test 💖 #1
public  abstract class ITest {
  @RegisterExtension
  static WireMockExtension wireMockServer = WireMockExtension.newInstance()
      .options(options().port(9999)) // TODO sa fie dynamicPort sa nu te calci pe porturi cu alte procese
      .build();

  @Autowired
  protected ProductCreatedEventTestListener testListener;

  @TestConfiguration // adaug la @Configuration pentru a nu fi incarcat in productie
  public static class KafkaTestConfig {
    @Bean
    public ProductCreatedEventTestListener productCreatedEventTestListener() {
      return new ProductCreatedEventTestListener();
    }
  }

  @Slf4j
  public static class ProductCreatedEventTestListener {
    private LinkedBlockingQueue<ConsumerRecord<String, ProductCreatedEvent>> receivedRecords = new LinkedBlockingQueue<>();

    @PostConstruct
    void init() {
      log.info("Test listener initialized");
    }
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
      LocalDateTime deadline = now().plus(timeout);
      while (true) {
        Duration timeLeft = Duration.between(now(), deadline);
        var record = receivedRecords.poll(timeLeft.toMillis(), MILLISECONDS);
        if (record == null) {
          throw new TimeoutException("Timeout while waiting for message");
        }
        if (messageSelector.test(record)) {
          log.info("Received message matched test: {}", record);
          return record;
        }else {
          log.info("Discarding message not matching test: {}", record);
        }
      }
    }
  }
}
