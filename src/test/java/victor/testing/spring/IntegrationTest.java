package victor.testing.spring;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductMapper;

// #1 in-mem H2 database (traditional)
@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTest {
  @MockBean
  protected SafetyClient safetyClient;
  @MockBean // in plus fata de cealalta clasa
  protected ProductMapper productMapper;
  @MockBean
  protected KafkaTemplate<String, String> kafkaTemplate;


  @Autowired
  protected SupplierRepo supplierRepo;

  @BeforeEach
  void insertReferenceData() { // tot in aceeasi tx cu @Test
    supplierRepo.save(new Supplier().setCode("s"));
  }


  @BeforeEach
  void checkAllTablesAreEmpty() { // testul imediat dupa unul vinovat va crapa
    if(supplierRepo.count() != 0) {
      throw new IllegalStateException("Tables are not empty");
    }
  }
}

// ==================================================================
// #2 naive Testcontainers (online examples)
//@SpringBootTest
//@Testcontainers
//public abstract class IntegrationTest {
//  // https://stackoverflow.com/questions/62425598/how-to-reuse-testcontainers-between-multiple-springboottests
//  // === The containers is reused across all subclasses ===
//  static public PostgreSQLContainer<?> postgres =
//      new PostgreSQLContainer<>("postgres:11");
//
//  // TODO add in ~/.testcontainers.properties put testcontainers.reuse.enable=true
//
//  @BeforeAll
//  public static void startTestcontainer() {
//    postgres.start();
//  }
//
//  @DynamicPropertySource
//  public static void registerPgProperties(DynamicPropertyRegistry registry) {
//    TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
//  }
//}

// ==================================================================
// #3 frameworks over Testcontainers (like a platform team will ofer)
//@SpringBootTest
//@ActiveProfiles("db-testcontainers-playtika")
//public abstract class IntegrationTest {
//
//}
