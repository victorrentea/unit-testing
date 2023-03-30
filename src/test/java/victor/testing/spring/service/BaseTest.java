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
@Transactional
@Testcontainers
@ActiveProfiles("db-migration")
public abstract class BaseTest {
  // https://stackoverflow.com/questions/62425598/how-to-reuse-testcontainers-between-multiple-springboottests
  // === The containers is reused across all subclasses ===
  static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
          "postgres:11")
          .withReuse(true); // BE CAREFUL to allow testcontainers to reuse the container across different test classes

  @BeforeAll
  public static void startTestcontainer() {
    System.out.println("(re)Starting testcontainer");
    postgres.start();
  }

  @DynamicPropertySource
  public static void registerPgProperties(DynamicPropertyRegistry registry) {
    TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
  }

}
