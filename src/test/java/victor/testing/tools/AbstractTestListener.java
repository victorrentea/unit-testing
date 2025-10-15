package victor.testing.tools;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AbstractTestListener<T> {
  protected final Logger log = LoggerFactory.getLogger(getClass());
  protected LinkedBlockingQueue<ConsumerRecord<String, T>> receivedRecords = new LinkedBlockingQueue<>();

  protected void receive(ConsumerRecord<String, T> record) {
    log.debug("Test listener received message: {}", record);
    receivedRecords.add(record);
  }

  public ConsumerRecord<String, T> blockingReceiveForHeader(
      String headerKey, String headerValue, Duration timeout) throws TimeoutException, InterruptedException {
    return blockingReceive(record -> filterByHeader(headerKey, headerValue, record), timeout);
  }

  private boolean filterByHeader(String headerKey, String headerValue, ConsumerRecord<String, T> record) {
    Header header = record.headers().lastHeader(headerKey);
    if (header == null) return false;
    return headerValue.equals(new String(header.value()));
  }

  public void drain(Duration forDuration) throws InterruptedException {
    LocalDateTime deadline = LocalDateTime.now().plus(forDuration);
    List<ConsumerRecord<String, T>> discardedMessages = new ArrayList<>();
    while (LocalDateTime.now().isBefore(deadline)) {
      Duration timeLeft = Duration.between(LocalDateTime.now(), deadline);
      tryBlockingReceive(r -> true, timeLeft)
          .ifPresent(discardedMessages::add);
    }
    log.debug("Drained {} messages: \n{}",
        discardedMessages.size(),
        discardedMessages.stream().map(Objects::toString).collect(Collectors.joining("\n"))
    );
  }

  public ConsumerRecord<String, T> blockingReceive(Duration timeout) throws TimeoutException, InterruptedException {
    return blockingReceive(r -> true, timeout);
  }

  // drop all until I find one that matches the selector or timeout occurs
  public ConsumerRecord<String, T> blockingReceive(
      Predicate<ConsumerRecord<String, T>> messageSelector,
      Duration timeout) throws TimeoutException, InterruptedException {
    return tryBlockingReceive(messageSelector, timeout)
        .orElseThrow(() -> new TimeoutException("Timeout while waiting for message"));
  }

  public Optional<ConsumerRecord<String, T>> tryBlockingReceive(
      Predicate<ConsumerRecord<String, T>> messageSelector,
      Duration timeout) throws InterruptedException {
    LocalDateTime deadline = LocalDateTime.now().plus(timeout);
    while (true) {
      Duration timeLeft = Duration.between(LocalDateTime.now(), deadline);
      var record = receivedRecords.poll(timeLeft.toMillis(), TimeUnit.MILLISECONDS);
      if (record == null) {
        return Optional.empty();
      }
      if (messageSelector.test(record)) {
        log.info("Received message matched test: {}", record);
        return Optional.of(record);
      } else {
        log.info("Discarding message not matching test: {}", record);
      }
    }
  }
}