package victor.testing.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.tools.TestcontainersUtils;

// #1 innocent Testcontainers test (online examples)
@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
public abstract class IntegrationTest {
}

// ==================================================================
// #2 innocent Testcontainers test (online examples)
//@SpringBootTest
//@Testcontainers
//public abstract class BaseDatabaseTest {
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
//public abstract class BaseDatabaseTest {
//
//}