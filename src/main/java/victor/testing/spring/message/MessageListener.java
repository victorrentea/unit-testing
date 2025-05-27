package victor.testing.spring.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class MessageListener {
  public static final String SUPPLIER_CREATED_EVENT = "supplier-created-event";
  private final SupplierRepo supplierRepo;

  @KafkaListener(topics = SUPPLIER_CREATED_EVENT)
  public void onMessage(String supplierName) {
    log.info("Received new supplier name: {}", supplierName);
    // TODO if (!supplierRepo.existsByName(supplierName))
    supplierRepo.save(new Supplier().setName(supplierName));
    log.info("Created supplier");
  }
}
