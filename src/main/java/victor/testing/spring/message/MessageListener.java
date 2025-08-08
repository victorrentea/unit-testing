package victor.testing.spring.message;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class MessageListener {
  public static final String SUPPLIER_CREATED_EVENT = "supplier-created-event";
  private final BizLogic bizLogic;

  @SneakyThrows
  @KafkaListener(topics = SUPPLIER_CREATED_EVENT)
  public void onMessage(String supplierName)  {
//    Thread.sleep(2000);
    bizLogic.bizLogic(supplierName);
  }
}

