package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.tools.WireMockExtension;

import javax.annotation.RegEx;

@Testcontainers
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@Transactional
@ActiveProfiles("db-migration")
public abstract class TestRepoBase {
    static public PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:11").withReuse(true);
    @BeforeAll
    public static void startTestcontainer() {
        postgres.start(); // porneste 1 data global pt toate testele
    }

    @RegisterExtension
    public WireMockExtension wireMock = new WireMockExtension(9999);
}
