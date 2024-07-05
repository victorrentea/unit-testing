package victor.testing.spring.message;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true", matchIfMissing = true)
@Validated
public class MessageListener {
  private final SupplierRepo supplierRepo;
  private final KafkaTemplate<String, String> kafkaTemplate;


  // @Scheduled
  // fire-and-forget:
  //   - CompletableFuture.runAsync(() -> { ... }) la care nu faci get
  //   - @Async void f() {}
  @SneakyThrows
  @KafkaListener(topics = "supplier-created-event")
  public void onMessage(String supplierName) {
    log.info("Received message: " + supplierName);
//    if (true) throw new RuntimeException("Intentional");
//    supplierRepo.save(new Supplier().setName(supplierName));
    log.info("Created supplier with name: " + supplierName);
//    kafkaTemplate.send("pt-bi-cu-dragoste", "k", "dragoste💖 " + supplierName);
    log.info("Message sent");
  }
  @SneakyThrows
  @KafkaListener(topics = "marcel")
  public void onMarcel( @Valid @Payload Message1 message) {
log.info("Got: "+message);
  }
  public record Message1(@NotBlank String name) {
  }
}
