package victor.testing.spring.message;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

@Slf4j
@RequiredArgsConstructor
@Service
public class SomeServiceWithAllTheLogic {
  private final SupplierRepo supplierRepo;
  private final SqsTemplate sqsTemplate;
  public void logic(String supplierName) {
    log.info("Received new supplier name: {}", supplierName);
    if (supplierRepo.existsByName(supplierName)) {
      log.info("Supplier already exists"); // idempotent consumer
      return;
    }
    supplierRepo.save(new Supplier()
        .setName(supplierName));
    log.info("Created supplier");

    sqsTemplate.send("out-queue", supplierName);
  }
}
