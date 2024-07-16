package victor.testing.spring.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class MessageListener {
  public static final String SUPPLIER_CREATED_ERROR = "supplier-created-error";
  private final SupplierRepo supplierRepo;
  private final KafkaTemplate<String, String> kafkaTemplate;

  @KafkaListener(topics = "supplier-created-event")
  public void onMessage(String supplierName) {
    log.info("Received message: {}", supplierName);
    if (supplierRepo.findByName(supplierName).isPresent()) {
      kafkaTemplate.send(SUPPLIER_CREATED_ERROR, "k", "Supplier already exists: " + supplierName);
      return;
    }
    supplierRepo.save(new Supplier().setName(supplierName));
    log.info("Created supplier with name: {}", supplierName);
    // TODO throw an exception
  }
}
