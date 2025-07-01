package victor.testing.spring.message;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class MessageListener2Test extends IntegrationTest {
  public static final String SUPPLIER_NAME = "supplier";
  @Autowired
  SqsTemplate sqsTemplate;
  @Autowired
  SupplierRepo supplierRepo;
  @Value("${supplier.created.event}")
  String queueName;
  @MockitoSpyBean // @SpyBean = "wrap" the real bun with a mockito spy so that I can intercept its method calls
  SomeServiceWithAllTheLogic service;

  @BeforeEach
  final void before() {
    supplierRepo.deleteAll();
  }

  @Test
  void supplierIsCreated() throws Exception {
    sqsTemplate.send(queueName, SUPPLIER_NAME);

    await().timeout(ofSeconds(5))
        .untilAsserted(() ->
            assertThat(supplierRepo.findByName(SUPPLIER_NAME)).isPresent());
  }

  @Test
  void supplierIsCreated_skipsDuplicates() throws Exception {
    supplierRepo.save(new Supplier().setName(SUPPLIER_NAME));

    // when / act
    sqsTemplate.send(queueName, SUPPLIER_NAME);

    Thread.sleep(5000); // 🤬 when passing eats time of CI
    // this test might fail on a slow INTEL 4GB RAM not NICE machine, 5s could be too little.
    // a=> increase the value for the weakest machine of any colleague
    // b=> but a MacBook pro Max32GB => build 6x faster!
    // c=> decouple all complexity from an async entry point (@Sqs, @Async, @Scheduled, @Kafka..)
    //   and extract into a separate class that you test separately
    assertThat(supplierRepo.findAll()).hasSize(1);
  }

  @Test
  void listenerCallsService() throws Exception {
    sqsTemplate.send(queueName, SUPPLIER_NAME);

    // mockit blocks for 5 sec max until the method is called!
    Mockito.verify(service, Mockito.timeout(5000)).logic(SUPPLIER_NAME);
  }

  @Test
  void testDirectlyTheLogic() throws Exception {
    supplierRepo.save(new Supplier().setName(SUPPLIER_NAME));

    // when / act
    service.logic(SUPPLIER_NAME);
    // 💖 no need to wait x seconds
    // 💖 you get the exceptions back from prod!!!

    assertThat(supplierRepo.findAll()).hasSize(1);
  }
}
