package victor.testing.spring;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.tools.TestcontainersUtils;

// #1 innocent Testcontainers test (online examples)
@SpringBootTest
@Testcontainers
@Sql(scripts = "classpath:/sql/cleanup.sql") //#2 for monster DB schemas
public abstract class IntegrationTest {
  @MockBean
  protected SafetyClient safetyClient;
  @MockBean
  protected KafkaTemplate<String, String> kafkaTemplate;

  // https://stackoverflow.com/questions/62425598/how-to-reuse-testcontainers-between-multiple-springboottests
  // === The containers is reused across all subclasses ===
  static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11");

  // TODO add in ~/.testcontainers.properties put testcontainers.reuse.enable=true

  @BeforeAll
  public static void startTestcontainer() {
    postgres.start();
  }

  @DynamicPropertySource
  public static void registerPgProperties(DynamicPropertyRegistry registry) {
    TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
  }
}

// ==================================================================
// #2 innocent Testcontainers test (online examples)
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
// #3 tuned Testcontainers test (like a platform team will ofer)
//@SpringBootTest
//@ActiveProfiles("db-testcontainers-playtika")
//public abstract class IntegrationTest {
//
//}
