package victor.testing.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import victor.testing.spring.service.ProductCreatedEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@Slf4j
public class ProductCreatedEventTestListener {
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
