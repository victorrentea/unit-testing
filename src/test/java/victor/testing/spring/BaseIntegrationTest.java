package victor.testing.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

// #1 innocent Testcontainers test (online examples)
@SpringBootTest
@ActiveProfiles({"db-mem", "wiremock"})
@AutoConfigureWireMock(port = 0) // porneste un server HTTP din teste
public class BaseIntegrationTest {
  @MockBean
  protected KafkaTemplate<String, String> kafkaTemplate;
}

// ==================================================================
// #2 innocent Testcontainers test (online examples)
//@SpringBootTest
//@Testcontainers
//public class BaseDatabaseTest {
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
// #3 tuned Testcontainers test (like a platform team will ofer)
//@SpringBootTest
//@ActiveProfiles("db-testcontainers-playtika")
//public class BaseDatabaseTest {
//
//}
