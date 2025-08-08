package victor.testing.tools;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

public class AbstractTestListener<T> {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected LinkedBlockingQueue<ConsumerRecord<String, T>> receivedRecords = new LinkedBlockingQueue<>();

    protected void receive(ConsumerRecord<String, T> record) {
      log.debug("Test listener received message: {}", record);
      receivedRecords.add(record);
    }

    public ConsumerRecord<String, T> blockingReceiveForHeader(
        String headerKey, String headerValue, Duration timeout) throws ExecutionException, InterruptedException, TimeoutException {

      return blockingReceive(record -> filterByHeader(headerKey, headerValue, record), timeout);
    }

    private boolean filterByHeader(String headerKey, String headerValue, ConsumerRecord<String, T> record) {
      Header header = record.headers().lastHeader(headerKey);
      if (header == null) return false;
      return headerValue.equals(new String(header.value()));
    }

    // drop all until I find one that matches the selector or timeout occurs
    // Challenge: uniquely identify the message expected (eg: use an UUID)
    public ConsumerRecord<String, T> blockingReceive(
        Predicate<ConsumerRecord<String, T>> messageSelector, Duration timeout) throws ExecutionException, InterruptedException, TimeoutException {
      LocalDateTime deadline = LocalDateTime.now().plus(timeout);
      while (true) {
        Duration timeLeft = Duration.between(LocalDateTime.now(), deadline);
        var record = receivedRecords.poll(timeLeft.toMillis(), TimeUnit.MILLISECONDS);
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

  public void drain() {
    // Repeatedly call blockingReceive with a very small timeout for up to 3 seconds.
    // If a message arrives immediately, loop again to catch trailing messages.
    final LocalDateTime deadline = LocalDateTime.now().plusSeconds(1);
    while (LocalDateTime.now().isBefore(deadline)) {
      // Do not block past the deadline
      Duration remaining = Duration.between(LocalDateTime.now(), deadline);
      if (remaining.isNegative()) {
        break;
      }
      Duration attemptTimeout = Duration.ofMillis(10);
      if (attemptTimeout.compareTo(remaining) > 0) {
        attemptTimeout = remaining;
      }
      try {
        // Accept any message
        blockingReceive(r -> true, attemptTimeout);
        // If we got one, immediately try again in the same loop iteration
        // to catch any trailing messages.
      } catch (java.util.concurrent.TimeoutException e) {
        // No message in this tiny window; keep looping until deadline.
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
      } catch (ExecutionException e) {
        // Unexpected during drain; log and continue trying until deadline
        log.warn("Unexpected exception while draining messages", e);
      }
    }
  }
}