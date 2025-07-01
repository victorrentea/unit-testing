package victor.testing.spring;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestComponent;
import victor.testing.spring.service.ProductCreatedEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@TestComponent
public class ProductCreatedEventTestListener {
  private LinkedBlockingQueue<ProductCreatedEvent> receivedRecords = new LinkedBlockingQueue<>();

  @SqsListener("${product.created.topic}")
  public void receive(ProductCreatedEvent record) {
    log.debug("Test listener received message: {}", record);
    receivedRecords.add(record);
  }

  public ProductCreatedEvent blockingReceive(Duration timeout) throws ExecutionException, InterruptedException, TimeoutException {
    LocalDateTime deadline = LocalDateTime.now().plus(timeout);
    while (true) {
      Duration timeLeft = Duration.between(LocalDateTime.now(), deadline);
      var record = receivedRecords.poll(timeLeft.toMillis(), MILLISECONDS);
      if (record == null) {
        throw new TimeoutException("Timeout while waiting for message");
      }
      return record;
    }
  }

  public void drain() {
    receivedRecords.clear();
  }
}