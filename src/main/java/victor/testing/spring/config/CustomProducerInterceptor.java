package victor.testing.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class CustomProducerInterceptor implements ProducerInterceptor<String, String> {
  @Override
  public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
    // Add headers to the outgoing message
    log.debug("Adding tenant-id header to the message");
    String tenantId = MDC.get("tenantId"); // picks data from a thread local source
    record.headers().add("tenant-id", tenantId.getBytes());
    return record;
  }

  @Override
  public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    // No action needed on acknowledgement
  }

  @Override
  public void close() {
    // No action needed on close
  }

  @Override
  public void configure(Map<String, ?> configs) {
    // No action needed on configure
  }
}
