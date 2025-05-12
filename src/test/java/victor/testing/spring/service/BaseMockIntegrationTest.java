package victor.testing.spring.service;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

@EmbeddedKafka
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWireMock(port = 0)
public abstract class BaseMockIntegrationTest {
  @MockBean
  protected SupplierRepo supplierRepo;
  @MockBean
  protected ProductRepo productRepo;
  @MockBean
  protected KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
}
