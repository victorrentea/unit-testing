package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

//@Cleanup
@ActiveProfiles("db-mem")
@SpringBootTest
@Transactional
public abstract class BaseTest {
    @BeforeEach
    final void before() {
        System.out.println("Sieu");
    }
}
