package victor.testing.spring.message;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageListener {
  public static final String SUPPLIER_CREATED_EVENT = "supplier-created-event";

  @SqsListener("${supplier.created.event}")
  public void onMessage(String supplierName) {
    service.logic(supplierName);
  }
  private final SomeServiceWithAllTheLogic service;
}

