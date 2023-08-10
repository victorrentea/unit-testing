package victor.testing.spring.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.repo.SupplierRepo;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class MessageListener {
  private final SupplierRepo supplierRepo;

  @KafkaListener(topics = "${input.topic}")
  public void onMessage(ConsumerRecord<String, String> record) {
    log.info("Received message: " + record);
    // serious logic
    supplierRepo.save(new Supplier().setName(record.value()));
    log.info("Created supplier with name: " + record);
//    rabbitTempplate.send()
  }
}
