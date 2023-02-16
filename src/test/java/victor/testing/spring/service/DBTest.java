package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.tools.TestcontainersUtils;

@SpringBootTest
@ActiveProfiles("db-migration")
@Testcontainers
@Transactional
// [THE BEST WAY to test with RDB] tell spring to start each test in a new transaction and  ROLLBACK after each test.
public abstract class DBTest {
  // === B) Reuse the container across multiple test classes ===
  static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11").withReuse(true);
  @BeforeAll
  public static void startTestcontainer() {
    postgres.start();
  }
  @DynamicPropertySource
  public static void registerPgProperties(DynamicPropertyRegistry registry) {
    TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
  }
}
