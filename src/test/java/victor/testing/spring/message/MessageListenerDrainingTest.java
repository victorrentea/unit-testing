//package victor.testing.spring.message;
//
//import io.awspring.cloud.sqs.operations.SqsTemplate;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import victor.testing.spring.IntegrationTest;
//import victor.testing.spring.OutQueueTestListener;
//import victor.testing.spring.repo.SupplierRepo;
//
//import static java.time.Duration.ofSeconds;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.awaitility.Awaitility.await;
//
//@TestMethodOrder(MethodOrderer.MethodName.class)
//public class MessageListenerDrainingTest extends IntegrationTest {
//  @Autowired
//  SqsTemplate sqsTemplate;
//  @Autowired
//  SupplierRepo supplierRepo;
//  @Value("${supplier.created.event}")
//  String queueName;
//  @Autowired
//  private OutQueueTestListener outQueueTestListener;
//
//  @BeforeEach
//  final void before() {
//    supplierRepo.deleteAll();
//    outQueueTestListener.drain(); //!!
//  }
//
//  @Test
//  void s1upplierIsCreated() throws Exception {
//    sqsTemplate.send(queueName, "A");
//
//    await().timeout(ofSeconds(5))
//        .untilAsserted(() ->
//            assertThat(supplierRepo.findByName("A")).isPresent());
//  }
//
//  @Test
//  void s2upplierIsCreatedBis() throws Exception {
//    sqsTemplate.send(queueName, "B");
//
//    var s = outQueueTestListener.blockingReceive(ofSeconds(5));
//    assertThat(s).isEqualTo("B");
//  }
//
//}
