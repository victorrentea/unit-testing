package victor.testing.spring.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class MessageListener {
  private final SupplierRepo supplierRepo;

  @KafkaListener(topics = "supplier-created-event")
  public void onMessage(String supplierName) {
    log.info("Received message: " + supplierName);
    supplierRepo.save(new Supplier().setName(supplierName));
    log.info("Created supplier with name: " + supplierName);
  }
}
