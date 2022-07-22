package victor.testing.spring.service.subpachet;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
public abstract class BaseDBInMemoryTest {
}
