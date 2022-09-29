package victor.testing.spring.service.subpa;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.tools.TestcontainersUtils;


@SpringBootTest
@Testcontainers
@Transactional
@ActiveProfiles("db-migration")
@Sql("classpath:/sql/common-reference-data.sql")
public class BaseClass {
    public static final long SUPPLIER_ID = 99L;
//    @Container
    static public PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:11")
                    .withReuse(true);

    @BeforeAll
    public static void startTestcontainer() {
        postgres.start();
    }

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
    }

}
