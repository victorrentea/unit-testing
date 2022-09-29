package victor.testing.spring.service.subpa;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import victor.testing.tools.TestcontainersUtils;

public class BaseClass {
    @Container
    static public PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:11");

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
    }
}
