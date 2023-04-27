package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.tools.TestcontainersUtils;

//@WebMvcTest // asta in loc de @SpringBootTest nu porneste decat controller =>
// nu booteaza Hibernate, Actuator, cahce, queue
// presupune ca ai @MockBean orice clasa chemata din controller.
@SpringBootTest
@Testcontainers
@Transactional
@ActiveProfiles("db-migration")
public class BaseDatabaseTest { // inherit from this test class

  // https://stackoverflow.com/questions/62425598/how-to-reuse-testcontainers-between-multiple-springboottests
  // === The containers is reused across all subclasses ===
  static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
          "postgres:11")
//          .withReuse(true)
          ;

  @BeforeAll
  public static void startTestcontainer() {
    System.out.println("Starting testcontainer");
    postgres.start();
  }

  @DynamicPropertySource
  public static void registerPgProperties(DynamicPropertyRegistry registry) {
    TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
  }

  // TODO in ~/.testcontainers.properties put testcontainers.reuse.enable=true

}
